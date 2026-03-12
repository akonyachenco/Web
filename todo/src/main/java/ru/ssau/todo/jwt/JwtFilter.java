package ru.ssau.todo.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ssau.todo.exception.ExpiredTokenException;
import ru.ssau.todo.exception.InvalidTokenFormatException;
import ru.ssau.todo.exception.InvalidTokenSignatureException;
import ru.ssau.todo.service.TokenService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final static List<String> IGNORED_PATHS = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/refresh"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            String token = tokenService.extractTokenFromHeader(authHeader);

            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing or invalid Authorization header");
                return;
            }

            Map<String, Object> payload = tokenService.validateToken(token);

            Long userId = ((Number) payload.get("userId")).longValue();
            List<String> roles = (List<String>) payload.get("roles");

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        }
        catch (ExpiredTokenException
               | InvalidTokenFormatException
               | InvalidTokenSignatureException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());

        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return IGNORED_PATHS.contains(request.getRequestURI());
    }
}
