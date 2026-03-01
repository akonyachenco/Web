package ru.ssau.course_project.entity.dto;

import jakarta.validation.constraints.NotBlank;

public class RegistrationDto extends EmployeeDto{
    @NotBlank(message = "Пароль обязателен")
    private String password;
}
