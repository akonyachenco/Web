package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.StatusDto;
import ru.ssau.course_project.service.StatusService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/status")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class StatusController {

    private final StatusService statusService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StatusDto dto) {
        try {
            return ResponseEntity.status(201).body(statusService.create(dto));
        }
        catch (IllegalArgumentException | DuplicateKeyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable short id, @RequestBody String name) {
        try {
            return ResponseEntity.ok(statusService.update(id, name));
        }
        catch (DuplicateKeyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable short id) {
        statusService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(statusService.findAll());
    }

}
