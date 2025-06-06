package com.udacity.jdnd.course4.ecommerce.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course4.ecommerce.model.persistence.Item;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	private static final Logger log = LoggerFactory.getLogger(ItemController.class);

	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.info("Received request to get all items");
		List<Item> items = itemRepository.findAll();
		log.info("Successfully retrieved all items. Item count: {}", items.size());
		return ResponseEntity.ok(items);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		log.info("Received request to get item by ID: {}", id);
		return ResponseEntity.of(itemRepository.findById(id));
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		log.info("Received request to get items by name: {}", name);
		List<Item> items = itemRepository.findByName(name);
		if (items == null || items.isEmpty()) {
			log.error("No items found with name: {}", name);
			return ResponseEntity.notFound().build();
		}
		log.info("Successfully retrieved items with name: {}. Item count: {}", name, items.size());
		return ResponseEntity.ok(items);
	}
}

