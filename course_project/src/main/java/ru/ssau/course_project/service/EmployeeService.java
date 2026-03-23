package ru.ssau.course_project.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.entity.dto.RegistrationDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto update(EmployeeDto dto) throws EntityNotFoundException, DuplicateKeyException;

    void delete(Long id);

    List<EmployeeDto> findAll();

    EmployeeDto findById(Long id) throws EntityNotFoundException;

    EmployeeDto findByUsername(String username) throws EntityNotFoundException;;

    List<EmployeeDto> findAllByFirstNameContainingIgnoreCase(String firstName);

    List<EmployeeDto> findAllByLastNameContainingIgnoreCase(String lastName);

    List<EmployeeDto> findByProjectsId(Long id) throws EntityNotFoundException;

    EmployeeDto updateRoles(Long id, List<String> roles) throws EntityNotFoundException;

    List<EmployeeDto> findEmployeesNotInProject(Long projectId) throws EntityNotFoundException;

}
