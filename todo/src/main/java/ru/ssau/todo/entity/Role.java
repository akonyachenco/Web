package ru.ssau.todo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new LinkedList<>();
}
