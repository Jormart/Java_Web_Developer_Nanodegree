package com.udacity.jdnd.course4.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course4.ecommerce.model.persistence.Cart;
import com.udacity.jdnd.course4.ecommerce.model.persistence.User;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.jdnd.course4.ecommerce.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/user")
public class UserController {

	public static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		try {
			log.info("Received createUser request for username: {}", createUserRequest.getUsername());

			// Check if the user already exists
			if (userRepository.findByUsername(createUserRequest.getUsername()) != null) {
				log.error("CreateUser failed - User already exists: {}", createUserRequest.getUsername());
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}

			// Validate password
			if (createUserRequest.getPassword().length() < 7 ||
					!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
				log.error("CreateUser failed - Password validation error for user: {}", createUserRequest.getUsername());
				return ResponseEntity.badRequest().build();
			}

			// Create new User and Cart
			User user = new User();
			user.setUsername(createUserRequest.getUsername());
			Cart cart = new Cart();
			cartRepository.save(cart);
			user.setCart(cart);
			user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

			// Save the user
			userRepository.save(user);
			log.info("CreateUser succeeded for user: {}", createUserRequest.getUsername());
			return ResponseEntity.ok(user);

		} catch (Exception e) {
			log.error("CreateUser failed - Exception: {} for user: {}", e.getMessage(), createUserRequest.getUsername());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


}
