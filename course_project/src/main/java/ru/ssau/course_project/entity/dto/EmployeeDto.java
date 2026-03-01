package ru.ssau.course_project.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    @NotBlank(message = "Логин обязателен")
    private String login;
    private String position;
    private List<RoleDto> roles;
    private List<ProjectDto> projects;
    private List<TaskDto> tasks;

}
