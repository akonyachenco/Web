package ru.ssau.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.exception.BusinessLogicException;
import ru.ssau.todo.exception.NotFoundException;
import ru.ssau.todo.service.TaskService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestParam(required = false) LocalDateTime from,
                               @RequestParam(required = false) LocalDateTime to,
                               @RequestParam long userId) {
        if(from == null) from = LocalDateTime.MIN;
        if(to == null) to = LocalDateTime.MAX;
        return ResponseEntity.ok(taskService.findAll(from, to, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable long id) {
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try {
            Task res = taskService.create(task);
            return ResponseEntity.created(URI.create("/tasks/" + res.getId()))
                    .body(res);
        }
        catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable long id, @RequestBody Task task) {
        task.setId(id);
        try{
            taskService.update(task);
            return ResponseEntity.ok().build();
        }
        catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        try{
            taskService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        catch (BusinessLogicException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/active/count")
    public ResponseEntity<Long> getActiveTaskCount(@RequestParam long userId) {
        return ResponseEntity.ok(taskService.countActiveTasksByUserId(userId));
    }
}
