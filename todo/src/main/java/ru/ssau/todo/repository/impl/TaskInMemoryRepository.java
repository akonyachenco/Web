package ru.ssau.todo.repository.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskStatus;
import ru.ssau.todo.exception.NotFoundException;
import ru.ssau.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("default")
public class TaskInMemoryRepository implements TaskRepository {

    private final Map<Long, Task> storage = new HashMap<>();
    private long counter;

    public TaskInMemoryRepository() {
        this.counter = 0;
    }


    @Override
    public Task create(Task task) {
        if(task == null) throw new IllegalArgumentException("Task cannot be null");

        task.setCreatedAt(LocalDateTime.now());
        task.setId(++counter);

        storage.put(counter, task);
        return task;
    }

    @Override
    public Optional<Task> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Task> findAll(LocalDateTime from, LocalDateTime to, long userId) {
        return storage.values().stream()
                .filter(task -> task.getCreatedBy() == userId)
                .filter(task -> {
                    LocalDateTime createdAt = task.getCreatedAt();
                    return !createdAt.isBefore(from) && !createdAt.isAfter(to);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void update(Task task) throws Exception {
        long id = task.getId();
        if(!storage.containsKey(id))
            throw new NotFoundException("Task with id " + task.getId() + " not found");

        Task taskToUpdate = storage.get(id);
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setStatus(task.getStatus());
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }

    @Override
    public long countActiveTasksByUserId(long userId) {
        return storage.values().stream()
                .filter(task -> task.getCreatedBy() == userId)
                .filter(task -> task.getStatus().equals(TaskStatus.IN_PROGRESS)
                        || task.getStatus().equals(TaskStatus.OPEN))
                .count();
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

}
