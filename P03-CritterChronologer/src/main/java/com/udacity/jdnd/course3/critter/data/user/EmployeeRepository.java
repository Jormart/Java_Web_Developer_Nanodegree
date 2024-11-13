package com.udacity.jdnd.course3.critter.data.user;

import com.udacity.jdnd.course3.critter.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Find all employees available on a specific day
    List<Employee> findAllByDaysAvailableContains(DayOfWeek dayOfWeek);
}
