package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.data.pet.PetRepository;
import com.udacity.jdnd.course3.critter.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetService.class);

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Pet savePet(Pet pet, Long ownerId) {
        logger.info("Saving pet with name: {} for owner ID: {}", pet.getName(), ownerId);

        // Retrieve the owner (Customer) using the provided ownerId
        Customer owner = customerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + ownerId));

        // Associate the pet with the owner and add the pet to the owner's list
        pet.setOwner(owner);
        Pet savedPet = petRepository.save(pet);

        // Ensure the customer has the pet in their list of pets
        owner.getPets().add(savedPet);
        customerRepository.save(owner); // Save the customer with the updated pet list

        return savedPet;
    }

    public Pet getPetById(Long id) {
        logger.info("Fetching pet by ID: {}", id);
        return petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + id));
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        logger.info("Fetching pets for owner ID: {}", ownerId);
        return petRepository.findByOwnerId(ownerId);
    }

    public List<Pet> getAllPets() {
        logger.info("Fetching all pets");
        return petRepository.findAll();
    }
}


