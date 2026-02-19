package ru.ssau.todo.service;

import org.springframework.stereotype.Service;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.exception.BusinessLogicException;
import ru.ssau.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(Task task) {
        long activeCount = taskRepository.countActiveTasksByUserId(task.getCreatedBy());
        if (activeCount >= 10) throw new BusinessLogicException("Maximum 10 active tasks per user");
        return taskRepository.create(task);
    }


    public Optional<Task> findById(long id) {
        return taskRepository.findById(id);
    }


    public List<Task> findAll(LocalDateTime from, LocalDateTime to, long userId) {
        return taskRepository.findAll(from, to, userId);
    }


    public void update(Task task) throws Exception {
        taskRepository.update(task);
    }


    public void deleteById(long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(taskOptional.isPresent()) {
            if(taskOptional.get().getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5)))
                throw new BusinessLogicException("Task was created less than 5 minutes ago");
        }
        taskRepository.deleteById(id);
    }


    public long countActiveTasksByUserId(long userId) {
        return taskRepository.countActiveTasksByUserId(userId);
    }
}
