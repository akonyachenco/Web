package ru.ssau.course_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ssau.course_project.entity.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Short> {
}
