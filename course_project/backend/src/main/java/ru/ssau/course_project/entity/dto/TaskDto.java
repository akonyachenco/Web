package ru.ssau.course_project.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private LocalDateTime createdAt;
    @NotNull(message = "Статус обязателен")
    private String statusName;
    @NotNull(message = "Cотрудник обязателен")
    private Long employeeId;
    @NotNull(message = "Спринт обязателен")
    private Long sprintId;
}
