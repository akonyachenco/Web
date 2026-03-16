package ru.ssau.course_project.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.LoginDto;
import ru.ssau.course_project.entity.dto.RegistrationDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(LoginDto loginDto) {
        try {
            return ResponseEntity.ok(authService.login(loginDto));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(RegistrationDto registrationDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registrationDto));
        }
        catch (IllegalArgumentException | EntityNotFoundException | DuplicateKeyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String refreshToken) {
        try {
            return ResponseEntity.ok(authService.refreshToken(refreshToken));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
