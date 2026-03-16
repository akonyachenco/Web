package ru.ssau.course_project.service;

import jakarta.persistence.EntityNotFoundException;
import ru.ssau.course_project.entity.dto.TaskDto;
import ru.ssau.course_project.entity.enums.PriorityEnum;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    TaskDto create(TaskDto dto) throws EntityNotFoundException, IllegalArgumentException;

    TaskDto update( TaskDto dto) throws EntityNotFoundException;

    void delete(Long id);

    List<TaskDto> findAll();

    TaskDto findById(Long id) throws EntityNotFoundException;

    List<TaskDto> findAllByPriority(PriorityEnum priority);

    List<TaskDto> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end) throws DateTimeException;

    List<TaskDto> findAllByCreatedAtBefore(LocalDateTime time);

    List<TaskDto> findAllByCreatedAtAfter(LocalDateTime time);

    List<TaskDto> findAllByStatus_Name(String statusName);

    List<TaskDto> findAllByEmployee_Id(Long id) throws EntityNotFoundException;

    List<TaskDto> findAllByEmployee_IdAndStatus_Name(Long id, String statusName) throws EntityNotFoundException;

    List<TaskDto> findAllBySprint_Id(Long id) throws EntityNotFoundException;

    List<TaskDto> findAllBySprint_IdAndStatus_Name(Long id, String statusName) throws EntityNotFoundException;

    List<TaskDto> findAllByEmployee_IdAndSprint_Id(Long id, Long sprintId) throws EntityNotFoundException;

    List<TaskDto> findAllByEmployeeIdAndProjectId(Long id, Long projectId) throws EntityNotFoundException;

    List<TaskDto> findMyTasks();

    List<TaskDto> findMyTasksByStatus(String statusName);

    List<TaskDto> findMyTasksBySprintId(Long sprintId) throws EntityNotFoundException;

    List<TaskDto> findMyTasksByProjectId(Long projectId) throws EntityNotFoundException;

}
