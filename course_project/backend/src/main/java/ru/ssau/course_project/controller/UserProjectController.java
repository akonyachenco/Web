package ru.ssau.course_project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.course_project.service.ProjectService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-projects")
public class UserProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> findMyProjects(@RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(projectService.findMyProjectsByStatusName(status));
        }
        else {
            return ResponseEntity.ok(projectService.findMyProjects());
        }
    }

    @GetMapping("/by-sprint-id/{id}")
    public ResponseEntity<?> findBySprintsId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(projectService.findBySprintsId(id));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
