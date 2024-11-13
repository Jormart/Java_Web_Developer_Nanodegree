package com.udacity.jdnd.course3.critter.data.schedule;

import com.udacity.jdnd.course3.critter.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Custom method to find schedules by pet ID
    List<Schedule> findByPets_Id(Long petId);

    // Custom method to find schedules by employee ID
    List<Schedule> findByEmployees_Id(Long employeeId);

    // Custom method to find schedules by customer ID (via pet ownership)
    List<Schedule> findByPets_OwnerId(Long ownerId);
}
