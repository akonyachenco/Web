package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.service.EmployeeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PutMapping
    public ResponseEntity<?> update(@RequestBody EmployeeDto employeeDto) {
        try {
            return ResponseEntity.ok(employeeService.update(employeeDto));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (DuplicateKeyException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/set-roles")
    public ResponseEntity<?> setRoles(@PathVariable Long id, @RequestBody List<String> roles) {
        try {
            return ResponseEntity.ok(employeeService.updateRoles(id, roles));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(employeeService.findById(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> findByUsername(@RequestParam String username) {
        try {
            return ResponseEntity.ok(employeeService.findByUsername(username));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all/by-firstname")
    public ResponseEntity<?> findByFirstNameContaining(@RequestParam String firstName) {
        return ResponseEntity.ok(employeeService.findAllByFirstNameContainingIgnoreCase(firstName));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all/by-lastname")
    public ResponseEntity<?> findByLastNameContaining(@RequestParam String lastName) {
        return ResponseEntity.ok(employeeService.findAllByLastNameContainingIgnoreCase(lastName));
    }

    @GetMapping("/all/project/{id}")
    public ResponseEntity<?> findByProjectId(@PathVariable long id) {
        try {
            return ResponseEntity.ok(employeeService.findByProjectsId(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all/not-in-project/{id}")
    public ResponseEntity<?> findEmployeesNotInProject(@PathVariable long id) {
        try {
            return ResponseEntity.ok(employeeService.findEmployeesNotInProject(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
