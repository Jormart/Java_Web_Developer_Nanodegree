package com.udacity.jdnd.course4.ecommerce;

import com.udacity.jdnd.course4.ecommerce.controllers.ItemController;
import com.udacity.jdnd.course4.ecommerce.model.persistence.Item;
import com.udacity.jdnd.course4.ecommerce.model.persistence.repositories.ItemRepository;
import org.junit.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getItems_happy_path() {
        List<Item> items = new ArrayList<>();
        items.add(createItem(1L, "Item1"));
        items.add(createItem(2L, "Item2"));

        when(itemRepository.findAll()).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void getItemById_happy_path() {
        Item item = createItem(1L, "TestItem");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("TestItem", response.getBody().getName());
    }

    @Test
    public void getItemById_not_found() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByName_happy_path() {
        List<Item> items = new ArrayList<>();
        items.add(createItem(1L, "TestItem"));

        when(itemRepository.findByName("TestItem")).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("TestItem");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("TestItem", response.getBody().get(0).getName());
    }

    @Test
    public void getItemsByName_not_found() {
        when(itemRepository.findByName("UnknownItem")).thenReturn(new ArrayList<>());

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("UnknownItem");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private Item createItem(Long id, String name) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(BigDecimal.valueOf(9.99));
        item.setDescription("Test description");
        return item;
    }
}
