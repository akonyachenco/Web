package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.SprintDto;
import ru.ssau.course_project.service.SprintService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sprint")
public class SprintController {

    private final SprintService sprintService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SprintDto sprintDto) {
        try {
            return ResponseEntity.status(201).body(sprintService.create(sprintDto));
        }
        catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody SprintDto sprintDto) {
        try {
            return ResponseEntity.ok(sprintService.update(sprintDto));
        }
        catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            sprintService.delete(id);
            return ResponseEntity.noContent().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(sprintService.findById(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(sprintService.findAll());
    }

    @GetMapping("/all/project/{id}")
    public ResponseEntity<?> findAllByProjectId(@PathVariable long id) {
        try {
            return ResponseEntity.ok(sprintService.findAllByProject_Id(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
