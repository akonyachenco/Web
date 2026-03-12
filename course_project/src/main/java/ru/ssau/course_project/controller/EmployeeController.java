package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.entity.dto.RegistrationDto;
import ru.ssau.course_project.service.EmployeeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RegistrationDto registrationDto) {
        try {
            return ResponseEntity.status(201).body(employeeService.create(registrationDto));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody EmployeeDto employeeDto) {
        try {
            return ResponseEntity.ok(employeeService.update(employeeDto));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(employeeService.findById(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping
    public ResponseEntity<?> findByLogin(@RequestParam String login) {
        try {
            return ResponseEntity.ok(employeeService.findByLogin(login));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findByFirstNameContaining(@RequestParam String firstName) {
        return ResponseEntity.ok(employeeService.findAllByFirstNameContainingIgnoreCase(firstName));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findByLastNameContaining(@RequestParam String lastName) {
        return ResponseEntity.ok(employeeService.findAllByLastNameContainingIgnoreCase(lastName));
    }

    @GetMapping("/all/project/{id}")
    public ResponseEntity<?> findByProjectId(@PathVariable long id) {
        return ResponseEntity.ok(employeeService.findByProjectsId(id));
    }
}
