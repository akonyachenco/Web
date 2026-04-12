package ru.ssau.course_project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.course_project.entity.Role;
import ru.ssau.course_project.entity.dto.RoleDto;
import ru.ssau.course_project.entity.dto.mapper.RoleMapper;
import ru.ssau.course_project.repository.RoleRepository;
import ru.ssau.course_project.service.RoleService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDto create(RoleDto roleDto) throws IllegalArgumentException {
        if (roleDto == null) throw new IllegalArgumentException();
        Role role = roleMapper.toEntity(roleDto);
        roleRepository.save(role);

        return roleMapper.toDto(role);
    }

    public RoleDto update(short id, String name) throws EntityNotFoundException {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Роль с id = " + id + " не найдена"));
        role.setName(name);

        return roleMapper.toDto(roleRepository.save(role));
    }

    public void delete(short id) {
        roleRepository.deleteById(id);
    }

    public RoleDto findById(short id) throws EntityNotFoundException {
        return roleMapper.toDto(
                roleRepository.findById(id).
                        orElseThrow(
                                () -> new EntityNotFoundException("Роль с id = " + id + " не найдена"))
        );
    }

    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .toList();
    }

    public List<RoleDto> findAllByEmployeeId(long employeeId) throws EntityNotFoundException {
        return roleRepository.findAllByEmployeeId(employeeId).stream()
                .map(roleMapper::toDto)
                .toList();
    }
}
