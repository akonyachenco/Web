package ru.ssau.course_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ssau.course_project.service.ProjectService;

@RestController
@RequiredArgsConstructor
@RequestMapping("my-projects")
public class UserProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> findMyProjects() {
        return ResponseEntity.ok(projectService.findAll());
    }
}
