package ru.ssau.course_project.service;

import jakarta.persistence.EntityNotFoundException;
import ru.ssau.course_project.entity.dto.StatusDto;

import java.util.List;

public interface StatusService {

    StatusDto create(StatusDto dto) throws IllegalArgumentException;

    StatusDto update(short id, String name) throws EntityNotFoundException;

    void delete(short id);

    List<StatusDto> findAll();
}
