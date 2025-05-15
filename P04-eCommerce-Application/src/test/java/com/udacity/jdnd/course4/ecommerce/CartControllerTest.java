package com.udacity.jdnd.course4.ecommerce;

import com.udacity.jdnd.course4.ecommerce.controllers.CartController;
import com.udacity.jdnd.course4.ecommerce.model.persistence.Cart;
import com.udacity.jdnd.course4.ecommerce.model.persistence.Item;
import com.udacity.jdnd.course4.ecommerce.model.persistence.User;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.ItemRepository;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.jdnd.course4.ecommerce.model.requests.ModifyCartRequest;
import org.junit.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addToCart_happy_path() {
        User user = createUser();
        Item item = createItem();

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(2);

        final ResponseEntity<Cart> response = cartController.addToCart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getItems().size());
    }

    @Test
    public void addToCart_user_not_found() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("unknownUser");
        request.setItemId(1L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addToCart(request);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart_item_not_found() {
        User user = createUser();
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.removeFromCart(request);

        assertEquals(404, response.getStatusCodeValue());
    }

    private User createUser() {
        User user = new User();
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        user.setUsername("testUser");
        return user;
    }

    private Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(BigDecimal.valueOf(10.99));
        return item;
    }
}

