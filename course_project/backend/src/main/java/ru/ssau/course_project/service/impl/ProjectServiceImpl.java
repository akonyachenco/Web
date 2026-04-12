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
import ru.ssau.course_project.repository.*;
import ru.ssau.course_project.security.AuthService;
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
    private final SprintRepository sprintRepository;
    private final AuthService authService;
    private final TaskRepository taskRepository;

    @Override
    public ProjectDto create(ProjectDto dto) throws EntityNotFoundException, IllegalArgumentException {
        if(dto == null) throw new IllegalArgumentException();

        if (dto.getStartDate().isAfter(dto.getDeadline()))
            throw new IllegalArgumentException("Дата начала не может быть позже дедлайна");

        Status status = statusRepository.findByName(dto.getStatusName()).orElseThrow(
                () -> new EntityNotFoundException("Статус " + dto.getStatusName() + " не найден")
        );

        Project project = projectMapper.toEntity(dto);
        project.setStatus(status);
        projectRepository.save(project);

        return projectMapper.toDto(project);
    }

    private boolean isStatusActive(String statusName) {
        return !(statusName.equals("Завершено") || statusName.equals("Отменено"));
    }

    private boolean isStatusCanceled(String statusName) {
        return statusName.equals("Отменено");
    }

    @Override
    public ProjectDto update(ProjectDto dto) throws EntityNotFoundException, IllegalArgumentException {
        Project project = projectRepository.findById(dto.getId()).orElseThrow(
                () -> new EntityNotFoundException("Проект с id = " + dto.getId() + " не найден")
        );
        String statusName = dto.getStatusName();
        if (statusName != null && !statusName.equals(project.getStatus().getName())) {
            Status status = statusRepository.findByName(dto.getStatusName()).orElseThrow(
                    () -> new EntityNotFoundException("Статус " + dto.getStatusName() + " не найден")
            );
            project.setStatus(status);
            if (isStatusCanceled(statusName)) {
                project.getSprints().forEach(sprint -> {
                    if (isStatusActive(sprint.getStatus().getName())) {
                        sprint.setStatus(status);
                        sprint.getTasks().forEach(task -> {
                            if (isStatusActive(task.getStatus().getName())) {
                                task.setStatus(status);
                            }
                        });
                    }
                });
            }

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

        if (project.getStartDate().isAfter(project.getDeadline()))
            throw new IllegalArgumentException("Дата начала не может быть позже дедлайна");

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public void delete(Long id) throws IllegalArgumentException {
        Project project = projectRepository.findById(id).get();
        if (!project.getSprints().isEmpty())
            throw new IllegalArgumentException("Ошибка удаления: есть спринты");

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

    public List<ProjectDto> findMyProjects() {
        EmployeeDto employeeDto = authService.getCurrentUser();

        if (employeeDto != null)
            return projectRepository.findAllByEmployeeId(employeeDto.getId()).stream()
                .map(projectMapper::toDto)
                .toList();

        return List.of();
    }

    @Override
    public List<ProjectDto> findMyProjectsByStatusName(String statusName) {
        EmployeeDto employeeDto = authService.getCurrentUser();

        if (employeeDto != null)
            return projectRepository.findAllByEmployeeIdAndStatusName(employeeDto.getId(), statusName).stream()
                    .map(projectMapper::toDto)
                    .toList();

        return List.of();
    }

    @Override
    public ProjectDto findBySprintsId(Long sprintsId) throws EntityNotFoundException {
        sprintRepository.findById(sprintsId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Спринт с id = %d не найден", sprintsId)));

        return projectMapper.toDto(
                projectRepository.findBySprintsId(sprintsId).get()
        );
    }

    @Override
    public void addToTeam(Long projectId, List<Long> employeeIds) throws EntityNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException("Проект с id = " + projectId + " не найден"));

        List<Employee> employees = employeeRepository.findAllById(employeeIds);

        project.getTeam().addAll(employees);

        projectRepository.save(project);
    }

    @Override
    public void removeFromTeam(Long projectId, Long employeeId) throws EntityNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException("Проект с id = " + projectId + " не найден"));

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new EntityNotFoundException("Сотрудник с id = " + employeeId + " не найден"));

        project.getTeam().remove(employee);

    }
}
