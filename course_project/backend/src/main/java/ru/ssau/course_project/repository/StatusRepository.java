package ru.ssau.course_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ssau.course_project.entity.Status;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Short> {
    Optional<Status> findByName(String name);
}
