package ru.ssau.course_project.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.Role;


import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtService {

    private final static String SECRET = System.getenv("JWT_SECRET");

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Key getSignedKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateAccessToken(Employee employee) {
        Date expirationDate = Date.from(Instant.now().plusSeconds(15 * 60));
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", employee.getRoles().stream().map(Role::getName).toList());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(employee.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Employee employee) {
        Date expirationDate = Date.from(Instant.now().plusSeconds(24 * 60 * 60));
        return Jwts.builder()
                .setSubject(employee.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        return objectMapper.convertValue(
                extractAllClaims(token).get("roles"),
                new TypeReference<List<String>>() {});
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
