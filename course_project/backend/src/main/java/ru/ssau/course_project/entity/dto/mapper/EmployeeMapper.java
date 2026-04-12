package ru.ssau.course_project.entity.dto.mapper;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.Role;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.entity.dto.RegistrationDto;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "roles", ignore = true)
    EmployeeDto toDto(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Employee toEntity(RegistrationDto dto);

    @BeforeMapping
    default void beforeMapping(@MappingTarget EmployeeDto target, Employee entity) {
        target.setRoles(
                entity.getRoles().stream()
                        .map(Role::getName)
                        .toList()
        );
    }

}
