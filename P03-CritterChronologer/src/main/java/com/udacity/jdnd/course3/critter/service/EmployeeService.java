package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.user.EmployeeRepository;
import com.udacity.jdnd.course3.critter.data.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        logger.info("Saving employee with name: {}", employee.getName());
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        logger.info("Fetching employee by ID: {}", id);
        return employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    public void setEmployeeAvailability(Long employeeId, Set<DayOfWeek> daysAvailable) {
        logger.info("Updating availability for employee ID: {}", employeeId);
        Employee employee = getEmployeeById(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, DayOfWeek day) {
        logger.info("Finding employees with skills {} available on {}", skills, day);

        // Fetch employees available on the given day
        List<Employee> employeesAvailable = employeeRepository.findAllByDaysAvailableContains(day);

        logger.info("Employees available on {}: {}", day, employeesAvailable.stream()
                .map(e -> "ID: " + e.getId() + ", Skills: " + e.getSkills())
                .collect(Collectors.toList()));

        // Filter employees who have all the required skills
        List<Employee> suitableEmployees = employeesAvailable.stream()
                .filter(e -> e.getSkills() != null && e.getSkills().containsAll(skills))
                .collect(Collectors.toList());

        logger.info("Employees found: {}", suitableEmployees.stream().map(Employee::getId).collect(Collectors.toList()));

        return suitableEmployees;
    }

}
