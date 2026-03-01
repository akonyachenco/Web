package ru.ssau.todo.service;

import org.springframework.stereotype.Service;
import ru.ssau.todo.dto.TaskDto;
import ru.ssau.todo.dto.mapper.TaskMapper;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskStatus;
import ru.ssau.todo.exception.ActiveTaskCountException;
import ru.ssau.todo.exception.DeletedTimeException;
import ru.ssau.todo.exception.NotFoundException;
import ru.ssau.todo.repository.TaskRepository;
import ru.ssau.todo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final short maxActiveTasks = 10;
    private final short deletedTime = 5;

    TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDto create(TaskDto task) throws ActiveTaskCountException, NotFoundException {
        userRepository.findById(task.getCreatedBy()).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        long activeCount = taskRepository.countActiveTasksByUserId(task.getCreatedBy());
        if (task.isActive()
                && activeCount >= maxActiveTasks)
            throw new ActiveTaskCountException("Maximum " + maxActiveTasks + " active tasks per user");
        task.setCreatedAt(LocalDateTime.now());
        return taskMapper.toDto(taskRepository.save(taskMapper.toEntity(task)));
    }



    public TaskDto findById(long id) throws NotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        return taskMapper.toDto(task);
    }


    public List<TaskDto> findAll(LocalDateTime from, LocalDateTime to, long userId) {
        return taskMapper.toDto(taskRepository.findAll(from, to, userId));
    }


    public void update(TaskDto task) throws NotFoundException, ActiveTaskCountException
    {
        Task oldTask = taskRepository.findById(task.getId()).orElseThrow(
                () -> new NotFoundException("Task not found")
        );
        if (task.isActive()) {
            if ((oldTask.getStatus().equals(TaskStatus.CLOSED)
                    || oldTask.getStatus().equals(TaskStatus.DONE))
                    && taskRepository.countActiveTasksByUserId(task.getCreatedBy()) >= maxActiveTasks) {
                throw new ActiveTaskCountException("Maximum " + maxActiveTasks + " active tasks per user");
            }
        }
        oldTask.setStatus(task.getStatus());
        oldTask.setTitle(task.getTitle());
        taskRepository.save(oldTask);
    }


    public void deleteById(long id) throws DeletedTimeException {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(taskOptional.isPresent()) {
            if(taskOptional.get().getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(deletedTime)))
                throw new DeletedTimeException("Task was created less than " + deletedTime + " minutes ago");
        }
        taskRepository.deleteById(id);
    }


    public long countActiveTasksByUserId(long userId) {
        return taskRepository.countActiveTasksByUserId(userId);
    }
}
