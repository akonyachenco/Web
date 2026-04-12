package ru.ssau.course_project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees = new ArrayList<>();
}
