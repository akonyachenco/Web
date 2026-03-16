package ru.ssau.course_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ssau.course_project.entity.Task;
import ru.ssau.course_project.entity.enums.PriorityEnum;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByPriority(PriorityEnum priority);

    List<Task> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Task> findAllByCreatedAtBefore(LocalDateTime time);

    List<Task> findAllByCreatedAtAfter(LocalDateTime time);

    List<Task> findAllByStatus_Name(String statusName);

    List<Task> findAllByEmployee_Id(Long id);

    List<Task> findAllByEmployee_IdAndStatus_Name(Long id, String statusName);

    List<Task> findAllBySprint_Id(Long id);

    List<Task> findAllBySprint_IdAndStatus_Name(Long id, String statusName);

    List<Task> findAllByEmployee_IdAndSprint_Id(Long id, Long sprintId);

    List<Task> findAllByEmployee_IdAndSprint_Project_Id(Long id, Long sprintProjectId);
}
