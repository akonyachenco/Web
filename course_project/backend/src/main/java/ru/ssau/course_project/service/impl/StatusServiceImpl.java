package ru.ssau.course_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.course_project.entity.Status;
import ru.ssau.course_project.entity.dto.StatusDto;
import ru.ssau.course_project.entity.dto.mapper.StatusMapper;
import ru.ssau.course_project.repository.StatusRepository;
import ru.ssau.course_project.service.StatusService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    @Override
    public StatusDto create(StatusDto dto) throws IllegalArgumentException, DuplicateKeyException {
        if (dto == null) throw new IllegalArgumentException();

        if (statusRepository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateKeyException(String.format("Статус %s уже существует", dto.getName()));
        }

        Status status = statusMapper.toEntity(dto);
        statusRepository.save(status);

        return statusMapper.toDto(status);
    }

    @Override
    public StatusDto update(short id, String name) throws EntityNotFoundException {
        if (statusRepository.findByName(name).isPresent()) {
            throw new DuplicateKeyException(String.format("Статус %s уже существует", name));
        }

        Status status = statusRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Статус с id = " + id + " не найден"));

        status.setName(name);

        return statusMapper.toDto(statusRepository.save(status));
    }

    @Override
    public void delete(short id) {
        statusRepository.deleteById(id);
    }

    @Override
    public List<StatusDto> findAll() {
        return statusRepository.findAll().stream()
                .map(statusMapper::toDto)
                .toList();
    }

}
