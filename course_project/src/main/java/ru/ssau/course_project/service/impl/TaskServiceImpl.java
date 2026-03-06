package ru.ssau.course_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.Sprint;
import ru.ssau.course_project.entity.Status;
import ru.ssau.course_project.entity.Task;
import ru.ssau.course_project.entity.dto.TaskDto;
import ru.ssau.course_project.entity.dto.mapper.TaskMapper;
import ru.ssau.course_project.entity.enums.PriorityEnum;
import ru.ssau.course_project.repository.EmployeeRepository;
import ru.ssau.course_project.repository.SprintRepository;
import ru.ssau.course_project.repository.StatusRepository;
import ru.ssau.course_project.repository.TaskRepository;
import ru.ssau.course_project.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final StatusRepository statusRepository;
    private final EmployeeRepository employeeRepository;
    private final SprintRepository sprintRepository;

    @Override
    public TaskDto create(TaskDto dto) throws EntityNotFoundException, IllegalArgumentException {
        if (dto == null) throw new IllegalArgumentException();

        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new EntityNotFoundException("Статус с id = " + dto.getStatusId() + " не найден"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Cотрудник с id = " + dto.getEmployeeId() + " не найден"));


        Sprint sprint = sprintRepository.findById(dto.getSprintId())
                .orElseThrow(() -> new EntityNotFoundException("Спринт с id = " + dto.getSprintId() + " не найден"));


        Task task = taskMapper.toEntity(dto);

        task.setSprint(sprint);
        task.setEmployee(employee);
        task.setStatus(status);
        task.setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);

        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto update(TaskDto dto) throws EntityNotFoundException {
        Task task = taskRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Задача с id = " + dto.getId() + " не найдена"));

        if (dto.getSprintId() != null && dto.getSprintId() != task.getSprint().getId()) {
            Sprint sprint = sprintRepository.findById(dto.getSprintId())
                    .orElseThrow(() -> new EntityNotFoundException("Спринт с id = " + dto.getSprintId() + " не найден"));

            task.setSprint(sprint);
        }

        if (dto.getStatusId() != null && dto.getStatusId() != task.getStatus().getId()) {
            Status status = statusRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new EntityNotFoundException("Статус с id = " + dto.getStatusId() + " не найден"));

            task.setStatus(status);
        }

        if (dto.getEmployeeId() != null && dto.getEmployeeId() != task.getEmployee().getId()) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Сотрудник с id = " + dto.getEmployeeId() + " не найден"));

            task.setEmployee(employee);
        }

        if (dto.getTitle() != null)
            task.setTitle(dto.getTitle());
        if (dto.getDescription() != null)
            task.setDescription(dto.getDescription());
        if (dto.getPriority() != null)
            task.setPriority(PriorityEnum.valueOf(dto.getPriority()));


        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDto> findAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto findById(Long id) throws EntityNotFoundException {
        return taskMapper.toDto(
                taskRepository.findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Задача с id = " + id + " не найдена")
                        )
        );
    }

    @Override
    public List<TaskDto> findAllByPriority(PriorityEnum priority) {
        return taskRepository.findAllByPriority(priority).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return taskRepository.findAllByCreatedAtBetween(start, end).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAllByCreatedAtBefore(LocalDateTime time) {
        return taskRepository.findAllByCreatedAtBefore(time).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAllByCreatedAtAfter(LocalDateTime time) {
        return taskRepository.findAllByCreatedAtAfter(time).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAllByStatus_Name(String statusName) {
        return taskRepository.findAllByStatus_Name(statusName).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAllByEmployee_Id(Long id) throws EntityNotFoundException {
        employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с id = " + id + " не найден"));

        return taskRepository.findAllByEmployee_Id(id).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAllByEmployee_IdAndStatus_Name(Long id, String statusName) throws EntityNotFoundException {
        employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с id = " + id + " не найден"));

        return taskRepository.findAllByEmployee_IdAndStatus_Name(id, statusName).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAllBySprint_Id(Long id) throws EntityNotFoundException {
        sprintRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Спринт с id = " + id + " не найден"));

        return taskRepository.findAllBySprint_Id(id).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAllBySprint_IdAndStatus_Name(Long id, String statusName) throws EntityNotFoundException {
        sprintRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Спринт с id = " + id + " не найден"));

        return taskRepository.findAllBySprint_IdAndStatus_Name(id, statusName).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAllByEmployee_IdAndSprint_Id(Long id, Long sprintId) throws EntityNotFoundException {
        sprintRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Спринт с id = " + sprintId + " не найден"));

        employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с id = " + id + " не найден"));

        return taskRepository.findAllByEmployee_IdAndSprint_Id(id, sprintId).stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
