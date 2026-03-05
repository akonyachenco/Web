package ru.ssau.course_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ssau.course_project.entity.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Short> {

    @Query("SELECT r FROM Role r JOIN r.employees e WHERE e.id = :id")
    List<Role> findAllByEmployeeId(Long id);
}
