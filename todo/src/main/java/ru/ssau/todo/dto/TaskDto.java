package ru.ssau.todo.dto;

import lombok.Data;
import ru.ssau.todo.entity.TaskStatus;

import java.time.LocalDateTime;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private TaskStatus status;
    private Long createdBy;
    private LocalDateTime createdAt;
}
