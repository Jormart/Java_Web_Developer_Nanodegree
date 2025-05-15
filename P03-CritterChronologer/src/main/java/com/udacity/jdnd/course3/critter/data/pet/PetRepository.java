package com.udacity.jdnd.course3.critter.data.pet;

import com.udacity.jdnd.course3.critter.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    // Custom method to find pets by owner
    List<Pet> findByOwnerId(Long ownerId);
}
