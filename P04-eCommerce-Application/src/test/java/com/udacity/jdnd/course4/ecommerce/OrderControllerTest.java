package com.udacity.jdnd.course4.ecommerce;

import com.udacity.jdnd.course4.ecommerce.controllers.OrderController;
import com.udacity.jdnd.course4.ecommerce.model.persistence.Cart;
import com.udacity.jdnd.course4.ecommerce.model.persistence.Item;
import com.udacity.jdnd.course4.ecommerce.model.persistence.User;
import com.udacity.jdnd.course4.ecommerce.model.persistence.UserOrder;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.OrderRepository;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.UserRepository;
import org.junit.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void submitOrder_happy_path() {
        User user = createUser();
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getItems().size());
    }

    @Test
    public void submitOrder_user_not_found() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        final ResponseEntity<UserOrder> response = orderController.submit("unknownUser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void submitOrder_empty_cart() {
        User user = createUser();
        user.getCart().getItems().clear();
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_happy_path() {
        User user = createUser();
        List<UserOrder> orders = new ArrayList<>();
        orders.add(UserOrder.createFromCart(user.getCart()));

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orders);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void getOrdersForUser_user_not_found() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("unknownUser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User createUser() {
        User user = new User();
        Cart cart = new Cart();
        cart.setUser(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(BigDecimal.valueOf(9.99));

        cart.addItem(item);
        user.setCart(cart);
        user.setUsername("testUser");
        return user;
    }
}
