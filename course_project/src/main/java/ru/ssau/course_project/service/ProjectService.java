package ru.ssau.course_project.service;

import jakarta.persistence.EntityNotFoundException;


import ru.ssau.course_project.entity.dto.ProjectDto;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    ProjectDto create(ProjectDto dto) throws EntityNotFoundException, IllegalArgumentException;

    ProjectDto update(ProjectDto dto) throws EntityNotFoundException;

    void delete(Long id);

    List<ProjectDto> findAll();

    ProjectDto findById(Long id) throws EntityNotFoundException;

    List<ProjectDto> findAllByStatus_NameIgnoreCase(String status);

    List<ProjectDto> findAllByStartDateAfter(LocalDate startDate);

    List<ProjectDto> findAllByEmployeeId(Long id) throws EntityNotFoundException;

    List<ProjectDto> findMyProjects();

    void addToTeam(Long projectId, List<Long> employeeIds) throws EntityNotFoundException;
    void removeFromTeam(Long projectId, Long employeeId) throws EntityNotFoundException;

}
