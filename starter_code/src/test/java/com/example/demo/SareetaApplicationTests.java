package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SareetaApplicationTests {

	@Test
	public void contextLoads() {
	}
	private UserController userController;

	private UserRepository userRepository = mock(UserRepository.class);

	private CartRepository cartRepository = mock(CartRepository.class);

	private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

	@Before
	public void setup() {
		userController = new UserController();
		injectObjects(userController, "userRepository", userRepository);
		injectObjects(userController, "cartRepository", cartRepository);
		injectObjects(userController, "bCryptPasswordEncoder", encoder);
	}

	@Test
	public void createUser() throws Exception {
		when(encoder.encode("password123456")).thenReturn("hashed");

		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername("test");
		createUserRequest.setPassword("password123456");
		createUserRequest.setConfirmedPassword("password123456");

		final ResponseEntity<User> response = userController.createUser(createUserRequest);

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());

		User user = response.getBody();
		assertNotNull(user);
		assertEquals(0, user.getId());
		assertEquals("test", user.getUsername());
		assertEquals("hashed", user.getPassword());
	}

	@Test
	public void getUserById() throws Exception {

		User user = new User();
		user.setUsername("test");
		user.setPassword("password123456");

		when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));

		final ResponseEntity<User> response = userController.findById(0L);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User userResponse = response.getBody();
		assertNotNull(userResponse);

		assertEquals(user.getId(), userResponse.getId());
		assertEquals(user.getUsername(), userResponse.getUsername());
		assertEquals(user.getPassword(), userResponse.getPassword());
	}

	@Test
	public void getUserByUsername() throws Exception {

		User user = new User();
		user.setUsername("test");
		user.setPassword("password123456");

		when(userRepository.findByUsername("test")).thenReturn(user);

		final ResponseEntity<User> response = userController.findByUserName("test");
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		User userResponse = response.getBody();
		assertNotNull(userResponse);

		assertEquals(user.getUsername(), userResponse.getUsername());
		assertEquals(user.getPassword(), userResponse.getPassword());
	}

	public static void injectObjects(Object target, String fieldname, Object toInject) {

		boolean wasPrivate = false;

		try {
			Field field = target.getClass().getDeclaredField(fieldname);
			if (!field.isAccessible()) {
				field.setAccessible(true);
				wasPrivate = true;
			}
			field.set(target, toInject);

			if (wasPrivate) field.setAccessible(false);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
