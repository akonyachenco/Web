package ru.ssau.course_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.course_project.entity.Project;
import ru.ssau.course_project.entity.Sprint;
import ru.ssau.course_project.entity.Status;
import ru.ssau.course_project.entity.dto.SprintDto;
import ru.ssau.course_project.entity.dto.mapper.SprintMapper;
import ru.ssau.course_project.repository.ProjectRepository;
import ru.ssau.course_project.repository.SprintRepository;
import ru.ssau.course_project.repository.StatusRepository;
import ru.ssau.course_project.service.SprintService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
    private final SprintMapper sprintMapper;
    private final StatusRepository statusRepository;
    private final ProjectRepository projectRepository;

    @Override
    public SprintDto create(SprintDto dto) throws EntityNotFoundException, IllegalArgumentException {
        if (dto == null) throw new IllegalArgumentException();
        if (dto.getStartDate().isAfter(dto.getEndDate()))
            throw new IllegalArgumentException("Дата начала не может быть позже даты завершения");

        Status status = statusRepository.findByName(dto.getStatusName())
                .orElseThrow(() -> new EntityNotFoundException("Статус " + dto.getStatusName() + " не найден"));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Проект с id = " + dto.getProjectId() + " не найден"));

        Sprint sprint = sprintMapper.toEntity(dto);
        sprint.setProject(project);
        sprint.setStatus(status);

        sprintRepository.save(sprint);

        return sprintMapper.toDto(sprint);
    }

    private boolean isStatusActive(String statusName) {
        return !(statusName.equals("Завершено") || statusName.equals("Отменено"));
    }

    private boolean isStatusCanceled(String statusName) {
        return statusName.equals("Отменено");
    }

    @Override
    public SprintDto update(SprintDto dto) throws EntityNotFoundException, IllegalArgumentException {
        Sprint sprint = sprintRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Спринт с id = " + dto.getId() + " не найден"));

        String statusName = dto.getStatusName();
        if (dto.getStatusName() != null && !dto.getStatusName().equals(sprint.getStatus().getName())) {
            Status status = statusRepository.findByName(dto.getStatusName()).orElseThrow(
                    () -> new EntityNotFoundException("Статус " + dto.getStatusName() + " не найден")
            );
            sprint.setStatus(status);
            if (isStatusCanceled(statusName)) {
                sprint.getTasks().forEach(task -> {
                    if (isStatusActive(task.getStatus().getName())) {
                        task.setStatus(status);
                    }
                });
            }
        }

        if (dto.getProjectId() != null && dto.getProjectId() != sprint.getProject().getId()) {
            Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(
                    () -> new EntityNotFoundException("Проект с id = " + dto.getProjectId() + " не найден")
            );
            sprint.setProject(project);
        }

        if (dto.getName() != null)
            sprint.setName(dto.getName());
        if (dto.getGoal() != null)
            sprint.setGoal(dto.getGoal());
        if (dto.getStartDate() != null)
            sprint.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null)
            sprint.setEndDate(dto.getEndDate());

        if (sprint.getStartDate().isAfter(sprint.getEndDate()))
            throw new IllegalArgumentException("Дата начала не может быть позже даты завершения");


        return sprintMapper.toDto(sprintRepository.save(sprint));
    }

    @Override
    public void delete(Long id) throws IllegalArgumentException {
        Sprint sprint = sprintRepository.findById(id).get();
        if (!sprint.getTasks().isEmpty())
            throw new IllegalArgumentException("Ошибка удаления: есть задачи");
        
        sprintRepository.deleteById(id);
    }

    @Override
    public List<SprintDto> findAll() {
        return sprintRepository.findAll().stream()
                .map(sprintMapper::toDto)
                .toList();
    }

    @Override
    public SprintDto findById(Long id) throws EntityNotFoundException {
        return sprintMapper.toDto(
                sprintRepository.findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Спринт с id = " + id + " не найден")
                        )
        );
    }

    @Override
    public List<SprintDto> findAllByProject_Id(Long id) throws EntityNotFoundException {
        projectRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Проект с id = " + id + " не найден")
                );
        return sprintRepository.findAllByProject_Id(id).stream()
                .map(sprintMapper::toDto)
                .toList();
    }
}
