package ru.ssau.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.todo.dto.TaskDto;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.exception.ActiveTaskCountException;
import ru.ssau.todo.exception.DeletedTimeException;
import ru.ssau.todo.exception.NotFoundException;
import ru.ssau.todo.service.TaskService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(@RequestParam(required = false) LocalDateTime from,
                                                  @RequestParam(required = false) LocalDateTime to,
                                                  @RequestParam long userId) {
        if(from == null) from = LocalDateTime.now().minusDays(10000);
        if(to == null) to = LocalDateTime.now().plusDays(1);
        return ResponseEntity.ok(taskService.findAll(from, to, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable long id) {
        try {
            return ResponseEntity.ok(taskService.findById(id));
        }
        catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskDto task) {
        try {
            TaskDto res = taskService.create(task);
            return ResponseEntity.created(URI.create("/tasks/" + res.getId()))
                    .body(res);
        }
        catch (ActiveTaskCountException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", e.getMessage());
            errorResponse.put("stackTrace", e.getStackTrace());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable long id, @RequestBody TaskDto task) {
        task.setId(id);
        try{
            taskService.update(task);
            return ResponseEntity.ok().build();
        }
        catch (NotFoundException nfe) {
            return ResponseEntity.notFound().build();
        } catch (ActiveTaskCountException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", e.getMessage());
            errorResponse.put("stackTrace", e.getStackTrace());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        try{
            taskService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        catch (DeletedTimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", e.getMessage());
            errorResponse.put("stackTrace", e.getStackTrace());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/active/count")
    public ResponseEntity<Long> getActiveTaskCount(@RequestParam long userId) {
        return ResponseEntity.ok(taskService.countActiveTasksByUserId(userId));
    }
}
