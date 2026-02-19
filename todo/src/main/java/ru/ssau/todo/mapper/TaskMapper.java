package ru.ssau.todo.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TaskMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setStatus(TaskStatus.valueOf(rs.getString("status")));
        task.setCreatedBy(rs.getLong("created_by"));
        task.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        return task;
    }
}
