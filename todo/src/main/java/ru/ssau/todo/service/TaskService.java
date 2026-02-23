package ru.ssau.todo.service;

import org.springframework.stereotype.Service;
import ru.ssau.todo.dto.TaskDto;
import ru.ssau.todo.dto.mapper.TaskMapper;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskStatus;
import ru.ssau.todo.exception.BusinessLogicException;
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

    TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDto create(TaskDto task) throws BusinessLogicException, NotFoundException {
        userRepository.findById(task.getCreatedBy()).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        long activeCount = taskRepository.countActiveTasksByUserId(task.getCreatedBy());
        if ((task.getStatus().equals(TaskStatus.OPEN)
                || task.getStatus().equals(TaskStatus.IN_PROGRESS))
                && activeCount >= 10)
            throw new BusinessLogicException("Maximum 10 active tasks per user");
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


    public void update(TaskDto task) throws NotFoundException, BusinessLogicException
    {
        Task oldTask = taskRepository.findById(task.getId()).orElseThrow(
                () -> new NotFoundException("Task not found")
        );
        if (task.getStatus().equals(TaskStatus.OPEN) || task.getStatus().equals(TaskStatus.IN_PROGRESS)) {
            if ((oldTask.getStatus().equals(TaskStatus.CLOSED)
                    || oldTask.getStatus().equals(TaskStatus.DONE))
                    && taskRepository.countActiveTasksByUserId(task.getCreatedBy()) >= 10) {
                throw new BusinessLogicException("Maximum 10 active tasks per user");
            }
        }
        oldTask.setStatus(task.getStatus());
        oldTask.setTitle(task.getTitle());
        taskRepository.save(oldTask);
    }


    public void deleteById(long id) throws BusinessLogicException {
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
