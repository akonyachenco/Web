package ru.ssau.course_project.integration.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.Project;
import ru.ssau.course_project.entity.Role;
import ru.ssau.course_project.entity.Status;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.repository.EmployeeRepository;
import ru.ssau.course_project.repository.ProjectRepository;
import ru.ssau.course_project.repository.RoleRepository;
import ru.ssau.course_project.repository.StatusRepository;
import ru.ssau.course_project.service.impl.EmployeeServiceImpl;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StatusRepository statusRepository;

    @BeforeEach
    void setUp() {
        // Добавление ролей
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);

        //Добавление сотрудников
        Employee employee1 = new Employee();
        employee1.setFirstName("Антон");
        employee1.setLastName("Антонов");
        employee1.setUsername("admin");
        employee1.setPosition("Project Manager");
        employee1.getRoles().add(roleAdmin);
        employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Иван");
        employee2.setLastName("Иванов");
        employee2.setUsername("user");
        employee2.setPosition("Developer");
        employeeRepository.save(employee2);

        //Добавление проекта
        Project project = new Project();
        project.setName("Веб-разработка");
        project.setCost(1000L);
        project.setStartDate(LocalDate.now());
        project.setDeadline(LocalDate.now().plusDays(30));
        project.setTeam(List.of(employee1, employee2));

        Status status = new Status();
        status.setName("В процессе");
        statusRepository.save(status);

        project.setStatus(status);
        projectRepository.save(project);
    }

    @Test
    void update_employeeNotFound_throwsException() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(404L);

        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сотрудник с id = " +employeeDto.getId() + " не найден");
    }

    @Test
    void update_onlyFirstName_updatedEmployee() {
        Employee existingEmployee = employeeRepository.findByUsername("admin").get();

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(existingEmployee.getId());
        employeeDto.setFirstName("Лев");

        EmployeeDto result = employeeService.update(employeeDto);

        assertThat(result.getFirstName()).isEqualTo(employeeDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(existingEmployee.getLastName());
        assertThat(result.getUsername()).isEqualTo(existingEmployee.getUsername());
        assertThat(result.getPosition()).isEqualTo(existingEmployee.getPosition());
        assertThat(result.getRoles()).isEqualTo(List.of(existingEmployee.getRoles().getFirst().getName()));
        assertThat(result.getId()).isEqualTo(existingEmployee.getId());
    }

    @Test
    void update_onlyLastName_updatedEmployee() {
        Employee existingEmployee = employeeRepository.findByUsername("admin").get();

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(existingEmployee.getId());
        employeeDto.setLastName("Зимин");

        EmployeeDto result = employeeService.update(employeeDto);

        assertThat(result.getFirstName()).isEqualTo(existingEmployee.getFirstName());
        assertThat(result.getLastName()).isEqualTo(employeeDto.getLastName());
        assertThat(result.getUsername()).isEqualTo(existingEmployee.getUsername());
        assertThat(result.getPosition()).isEqualTo(existingEmployee.getPosition());
        assertThat(result.getRoles()).isEqualTo(List.of(existingEmployee.getRoles().getFirst().getName()));
        assertThat(result.getId()).isEqualTo(existingEmployee.getId());
    }

    @Test
    void update_onlyPosition_updatedEmployee() {
        Employee existingEmployee = employeeRepository.findByUsername("admin").get();

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(existingEmployee.getId());
        employeeDto.setPosition("PM");

        EmployeeDto result = employeeService.update(employeeDto);

        assertThat(result.getFirstName()).isEqualTo(existingEmployee.getFirstName());
        assertThat(result.getLastName()).isEqualTo(existingEmployee.getLastName());
        assertThat(result.getUsername()).isEqualTo(existingEmployee.getUsername());
        assertThat(result.getPosition()).isEqualTo(employeeDto.getPosition());
        assertThat(result.getRoles()).isEqualTo(List.of(existingEmployee.getRoles().getFirst().getName()));
        assertThat(result.getId()).isEqualTo(existingEmployee.getId());
    }

    @Test
    void update_onlyUsername_updatedEmployee() {
        Employee existingEmployee = employeeRepository.findByUsername("admin").get();

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(existingEmployee.getId());
        employeeDto.setUsername("ADMIN!");

        EmployeeDto result = employeeService.update(employeeDto);

        assertThat(result.getFirstName()).isEqualTo(existingEmployee.getFirstName());
        assertThat(result.getLastName()).isEqualTo(existingEmployee.getLastName());
        assertThat(result.getUsername()).isEqualTo(employeeDto.getUsername());
        assertThat(result.getPosition()).isEqualTo(existingEmployee.getPosition());
        assertThat(result.getRoles()).isEqualTo(List.of(existingEmployee.getRoles().getFirst().getName()));
        assertThat(result.getId()).isEqualTo(existingEmployee.getId());
    }

    @Test
    void update_existingUsername_throwsException() {
        Employee existingEmployee = employeeRepository.findByUsername("admin").get();

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(existingEmployee.getId());
        employeeDto.setUsername("user");

        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("Имя пользователя занято");
    }

    @Test
    void updateRoles_employeeNotFound_throwsException() {
        Long id = 404L;

        List<String> roles = List.of("ROLE_USER", "ROLE_USER");

        assertThatThrownBy(() -> employeeService.updateRoles(id, roles))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сотрудник с id = " + id + " не найден");
    }

    @Test
    void updateRoles_validData_updatedEmployee() {
        Employee existingEmployee = employeeRepository.findByUsername("user").get();

        Long id = existingEmployee.getId();

        List<String> roles = List.of("ROLE_USER", "ROLE_USER");

        EmployeeDto result = employeeService.updateRoles(id, roles);

        assertThat(result.getFirstName()).isEqualTo(existingEmployee.getFirstName());
        assertThat(result.getLastName()).isEqualTo(existingEmployee.getLastName());
        assertThat(result.getUsername()).isEqualTo(existingEmployee.getUsername());
        assertThat(result.getPosition()).isEqualTo(existingEmployee.getPosition());
        assertThat(result.getRoles()).hasSize(1);
        assertThat(result.getRoles()).contains("ROLE_USER");
    }

    @Test
    void updateRoles_withNonExistenRole_updatedEmployee() {
        Employee existingEmployee = employeeRepository.findByUsername("user").get();

        Long id = existingEmployee.getId();

        List<String> roles = List.of("NON-EXISTEN");

        EmployeeDto result = employeeService.updateRoles(id, roles);

        assertThat(result.getFirstName()).isEqualTo(existingEmployee.getFirstName());
        assertThat(result.getLastName()).isEqualTo(existingEmployee.getLastName());
        assertThat(result.getUsername()).isEqualTo(existingEmployee.getUsername());
        assertThat(result.getPosition()).isEqualTo(existingEmployee.getPosition());
        assertThat(result.getRoles()).isEmpty();
    }


    @Test
    void findByProjectsId_projectNotFound_throwException() {
        Long id = 404L;

        assertThatThrownBy(() -> employeeService.findByProjectsId(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Проект с id = " + id + " не найден");
    }

    @Test
    void findByProjectsId_validData_employeeDtoList() {
        Project project = projectRepository.findAll().getFirst();

        List<EmployeeDto> result = employeeService.findByProjectsId(project.getId());

        List<EmployeeDto> expected = employeeService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expected);
    }
}