package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.RoleDto;
import ru.ssau.course_project.service.RoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoleDto dto) {
        try {
            return ResponseEntity.status(201).body(roleService.create(dto));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable short id, @RequestParam String name) {
        try {
            return ResponseEntity.ok(roleService.update(id, name));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable short id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable short id) {
        try {
            return ResponseEntity.ok(roleService.findById(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/all/employee/{employeeId}")
    public ResponseEntity<?> findAllByEmployeeId(@PathVariable long employeeId) {
        try {
            return ResponseEntity.ok(roleService.findAllByEmployeeId(employeeId));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
