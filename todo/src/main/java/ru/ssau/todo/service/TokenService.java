package ru.ssau.todo.service;

import org.springframework.stereotype.Service;
import ru.ssau.todo.dto.UserDto;
import ru.ssau.todo.exception.ExpiredTokenException;
import ru.ssau.todo.exception.InvalidTokenFormatException;
import ru.ssau.todo.exception.InvalidTokenSignatureException;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    private final String secret = System.getenv("JWT_SECRET");
    private final static long ACCESS_EXPIRATION_TIME = 15 * 60;
    private final static long REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateAccessToken(UserDto user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        payload.put("roles", user.getRoles());
        payload.put("iat", Instant.now().getEpochSecond());
        payload.put("exp", Instant.now().getEpochSecond() + ACCESS_EXPIRATION_TIME);

        return generateToken(payload);
    }

    public String generateRefreshToken(UserDto user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        payload.put("iat", Instant.now().getEpochSecond());
        payload.put("exp", Instant.now().getEpochSecond() + REFRESH_EXPIRATION_TIME);

        return generateToken(payload);
    }

    private String createSignature(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

            mac.init(key);

            byte[] signatureBytes = mac.doFinal(payload.getBytes());

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(signatureBytes);

        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to create signature", e);
        }

    }

    public String generateToken(Map<String, Object> payload) {

        // Шаг 1. Преобразование payload в JSON
        String json = objectMapper.writeValueAsString(payload);

        // Шаг 2. Кодирование payload
        String encodedPayload =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(json.getBytes());

        // Шаг 3. Создание подписи
        // Шаг 4. Кодирование подписи
        String encodedSignature = createSignature(encodedPayload);

        //Шаг 5. Сборка токена
        return String.format("%s.%s", encodedPayload, encodedSignature);

    }

    public String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public Map<String, Object> validateToken(String token)
            throws ExpiredTokenException,
            InvalidTokenFormatException,
            InvalidTokenSignatureException {

        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            throw new InvalidTokenFormatException();
        }

        String payload = parts[0];
        String signature = parts[1];

        String expectedSignature = createSignature(payload);
        if (!expectedSignature.equals(signature)) {
            throw new InvalidTokenSignatureException();
        }

        byte[] decoded =
                Base64.getUrlDecoder().decode(payload);

        Map<String, Object> payloadMap = objectMapper.readValue(decoded, Map.class);

        if (Instant.now().getEpochSecond() > ((Number) payloadMap.get("exp")).longValue())
            throw new ExpiredTokenException();

        return payloadMap;

    }
}
