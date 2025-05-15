package com.udacity.jdnd.course4.ecommerce.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course4.ecommerce.model.persistence.User;
import com.udacity.jdnd.course4.ecommerce.model.persistence.UserOrder;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.OrderRepository;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			log.error("Order submission failed - User not found: {}", username);
			return ResponseEntity.notFound().build();
		}
		if (user.getCart().getItems().isEmpty()) {
			log.error("Order submission failed - Cart is empty for user: {}", username);
			return ResponseEntity.badRequest().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		log.info("Order submitted successfully for user: {}", username);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			log.error("Order history retrieval failed - User not found: {}", username);
			return ResponseEntity.notFound().build();
		}
		List<UserOrder> orders = orderRepository.findByUser(user);
		log.info("Order history retrieved successfully for user: {}, Order count: {}", username, orders.size());
		return ResponseEntity.ok(orders);
	}
}

