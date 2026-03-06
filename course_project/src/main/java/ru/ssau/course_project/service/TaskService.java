package ru.ssau.course_project.service;

import jakarta.persistence.EntityNotFoundException;
import ru.ssau.course_project.entity.Task;
import ru.ssau.course_project.entity.dto.TaskDto;
import ru.ssau.course_project.entity.enums.PriorityEnum;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    TaskDto create(TaskDto dto) throws EntityNotFoundException, IllegalArgumentException;

    TaskDto update( TaskDto dto) throws EntityNotFoundException;

    void delete(Long id);

    List<TaskDto> findAll();

    TaskDto findById(Long id) throws EntityNotFoundException;

    List<TaskDto> findAllByPriority(PriorityEnum priority);

    List<TaskDto> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<TaskDto> findAllByCreatedAtBefore(LocalDateTime time);

    List<TaskDto> findAllByCreatedAtAfter(LocalDateTime time);

    List<TaskDto> findAllByStatus_Name(String statusName);

    List<TaskDto> findAllByEmployee_Id(Long id) throws EntityNotFoundException;

    List<TaskDto> findAllByEmployee_IdAndStatus_Name(Long id, String statusName) throws EntityNotFoundException;

    List<TaskDto> findAllBySprint_Id(Long id) throws EntityNotFoundException;

    List<TaskDto> findAllBySprint_IdAndStatus_Name(Long id, String statusName) throws EntityNotFoundException;

    List<TaskDto> findAllByEmployee_IdAndSprint_Id(Long id, Long sprintId) throws EntityNotFoundException;
}
