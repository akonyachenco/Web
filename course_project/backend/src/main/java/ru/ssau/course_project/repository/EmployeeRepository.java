package ru.ssau.course_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ssau.course_project.entity.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsername(String username);

    List<Employee> findAllByFirstNameContainingIgnoreCase(String firstName);

    List<Employee> findAllByLastNameContainingIgnoreCase(String lastName);

    List<Employee> findByProjectsId(Long id);

    @Query("SELECT e FROM Employee e WHERE e.id NOT IN " +
            "(SELECT emp.id FROM Project p JOIN p.team emp WHERE p.id = :projectId)")
    List<Employee> findEmployeesNotInProject(Long projectId);



}
