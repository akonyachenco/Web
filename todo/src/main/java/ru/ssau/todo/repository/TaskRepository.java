package ru.ssau.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ssau.todo.entity.Task;

import java.time.LocalDateTime;
import java.util.List;



public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM task WHERE created_by = :userId " +
                    "AND created_at BETWEEN :from and :to")
    List<Task> findAll(@Param("from") LocalDateTime from,
                       @Param("to") LocalDateTime to,
                       @Param("userId") Long userId);


    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId " +
            "AND t.status IN ('OPEN', 'IN_PROGRESS')")
    Long countActiveTasksByUserId(@Param("userId") Long userId);

}
