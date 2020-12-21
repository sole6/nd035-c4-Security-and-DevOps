package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.SareetaApplicationTests.injectObjects;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTests {
    @Test
    public void contextLoads() {
    }
    private ItemController itemController;

    private CartRepository cartRepository = mock(CartRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private static User user;

    private static Item item;
    private static List<Item> itemList;
    @BeforeClass
    public static void init() {
        item = new Item();
        item.setPrice(new BigDecimal("100"));
        item.setName("Item1");
        item.setDescription("Item1 descr");
        itemList = new ArrayList<Item>();
        itemList.add(item);
    }
    @Before
    public void setup() {
        itemController = new ItemController();
        injectObjects(itemController, "userRepository", userRepository);
        injectObjects(itemController, "itemRepository", itemRepository);
    }
    @Test
    public void getItems() {
       when(itemRepository.findAll()).thenReturn(itemList);

        final ResponseEntity<List<Item>> res = itemController.getItems();

        assertEquals(200, res.getStatusCodeValue());

        List<Item> responseItems = res.getBody();
        assertNotNull(responseItems);
        assertEquals(1, responseItems.size());
        assertEquals(item.getId(), responseItems.get(0).getId());
    }
}
