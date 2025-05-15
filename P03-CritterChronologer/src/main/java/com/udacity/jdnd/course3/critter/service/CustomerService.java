package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.data.pet.PetRepository;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.data.user.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    public Customer saveCustomer(Customer customer) {
        logger.info("Saving customer with name: {}", customer.getName());
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        logger.info("Retrieving all customers");
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        logger.info("Fetching customer by ID: {}", id);
        return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    public Customer getCustomerByPetId(Long petId) {
        logger.info("Fetching customer by pet ID: {}", petId);
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        return pet.getOwner();
    }
}
