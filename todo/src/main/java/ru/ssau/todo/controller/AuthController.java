package ru.ssau.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.dto.TokenResponseDto;
import ru.ssau.todo.dto.UserDto;
import ru.ssau.todo.dto.UserRequestDto;
import ru.ssau.todo.dto.UserResponseDto;
import ru.ssau.todo.exception.*;
import ru.ssau.todo.service.CustomUserDetailsService;
import ru.ssau.todo.service.TokenService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            UserDto user = userDetailsService.getCurrentUser();

            UserResponseDto dto = new UserResponseDto();
            dto.setUsername(user.getUsername());
            dto.setRoles(user.getRoles());

            return ResponseEntity.ok(dto);
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto dto) {
        try {
            userDetailsService.register(dto);
            return ResponseEntity.ok().build();
        }
        catch (DuplicateUsernameException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto dto) {
        try {
            UserDto user = userDetailsService.findByUsername(dto.getUsername());

            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("Invalid password");
            }

            String accessToken = tokenService.generateAccessToken(user);
            String refreshToken = tokenService.generateRefreshToken(user);

            TokenResponseDto tokenResponseDto = new TokenResponseDto();
            tokenResponseDto.setAccessToken(accessToken);
            tokenResponseDto.setRefreshToken(refreshToken);

            return ResponseEntity.ok(tokenResponseDto);
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {

        try {
            String refreshToken = body.get("refreshToken");
            if (refreshToken != null) {
                Map<String, Object> payload = tokenService.validateToken(refreshToken);

                Long userId = ((Number) payload.get("userId")).longValue();

                UserDto user = userDetailsService.findById(userId);

                String accessToken = tokenService.generateAccessToken(user);

                TokenResponseDto tokenResponseDto = new TokenResponseDto();
                tokenResponseDto.setAccessToken(accessToken);
                tokenResponseDto.setRefreshToken(refreshToken);

                return ResponseEntity.ok(tokenResponseDto);
            }
            return ResponseEntity.status(401).body("Invalid refresh token");

        }
        catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (ExpiredTokenException
                | InvalidTokenFormatException
                | InvalidTokenSignatureException exception) {
            return ResponseEntity.status(401).body(exception.getMessage());

        }
    }
}
