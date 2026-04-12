package ru.ssau.course_project.entity.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ssau.course_project.entity.Task;
import ru.ssau.course_project.entity.dto.TaskDto;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "statusName", source = "status.name")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "sprintId", source = "sprint.id")
    TaskDto toDto(Task entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "sprint", ignore = true)
    Task toEntity(TaskDto dto);
}
