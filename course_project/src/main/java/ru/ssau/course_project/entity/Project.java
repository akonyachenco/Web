package ru.ssau.course_project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private Long cost;

    @Column(name = "start_date")
    private LocalDate startDate;

    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
    private List<Employee> team = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<Sprint> sprints = new ArrayList<>();
}
