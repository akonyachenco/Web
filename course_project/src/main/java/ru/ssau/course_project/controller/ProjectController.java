package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.ProjectDto;
import ru.ssau.course_project.service.ProjectService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody ProjectDto projectDto) {
        try {
            return ResponseEntity.status(201).body(projectService.create(projectDto));
        }
        catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody ProjectDto projectDto) {
        try {
            return ResponseEntity.ok(projectService.update(projectDto));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(projectService.findById(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllByStatusName(@RequestParam String status) {
        return ResponseEntity.ok(projectService.findAllByStatus_NameIgnoreCase(status));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllByStartDateAfter(@RequestParam LocalDate after) {
        return ResponseEntity.ok(projectService.findAllByStartDateAfter(after));
    }

    @GetMapping("/all/{employeeId}")
    public ResponseEntity<?> findAllByEmployeeId(@PathVariable long employeeId) {
        try {
            return ResponseEntity.ok(projectService.findAllByEmployeeId(employeeId));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
