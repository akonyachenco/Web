package ru.ssau.course_project.entity.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ssau.course_project.entity.Role;
import ru.ssau.course_project.entity.dto.RoleDto;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Role toEntity(RoleDto dto);
}
