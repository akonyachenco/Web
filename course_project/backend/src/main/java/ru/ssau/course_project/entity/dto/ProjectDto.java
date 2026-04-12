package ru.ssau.course_project.entity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    @Positive
    private Long cost;
    private LocalDate startDate;
    private LocalDate deadline;
    @NotNull(message = "Статус обязателен")
    private String statusName;
//    private List<EmployeeDto> team;
//    private List<SprintDto> sprints;
}
