package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
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
public class OrderControllerTests {
    @Test
    public void contextLoads() {
    }
    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private static User user;

    private static Item item;
    private static Cart cart;
    @BeforeClass
    public static void init() {
        user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("password123456");
        item = new Item();
        item.setId(0L);
        item.setPrice(new BigDecimal("100"));
        item.setName("Item1");
        item.setDescription("Item1 descr");
    }
    @Before
    public void setup() {
        orderController = new OrderController();
        injectObjects(orderController, "userRepository", userRepository);
        injectObjects(orderController, "orderRepository", orderRepository);
    }
    @Test
    public void testOrder() {
        cart = new Cart();
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> res = orderController.submit(user.getUsername());

        assertEquals(200, res.getStatusCodeValue());

        UserOrder order = res.getBody();

        assertNotNull(order);
        assertEquals(cart.getItems(), order.getItems());
        assertEquals(cart.getTotal(), order.getTotal());
    }
}
