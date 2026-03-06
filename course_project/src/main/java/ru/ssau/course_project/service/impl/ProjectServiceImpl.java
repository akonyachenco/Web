package ru.ssau.course_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.Project;
import ru.ssau.course_project.entity.Status;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.entity.dto.ProjectDto;
import ru.ssau.course_project.entity.dto.mapper.ProjectMapper;
import ru.ssau.course_project.repository.EmployeeRepository;
import ru.ssau.course_project.repository.ProjectRepository;
import ru.ssau.course_project.repository.StatusRepository;
import ru.ssau.course_project.service.ProjectService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final StatusRepository statusRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ProjectDto create(ProjectDto dto) throws EntityNotFoundException, IllegalArgumentException {
        if(dto == null) throw new IllegalArgumentException();

        Status status = statusRepository.findById(dto.getStatusId()).orElseThrow(
                () -> new EntityNotFoundException("Статус с id = " + dto.getStatusId() + " не найден")
        );

        Project project = projectMapper.toEntity(dto);
        project.setStatus(status);
        projectRepository.save(project);

        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto update(ProjectDto dto) throws EntityNotFoundException {
        Project project = projectRepository.findById(dto.getId()).orElseThrow(
                () -> new EntityNotFoundException("Проект с id = " + dto.getId() + " не найден")
        );
        if (dto.getStatusId() != null && dto.getStatusId() != project.getStatus().getId()) {
            Status status = statusRepository.findById(dto.getStatusId()).orElseThrow(
                    () -> new EntityNotFoundException("Статус с id = " + dto.getStatusId() + " не найден")
            );
            project.setStatus(status);

        }
        if (dto.getName() != null)
            project.setName(dto.getName());
        if (dto.getDescription() != null)
            project.setDescription(dto.getDescription());
        if (dto.getCost() != null)
            project.setCost(dto.getCost());
        if (dto.getStartDate() != null)
            project.setStartDate(dto.getStartDate());
        if (dto.getDeadline() != null)
            project.setDeadline(dto.getDeadline());
        if (dto.getTeam() != null) {
            List<Long> ids = dto.getTeam().stream()
                    .map(EmployeeDto::getId)
                    .toList();

            List<Employee> employees = employeeRepository.findAllById(ids);

            project.getTeam().clear();
            project.getTeam().addAll(employees);
        }

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectDto> findAll() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    public ProjectDto findById(Long id) throws EntityNotFoundException {
        return projectMapper.toDto(
                projectRepository.findById(id).
                        orElseThrow(
                                () -> new EntityNotFoundException("Проект с id = " + id + " не найден"))
        );
    }

    @Override
    public List<ProjectDto> findAllByStatus_NameIgnoreCase(String status) {
        return projectRepository.findAllByStatus_NameIgnoreCase(status).stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    public List<ProjectDto> findAllByStartDateAfter(LocalDate startDate) {
        return projectRepository.findAllByStartDateAfter(startDate).stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    public List<ProjectDto> findAllByEmployeeId(Long id) throws EntityNotFoundException {
        employeeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Сотрудник с id = " + id + " не найден")
        );
        return projectRepository.findAllByEmployeeId(id).stream()
                .map(projectMapper::toDto)
                .toList();
    }
}
