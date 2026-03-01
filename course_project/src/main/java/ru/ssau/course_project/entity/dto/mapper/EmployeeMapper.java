package ru.ssau.course_project.entity.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.entity.dto.RegistrationDto;

@Mapper(componentModel = "spring",
        uses = {RoleMapper.class,
                ProjectMapper.class,
                TaskMapper.class})
public interface EmployeeMapper {

    EmployeeDto toDto(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Employee toEntity(RegistrationDto dto);
}
