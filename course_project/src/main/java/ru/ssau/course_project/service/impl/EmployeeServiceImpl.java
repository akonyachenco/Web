package ru.ssau.course_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.Role;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.entity.dto.RegistrationDto;
import ru.ssau.course_project.entity.dto.RoleDto;
import ru.ssau.course_project.entity.dto.mapper.EmployeeMapper;
import ru.ssau.course_project.repository.EmployeeRepository;
import ru.ssau.course_project.repository.ProjectRepository;
import ru.ssau.course_project.repository.RoleRepository;
import ru.ssau.course_project.repository.TaskRepository;
import ru.ssau.course_project.service.EmployeeService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeDto create(RegistrationDto dto) throws IllegalArgumentException {
        if (dto == null) throw new IllegalArgumentException();

        Employee employee = employeeMapper.toEntity(dto);

        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDto update(EmployeeDto dto) throws EntityNotFoundException {
        Employee employee = employeeRepository.findById(dto.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Сотрудник с id = " + dto.getId() + " не найден")
                );

        if (dto.getFirstName() != null)
            employee.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            employee.setLastName(dto.getLastName());
        if (dto.getLogin() != null)
            employee.setLogin(dto.getLogin());
        if (dto.getPosition() != null)
            employee.setPosition(dto.getPosition());

//        if (dto.getRoles() != null) {
//            List<Short> ids = dto.getRoles().stream()
//                    .map(RoleDto::getId)
//                    .toList();
//
//            List<Role> roles = roleRepository.findAllById(ids);
//
//            employee.getRoles().clear();
//            employee.getRoles().addAll(roles);
//        }

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
    public EmployeeDto findByLogin(String login) throws EntityNotFoundException {
        return employeeMapper.toDto(
                employeeRepository.findByLogin(login)
                .orElseThrow(
                        () -> new EntityNotFoundException("Сотрудник с login = " + login + " не найден")
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
    public List<EmployeeDto> findAllByTasksId(Long id) throws EntityNotFoundException {
        taskRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Задача с id = " + id + " не найдена")
                );
        return employeeRepository.findAllByTasksId(id).stream()
                .map(employeeMapper::toDto)
                .toList();
    }
}
