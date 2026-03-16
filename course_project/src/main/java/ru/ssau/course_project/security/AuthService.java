package ru.ssau.course_project.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.entity.dto.LoginDto;
import ru.ssau.course_project.entity.dto.RegistrationDto;
import ru.ssau.course_project.entity.dto.TokenResponseDto;
import ru.ssau.course_project.entity.dto.mapper.EmployeeMapper;
import ru.ssau.course_project.repository.EmployeeRepository;
import ru.ssau.course_project.repository.RoleRepository;
import ru.ssau.course_project.service.EmployeeService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmployeeMapper employeeMapper;
    private final RoleRepository roleRepository;
    private static final String DEFAULT_ROLE = "ROLE_USER";

    public TokenResponseDto register(RegistrationDto dto)
            throws IllegalArgumentException, EntityNotFoundException, DuplicateKeyException {

        if (dto == null) throw new IllegalArgumentException();

        if (employeeRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new DuplicateKeyException("Имя пользователя занято");
        }

        Employee employee = employeeMapper.toEntity(dto);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setRoles(List.of(
                roleRepository.findByName(DEFAULT_ROLE)
                        .orElseThrow(
                                () -> new EntityNotFoundException(String.format("Роль %s не найдена", DEFAULT_ROLE))
                        )
                )
        );
        employeeRepository.save(employee);

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(dto.getUsername());
        loginDto.setPassword(dto.getPassword());

        return login(loginDto);
    }

    public TokenResponseDto login(LoginDto dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        Employee employee = employeeRepository.findByUsername(dto.getUsername()).get();

        String accessToken = jwtService.generateAccessToken(employee);
        String refreshToken = jwtService.generateRefreshToken(employee);

        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setAccessToken(accessToken);
        tokenResponseDto.setRefreshToken(refreshToken);

        return tokenResponseDto;
    }

    public TokenResponseDto refreshToken(String refreshToken) throws EntityNotFoundException {
        if (refreshToken != null && jwtService.isTokenValid(refreshToken)) {

            String username = jwtService.extractUsername(refreshToken);
            Employee employee = employeeRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Сотрудник %s не найден", username)));

            String accessToken = jwtService.generateAccessToken(employee);

            TokenResponseDto tokenResponseDto = new TokenResponseDto();
            tokenResponseDto.setAccessToken(accessToken);
            tokenResponseDto.setRefreshToken(refreshToken);
            return tokenResponseDto;
        }
        return null;
    }

    public EmployeeDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return employeeService.findByUsername(authentication.getName());
        }
        return null;
    }
}
