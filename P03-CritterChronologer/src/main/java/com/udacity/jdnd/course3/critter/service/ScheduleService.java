package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.user.EmployeeRepository;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.data.pet.PetRepository;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.data.schedule.ScheduleRepository;
import com.udacity.jdnd.course3.critter.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Schedule createSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        logger.info("Creating schedule for employees: {} and pets: {}", employeeIds, petIds);
        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        List<Pet> pets = petRepository.findAllById(petIds);
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedulesForPet(Long petId) {
        logger.info("Fetching schedules for pet ID: {}", petId);
        return scheduleRepository.findByPets_Id(petId);
    }

    public List<Schedule> getSchedulesForEmployee(Long employeeId) {
        logger.info("Fetching schedules for employee ID: {}", employeeId);
        return scheduleRepository.findByEmployees_Id(employeeId);
    }

    public List<Schedule> getSchedulesForCustomer(Long customerId) {
        logger.info("Fetching schedules for customer ID: {}", customerId);
        return scheduleRepository.findByPets_OwnerId(customerId);
    }


    public List<Schedule> getAllSchedules() {
        logger.info("Fetching all schedules");
        return scheduleRepository.findAll();
    }
}

