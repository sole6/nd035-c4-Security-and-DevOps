package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static com.example.demo.SareetaApplicationTests.injectObjects;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTests {
    @Test
    public void contextLoads() {
    }
    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    private static User user;

    private static Item item;

    private static Cart cart;
    @BeforeClass
    public static void init() {

        user = new User();
        user.setUsername("test");
        user.setPassword("password123456");

        item = new Item();
        item.setPrice(new BigDecimal("100"));
        item.setName("Item1");
        item.setDescription("Item1 descr");
    }
    @Before
    public void setup() {
        cartController = new CartController();
        injectObjects(cartController, "userRepository", userRepository);
        injectObjects(cartController, "cartRepository", cartRepository);
        injectObjects(cartController, "itemRepository", itemRepository);
        cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);

        user.setCart(cart);
    }
    @Test
    public void addToCart() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(java.util.Optional.ofNullable(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(item.getId());
        request.setQuantity(300);
        request.setUsername(user.getUsername());

        final ResponseEntity<Cart> res = cartController.addTocart(request);

        assertEquals(200, res.getStatusCodeValue());

        Cart cartres = res.getBody();
        assertEquals(item, cartres.getItems().get(0)); }
}
