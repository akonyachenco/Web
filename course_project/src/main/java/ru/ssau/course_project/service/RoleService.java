package ru.ssau.course_project.service;

import jakarta.persistence.EntityNotFoundException;
import ru.ssau.course_project.entity.Role;
import ru.ssau.course_project.entity.dto.RoleDto;

import java.util.List;

public interface RoleService {

    public RoleDto create(RoleDto roleDto) throws IllegalArgumentException;

    public RoleDto update(short id, String name) throws EntityNotFoundException;

    public void delete(short id);

    public RoleDto findById(short id) throws EntityNotFoundException;

    public List<RoleDto> findAll();

    public List<RoleDto> findAllByEmployeeId(long employeeId) throws EntityNotFoundException;
}
