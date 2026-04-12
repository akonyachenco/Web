package ru.ssau.course_project.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String position;
    private List<String> roles;
//    private List<ProjectDto> projects;
//    private List<TaskDto> tasks;

}
