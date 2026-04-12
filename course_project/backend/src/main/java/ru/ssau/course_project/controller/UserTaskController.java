package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-tasks")
public class UserTaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<?> findMyTasks(
            @RequestParam(required = false) String status) {

        try {
            if (status != null) {
                return  ResponseEntity.ok(taskService.findMyTasksByStatus(status));
            }

            return ResponseEntity.ok(taskService.findMyTasks());
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }


    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<?> findMyTasksBySprintId(
            @PathVariable long sprintId) {
        try {
            return ResponseEntity.ok(taskService.findMyTasksBySprintId(sprintId));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> findMyTasksByProjectId(
            @PathVariable long projectId) {

        try {
            return ResponseEntity.ok(taskService.findMyTasksByProjectId(projectId));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/change-status")
    public ResponseEntity<?> changeStatus(@PathVariable long id, @RequestBody String statusName) {
        try {
            taskService.changeStatus(id, statusName);
            return ResponseEntity.ok("Статус изменен");
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
