package ru.ssau.course_project.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoleDto {
    private Short id;
    private String name;
    private List<EmployeeDto> employees;
}
