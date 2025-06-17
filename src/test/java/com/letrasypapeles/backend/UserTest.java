package com.letrasypapeles.backend;


import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.entity.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {

	@Test
	public void testCrearUsuario() {
		List<Role> roles = List.of(Role.builder().name("USER").build());
		User user = User.builder()
			.username("username")
			.password("pass123")
			.name("Juanito Usuario")
			.email("juanito@test.com")
			.roles(roles)
			.build();

			assertNotNull(user);
			assertEquals("username", user.getUsername());
			assertEquals("Juanito Usuario", user.getName());
			assertEquals("juanito@test.com", user.getEmail());
			assertEquals("pass123", user.getPassword());
			assertEquals(1, user.getRoles().size());


	}
}
