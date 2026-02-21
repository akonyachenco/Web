package ru.ssau.todo.repository.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.ssau.todo.entity.Task;
import ru.ssau.todo.entity.TaskStatus;
import ru.ssau.todo.exception.BusinessLogicException;
import ru.ssau.todo.exception.NotFoundException;
import ru.ssau.todo.mapper.TaskMapper;
import ru.ssau.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class TaskJdbcRepository  implements TaskRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TaskMapper taskMapper;

    TaskJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.taskMapper = new TaskMapper();
    }

    @Override
    public Task create(Task task) throws BusinessLogicException {
        if(task == null) throw new IllegalArgumentException("Task cannot be null");

        String sql = "INSERT INTO task (title, status, created_by, created_at)" +
                "VALUES (:title, :status, :createdBy, CURRENT_TIMESTAMP)" +
                "RETURNING id, title, status, created_by, created_at;";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", task.getTitle())
                .addValue("status", task.getStatus().name())
                .addValue("createdBy", task.getCreatedBy());
        return namedParameterJdbcTemplate.queryForObject(sql, params, taskMapper);
    }

    @Override
    public Optional<Task> findById(long id) {
        String sql = "SELECT * FROM task WHERE id = :id;";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        try{
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql,params, taskMapper));
        }
        catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Task> findAll(LocalDateTime from, LocalDateTime to, long userId) {
        String sql = "SELECT * FROM task WHERE created_by = :userId AND created_at BETWEEN :from and :to;";
        SqlParameterSource params = new MapSqlParameterSource("userId", userId)
                .addValue("from", from)
                .addValue("to", to);
        return namedParameterJdbcTemplate.query(sql, params, taskMapper);
    }

    @Override
    public void update(Task task) throws NotFoundException, BusinessLogicException {
        String sql = "UPDATE task SET title = :title, status = :status WHERE id = :id;";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", task.getTitle())
                .addValue("status", task.getStatus().name())
                .addValue("id", task.getId());
        if (namedParameterJdbcTemplate.update(sql, params) == 0) {
            throw new NotFoundException("Task with id " + task.getId() + " not found");
        }
    }

    @Override
    public void deleteById(long id) throws BusinessLogicException {
        String sql = "DELETE FROM task WHERE id = :id;";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public long countActiveTasksByUserId(long userId) {
        String sql = "SELECT COUNT(*) FROM task WHERE created_by = :userId AND status IN (:status1, :status2);";
        SqlParameterSource params = new MapSqlParameterSource("userId", userId)
                .addValue("status1", TaskStatus.OPEN.name())
                .addValue("status2", TaskStatus.IN_PROGRESS.name());
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }
}
