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

        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new EntityNotFoundException("Статус с id = " + dto.getStatusId() + " не найден"));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Проект с id = " + dto.getProjectId() + " не найден"));

        Sprint sprint = sprintMapper.toEntity(dto);
        sprint.setProject(project);
        sprint.setStatus(status);

        sprintRepository.save(sprint);

        return sprintMapper.toDto(sprint);
    }

    @Override
    public SprintDto update(SprintDto dto) throws EntityNotFoundException {
        Sprint sprint = sprintRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Спринт с id = " + dto.getId() + " не найден"));

        if (dto.getStatusId() != null && dto.getStatusId() != sprint.getStatus().getId()) {
            Status status = statusRepository.findById(dto.getStatusId()).orElseThrow(
                    () -> new EntityNotFoundException("Статус с id = " + dto.getStatusId() + " не найден")
            );
            sprint.setStatus(status);
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

        return sprintMapper.toDto(sprintRepository.save(sprint));
    }

    @Override
    public void delete(Long id) {
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
