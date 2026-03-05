package ru.ssau.course_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ssau.course_project.entity.Project;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByStatus_NameIgnoreCase(String status);

    List<Project> findAllByStartDateAfter(LocalDate startDate);

    @Query("SELECT p FROM Project p JOIN p.team e WHERE e.id = :id")
    List<Project> findAllByEmployeeId(Long id);
}
