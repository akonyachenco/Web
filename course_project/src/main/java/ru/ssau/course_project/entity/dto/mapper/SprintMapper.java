package ru.ssau.course_project.entity.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ssau.course_project.entity.Sprint;
import ru.ssau.course_project.entity.dto.SprintDto;

@Mapper(componentModel = "spring")
public interface SprintMapper {

    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "projectId", source = "project.id")
    SprintDto toDto(Sprint entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Sprint toEntity(SprintDto dto);
}
