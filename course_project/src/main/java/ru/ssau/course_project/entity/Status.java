package ru.ssau.course_project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    private String name;

    @OneToMany(mappedBy = "status")
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "status")
    private List<Sprint> sprints = new ArrayList<>();

    @OneToMany(mappedBy = "status")
    private List<Task> tasks = new ArrayList<>();
}
