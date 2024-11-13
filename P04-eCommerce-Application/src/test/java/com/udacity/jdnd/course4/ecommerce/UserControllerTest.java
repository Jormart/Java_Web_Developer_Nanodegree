package com.udacity.jdnd.course4.ecommerce;

import com.udacity.jdnd.course4.ecommerce.controllers.UserController;
import com.udacity.jdnd.course4.ecommerce.model.persistence.User;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.CartRepository;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.UserRepository;
import com.udacity.jdnd.course4.ecommerce.model.requests.CreateUserRequest;
import org.junit.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private CartRepository cartRepository = mock(CartRepository.class);

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void create_user_happy_path() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void create_user_password_too_short() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("short");
        request.setConfirmPassword("short");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void create_user_password_mismatch() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("differentPassword");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_id() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        final ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", response.getBody().getUsername());
    }

    @Test
    public void find_user_by_username() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test", response.getBody().getUsername());
    }

    @Test
    public void find_user_by_username_not_found() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        final ResponseEntity<User> response = userController.findByUserName("unknown");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
