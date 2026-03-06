package ru.ssau.course_project.service;

import jakarta.persistence.EntityNotFoundException;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.entity.dto.RegistrationDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto create(RegistrationDto dto) throws IllegalArgumentException;

    EmployeeDto update(EmployeeDto dto) throws EntityNotFoundException;

    void delete(Long id);

    List<EmployeeDto> findAll();

    EmployeeDto findById(Long id) throws EntityNotFoundException;

    EmployeeDto findByLogin(String login) throws EntityNotFoundException;;

    List<EmployeeDto> findAllByFirstNameContainingIgnoreCase(String firstName);

    List<EmployeeDto> findAllByLastNameContainingIgnoreCase(String lastName);

    List<EmployeeDto> findByProjectsId(Long id) throws EntityNotFoundException;;

    List<EmployeeDto> findAllByTasksId(Long id) throws EntityNotFoundException;;
}
