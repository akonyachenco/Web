package ru.ssau.course_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ssau.course_project.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Short> {

    @Query("SELECT r FROM Role r JOIN r.employees e WHERE e.id = :id")
    List<Role> findAllByEmployeeId(@Param("id") Long id);

    Optional<Role> findByName(String name);
}
