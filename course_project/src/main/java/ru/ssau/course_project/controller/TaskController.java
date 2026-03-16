package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.TaskDto;
import ru.ssau.course_project.entity.enums.PriorityEnum;
import ru.ssau.course_project.service.TaskService;

import java.time.DateTimeException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@PreAuthorize("hasRole('ROLE_ADMIN')")
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
            try {
                return ResponseEntity.ok(taskService.findAllByCreatedAtBetween(start, end));
            }
            catch (DateTimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
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

        try {
            if (status != null) {
                return  ResponseEntity.ok(taskService.findAllByEmployee_IdAndStatus_Name(id, status));
            }

            return ResponseEntity.ok(taskService.findAllByEmployee_Id(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }


    @GetMapping("/all/sprint/{id}")
    public ResponseEntity<?> findAllBySprintId(
            @PathVariable long id,
            @RequestParam(required = false) String status) {

        try {
            if (status != null) {
                return ResponseEntity.ok(taskService.findAllBySprint_IdAndStatus_Name(id, status));
            }

            return ResponseEntity.ok(taskService.findAllBySprint_Id(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    @GetMapping("/all/employee/{employeeId}/sprint/{sprintId}")
    public ResponseEntity<?> findAllByEmployeeIdAndSprintId(
            @PathVariable long employeeId,
            @PathVariable long sprintId) {
        try {
            return ResponseEntity.ok(taskService.findAllByEmployee_IdAndSprint_Id(employeeId, sprintId));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/all/employee/{employeeId}/project/{projectId}")
    public ResponseEntity<?> findAllByEmployeeIdAndProjectId(
            @PathVariable long employeeId,
            @PathVariable long projectId) {

        try {
            return ResponseEntity.ok(taskService.findAllByEmployeeIdAndProjectId(employeeId, projectId));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }
}
