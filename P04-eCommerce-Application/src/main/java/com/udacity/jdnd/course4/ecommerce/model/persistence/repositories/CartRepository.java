package com.udacity.jdnd.course4.ecommerce.model.persistence.repositories;

import com.udacity.jdnd.course4.ecommerce.model.persistence.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udacity.jdnd.course4.ecommerce.model.persistence.User;

import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
