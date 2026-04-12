package ru.ssau.course_project.entity.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ssau.course_project.entity.Project;
import ru.ssau.course_project.entity.dto.ProjectDto;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "statusName", source = "status.name")
    ProjectDto toDto(Project entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "sprints", ignore = true)
    Project toEntity(ProjectDto dto);
}
