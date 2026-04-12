package ru.ssau.course_project.service;

import jakarta.persistence.EntityNotFoundException;
import ru.ssau.course_project.entity.dto.SprintDto;

import java.util.List;

public interface SprintService {

    SprintDto create( SprintDto dto) throws EntityNotFoundException, IllegalArgumentException;

    SprintDto update( SprintDto dto) throws EntityNotFoundException, IllegalArgumentException;

    void delete(Long id) throws IllegalArgumentException;

    List<SprintDto> findAll();

    SprintDto findById(Long id) throws EntityNotFoundException;

    List<SprintDto> findAllByProject_Id(Long id) throws EntityNotFoundException;

}
