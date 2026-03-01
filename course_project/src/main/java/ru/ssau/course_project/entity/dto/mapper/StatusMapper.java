package ru.ssau.course_project.entity.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ssau.course_project.entity.Status;
import ru.ssau.course_project.entity.dto.StatusDto;

@Mapper(componentModel = "spring",
        uses = {ProjectMapper.class,
                SprintMapper.class,
                TaskMapper.class})
public interface StatusMapper {

    StatusDto toDto(Status entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "sprints", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Status toEntity(StatusDto dto);
}
