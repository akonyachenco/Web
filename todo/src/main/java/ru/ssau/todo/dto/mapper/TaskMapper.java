package ru.ssau.todo.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ssau.todo.dto.TaskDto;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.exception.NotFoundException;
import ru.ssau.todo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Component
@AllArgsConstructor
public class TaskMapper {

    private final UserRepository userRepository;

    public TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setCreatedBy(task.getUser().getId());
        return dto;
    }

    public Task toEntity(TaskDto dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setStatus(dto.getStatus());
        task.setCreatedAt(dto.getCreatedAt());
        task.setUser(userRepository.findById(dto.getCreatedBy()).orElseThrow(
                () -> new NotFoundException("User not found")));
        return task;
    }

    public List<TaskDto> toDto(List<Task> tasks) {
        List<TaskDto> res = new ArrayList<>();
        for (Task task : tasks) {
            res.add(toDto(task));
        }
        return res;
    }
}
