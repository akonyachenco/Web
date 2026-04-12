package ru.ssau.course_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.Project;
import ru.ssau.course_project.entity.Role;
import ru.ssau.course_project.entity.dto.EmployeeDto;

import ru.ssau.course_project.entity.dto.mapper.EmployeeMapper;
import ru.ssau.course_project.repository.EmployeeRepository;
import ru.ssau.course_project.repository.ProjectRepository;
import ru.ssau.course_project.repository.RoleRepository;
import ru.ssau.course_project.service.EmployeeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeMapper employeeMapper;



    @Override
    public EmployeeDto update(EmployeeDto dto) throws EntityNotFoundException, DuplicateKeyException {
        Employee employee = employeeRepository.findById(dto.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Сотрудник с id = " + dto.getId() + " не найден"));

        if (dto.getFirstName() != null)
            employee.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            employee.setLastName(dto.getLastName());

        if (dto.getUsername() != null) {
            if (employeeRepository.findByUsername(dto.getUsername()).isPresent()) {
                throw new DuplicateKeyException("Имя пользователя занято");
            }
            employee.setUsername(dto.getUsername());
        }
        if (dto.getPosition() != null)
            employee.setPosition(dto.getPosition());


        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDto updateRoles(Long id, List<String> roles) throws EntityNotFoundException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Сотрудник с id = " + id + " не найден"));

        List<Role> roleList = roles.stream()
                .distinct()
                .map(roleRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());;

        employee.setRoles(roleList);

        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Override
    public EmployeeDto findById(Long id) throws EntityNotFoundException {
        return employeeMapper.toDto(
                employeeRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Сотрудник с id = " + id + " не найден")
                ));
    }

    @Override
    public EmployeeDto findByUsername(String username) throws EntityNotFoundException {
        return employeeMapper.toDto(
                employeeRepository.findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException("Сотрудник " + username + " не найден")
                )
        );
    }

    @Override
    public List<EmployeeDto> findAllByFirstNameContainingIgnoreCase(String firstName) {
        return employeeRepository.findAllByFirstNameContainingIgnoreCase(firstName).stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Override
    public List<EmployeeDto> findAllByLastNameContainingIgnoreCase(String lastName) {
        return employeeRepository.findAllByLastNameContainingIgnoreCase(lastName).stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Override
    public List<EmployeeDto> findByProjectsId(Long id) throws EntityNotFoundException {
        projectRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Проект с id = " + id + " не найден")
                );
        return employeeRepository.findByProjectsId(id).stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Override
    public List<EmployeeDto> findEmployeesNotInProject(Long projectId) throws EntityNotFoundException {
        projectRepository.findById(projectId).orElseThrow(
                () -> new EntityNotFoundException("Проект с id = " + projectId + " не найден"));

        return employeeRepository.findEmployeesNotInProject(projectId).stream()
                .map(employeeMapper::toDto)
                .toList();
    }

}
