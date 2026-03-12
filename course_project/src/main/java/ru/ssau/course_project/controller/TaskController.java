package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.TaskDto;
import ru.ssau.course_project.entity.enums.PriorityEnum;
import ru.ssau.course_project.service.TaskService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskDto dto) {
        try {
            return ResponseEntity.status(201).body(taskService.create(dto));
        }
        catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody TaskDto dto) {
        try {
            return ResponseEntity.ok(taskService.update(dto));
        }
        catch (EntityNotFoundException e) {
           return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.findById(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end,
            @RequestParam(required = false) String status) {

        if (priority != null) {
            return ResponseEntity.ok(taskService.findAllByPriority(PriorityEnum.valueOf(priority.toUpperCase())));
        }

        if (start != null && end != null) {
            return ResponseEntity.ok(taskService.findAllByCreatedAtBetween(start, end));
        }
        else if (start != null) {
            return ResponseEntity.ok(taskService.findAllByCreatedAtAfter(start));
        }
        else if (end != null) {
            return ResponseEntity.ok(taskService.findAllByCreatedAtBefore(end));
        }

        if (status != null) {
            return ResponseEntity.ok(taskService.findAllByStatus_Name(status));
        }
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/all/employee/{id}")
    public ResponseEntity<?> findAllByEmployeeId(
            @PathVariable Long id,
            @RequestParam(required = false) String status) {

        if (status != null) {
            return  ResponseEntity.ok(taskService.findAllByEmployee_IdAndStatus_Name(id, status));
        }

        return ResponseEntity.ok(taskService.findAllByEmployee_Id(id));
    }


    @GetMapping("/all/sprint/{id}")
    public ResponseEntity<?> findAllBySprintId(
            @PathVariable long id,
            @RequestParam(required = false) String status) {

        if (status != null) {
            return ResponseEntity.ok(taskService.findAllBySprint_IdAndStatus_Name(id, status));
        }

        return ResponseEntity.ok(taskService.findAllBySprint_Id(id));
    }

    @GetMapping("/all/employee/{employeeId}/sprint/{sprintId}")
    public ResponseEntity<?> findAllByEmployeeIdAndSprintId(
            @PathVariable long employeeId,
            @PathVariable long sprintId) {

        return ResponseEntity.ok(taskService.findAllByEmployee_IdAndSprint_Id(employeeId, sprintId));
    }
}
