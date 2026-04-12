package ru.ssau.course_project.unit.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import ru.ssau.course_project.entity.Employee;
import ru.ssau.course_project.entity.Project;
import ru.ssau.course_project.entity.Role;
import ru.ssau.course_project.entity.dto.EmployeeDto;
import ru.ssau.course_project.entity.dto.mapper.EmployeeMapper;
import ru.ssau.course_project.repository.EmployeeRepository;
import ru.ssau.course_project.repository.ProjectRepository;
import ru.ssau.course_project.repository.RoleRepository;
import ru.ssau.course_project.service.impl.EmployeeServiceImpl;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;
    private Employee admin;
    private EmployeeDto adminDto;


    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setUsername("Test");
        employee.setFirstName("Kevin");
        employee.setLastName("Benson");
        employee.setPosition("Developer");

        admin = new Employee();
        admin.setId(2L);
        admin.setUsername("Admin");
        admin.setFirstName("Kirill");
        admin.setLastName("Ivanov");
        admin.setPosition("Project Manager");

        employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setUsername("Test");
        employeeDto.setFirstName("Kevin");
        employeeDto.setLastName("Benson");
        employeeDto.setPosition("Developer");

        adminDto = new EmployeeDto();
        adminDto.setId(2L);
        adminDto.setUsername("Admin");
        adminDto.setFirstName("Kirill");
        adminDto.setLastName("Ivanov");
        adminDto.setPosition("Project Manager");
    }

    @Test
    void update_employeeNotFound_throwException() {
        employeeDto.setId(512L);
        when(employeeRepository.findById(512L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.update(employeeDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сотрудник с id = 512 не найден");

    }

    @Test
    void update_onlyFirstName_updatedEmployee() {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);
        dto.setFirstName("Harry");

        employee.setFirstName("Harry");
        employeeDto.setFirstName("Harry");

        when(employeeRepository.findById(dto.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto expected = new EmployeeDto();
        expected.setId(1L);
        expected.setFirstName("Harry");
        expected.setLastName("Benson");
        expected.setPosition("Developer");
        expected.setUsername("Test");

        EmployeeDto result = employeeService.update(dto);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void update_onlyLastName_updatedEmployee() {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);
        dto.setLastName("Dark");

        employee.setLastName("Dark");
        employeeDto.setLastName("Dark");

        when(employeeRepository.findById(dto.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto expected = new EmployeeDto();
        expected.setId(1L);
        expected.setFirstName("Kevin");
        expected.setLastName("Dark");
        expected.setPosition("Developer");
        expected.setUsername("Test");

        EmployeeDto result = employeeService.update(dto);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void update_onlyUsername_updatedEmployee() {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);
        dto.setUsername("Admin");

        employee.setUsername("Admin");
        employeeDto.setUsername("Admin");

        when(employeeRepository.findById(dto.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto expected = new EmployeeDto();
        expected.setId(1L);
        expected.setFirstName("Kevin");
        expected.setLastName("Benson");
        expected.setPosition("Developer");
        expected.setUsername("Admin");

        EmployeeDto result = employeeService.update(dto);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void update_onlyUsername_throwException() {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);
        dto.setUsername("Admin");

        when(employeeRepository.findById(dto.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.findByUsername("Admin")).thenReturn(Optional.of(admin));

        assertThatThrownBy(() -> employeeService.update(dto))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("Имя пользователя занято");
    }

    @Test
    void update_onlyPosition_updatedEmployee() {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);
        dto.setPosition("QA engineer");

        employee.setPosition("QA engineer");
        employeeDto.setPosition("QA engineer");

        when(employeeRepository.findById(dto.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto expected = new EmployeeDto();
        expected.setId(1L);
        expected.setFirstName("Kevin");
        expected.setLastName("Benson");
        expected.setPosition("QA engineer");
        expected.setUsername("Test");

        EmployeeDto result = employeeService.update(dto);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void updateRoles_employeeNotFound_throwException() {
        Long id = 404L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateRoles(id, List.of()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сотрудник с id = " + id + " не найден");
    }

    @Test
    void updateRoles_validData_updatedEmployee() {
        Long id = 1L;
        List<String> roles = List.of("ROLE_ADMIN", "ROLE_USER");

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");

        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");

        List<Role> roleList = List.of(roleAdmin, roleUser);

        employee.setRoles(roleList);
        employeeDto.setRoles(roles);


        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(roleRepository.findByName(roleAdmin.getName())).thenReturn(Optional.of(roleAdmin));
        when(roleRepository.findByName(roleUser.getName())).thenReturn(Optional.of(roleUser));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto expected = new EmployeeDto();
        expected.setId(1L);
        expected.setFirstName("Kevin");
        expected.setLastName("Benson");
        expected.setPosition("Developer");
        expected.setUsername("Test");
        expected.setRoles(roles);

        EmployeeDto result = employeeService.updateRoles(id, roles);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void updateRoles_withNonExistenRole_updatedEmployee() {
        Long id = 1L;
        List<String> roles = List.of("NON-EXISTEN");

        employeeDto.setRoles(List.of());

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(roleRepository.findByName("NON-EXISTEN")).thenReturn(Optional.empty());
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto expected = new EmployeeDto();
        expected.setId(1L);
        expected.setFirstName("Kevin");
        expected.setLastName("Benson");
        expected.setPosition("Developer");
        expected.setUsername("Test");
        expected.setRoles(List.of());

        EmployeeDto result = employeeService.updateRoles(id, roles);

        assertThat(result).isEqualTo(expected);
    }


    @Test
    void findByProjectsId_projectNotFound_throwException() {
        Long id = 404L;

        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findByProjectsId(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Проект с id = " + id + " не найден");
    }

    @Test
    void findByProjectsId_validData_employeeDtoList() {
        Long id = 1L;

        List<Employee> team = List.of(employee, admin);

        Project project = new Project();
        project.setId(id);
        project.setTeam(team);

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);
        when(employeeMapper.toDto(admin)).thenReturn(adminDto);
        when(employeeRepository.findByProjectsId(id)).thenReturn(team);

        EmployeeDto e1 = new EmployeeDto();
        e1.setId(1L);
        e1.setUsername("Test");
        e1.setFirstName("Kevin");
        e1.setLastName("Benson");
        e1.setPosition("Developer");

        EmployeeDto e2 = new EmployeeDto();
        e2.setId(2L);
        e2.setUsername("Admin");
        e2.setFirstName("Kirill");
        e2.setLastName("Ivanov");
        e2.setPosition("Project Manager");

        List<EmployeeDto> expected = List.of(e1, e2);
        List<EmployeeDto> result = employeeService.findByProjectsId(id);

        assertThat(result).isEqualTo(expected);
    }
}