package ru.ssau.course_project.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SprintDto {
    private Long id;
    private String name;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull(message = "Статус обязателен")
    private Short statusId;
    @NotNull(message = "Проект обязателен")
    private Long projectId;
    private List<TaskDto> tasks;
}
