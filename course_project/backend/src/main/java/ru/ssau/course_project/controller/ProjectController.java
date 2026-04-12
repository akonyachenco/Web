package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.ProjectDto;
import ru.ssau.course_project.service.ProjectService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
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
        catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            projectService.delete(id);
            return ResponseEntity.noContent().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

    @GetMapping("/all/by-status")
    public ResponseEntity<?> findAllByStatusName(@RequestParam String status) {
        return ResponseEntity.ok(projectService.findAllByStatus_NameIgnoreCase(status));
    }

    @GetMapping("/all/by-start-date")
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

    @PostMapping("/{id}/add-to-team")
    public ResponseEntity<?> addToTeam(@PathVariable long id, @RequestBody List<Long> employeeIds) {
        try {
            projectService.addToTeam(id, employeeIds);
            return ResponseEntity.ok().build();
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/delete-from-team/{employeeId}")
    public ResponseEntity<?> deleteFromTeam(@PathVariable long id, @PathVariable long employeeId) {
        try {
            projectService.removeFromTeam(id, employeeId);
            return ResponseEntity.ok().build();
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
