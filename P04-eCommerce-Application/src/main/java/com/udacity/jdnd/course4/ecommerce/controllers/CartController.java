package com.udacity.jdnd.course4.ecommerce.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course4.ecommerce.model.persistence.Cart;
import com.udacity.jdnd.course4.ecommerce.model.persistence.Item;
import com.udacity.jdnd.course4.ecommerce.model.persistence.User;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.ItemRepository;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.jdnd.course4.ecommerce.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ItemRepository itemRepository;

	private static final Logger log = LoggerFactory.getLogger(CartController.class);

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		log.info("Received addToCart request - Username: {}, Item ID: {}, Quantity: {}",
				request.getUsername(), request.getItemId(), request.getQuantity());

		User user = userRepository.findByUsername(request.getUsername());
		if (user == null) {
			log.error("Add to cart failed - User not found: {}", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());
		if (!item.isPresent()) {
			log.error("Add to cart failed - Item not found: {}", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity()).forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);

		log.info("Add to cart succeeded - User: {}, Item ID: {}, Quantity: {}",
				request.getUsername(), request.getItemId(), request.getQuantity());
		return ResponseEntity.ok(cart);
	}


	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		log.info("Received removeFromCart request - Username: {}, Item ID: {}, Quantity: {}",
				request.getUsername(), request.getItemId(), request.getQuantity());

		User user = userRepository.findByUsername(request.getUsername());
		if (user == null) {
			log.error("Remove from cart failed - User not found: {}", request.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());
		if (!item.isPresent()) {
			log.error("Remove from cart failed - Item not found: {}", request.getItemId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity()).forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);

		log.info("Remove from cart succeeded - User: {}, Item ID: {}, Quantity: {}",
				request.getUsername(), request.getItemId(), request.getQuantity());
		return ResponseEntity.ok(cart);
	}

}


