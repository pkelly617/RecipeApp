package com.perscholas.recipeApp.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.perscholas.recipeApp.models.Filter;
import com.perscholas.recipeApp.models.Recipe;
import com.perscholas.recipeApp.models.User;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
	
	@Autowired
	UserService us;
	List<User> originalUsers;

	@BeforeEach
	void setUp() throws Exception {
		originalUsers = us.findAll();
	}

	@AfterEach
	void tearDown() throws Exception {
		for (User user : us.findAll()) {
			if (user.getUsername().contains("testUser")) {
				us.deleteById(user.getId());
			}
		}
	}

	@Test
	void testFindAll() {
		assertTrue(true);
	}
	
	@Test
	void testFindById() {
		User newUser = new User("testUser1", "testEmail", "testPassword");
		us.save(newUser);
		int id = us.findByUsername(newUser.getUsername()).getId();
		User actual = us.findById(id);
		assertTrue(actual.getUsername().equals(newUser.getUsername()));
	}
	
	@Test
	void testExistsById() {
		User newUser = new User("testUser2", "testEmail", "testPassword");
		us.save(newUser);
		int id = us.findByUsername(newUser.getUsername()).getId();
		assertTrue(us.existsById(id));
	}
	
	@Test
	void testDeleteById() {
		User newUser = new User("testUser3", "testEmail", "testPassword");
		us.save(newUser);
		int id = us.findByUsername(newUser.getUsername()).getId();
		us.deleteById(id);
		assertThrows(NoSuchElementException.class, () -> { us.findById(id);});
	}
	
	@Test
	void testSave() {
		User newUser = new User("testUser4", "testEmail", "testPassword");
		int preSize = us.findAll().size();
		us.save(newUser);
		int postSize = us.findAll().size();
		boolean sizesCorrect = ((Integer) (postSize - preSize)).equals(1);
		boolean nameCorrect = us.findByUsername(newUser.getUsername()).getUsername().equals(newUser.getUsername());
		assertTrue(nameCorrect && sizesCorrect);
	}
	
	@Test
	void testFindByUsername() {
		User newUser = new User("testUser5", "testEmail", "testPassword");
		String expected = newUser.getUsername();
		us.save(newUser);
		User actual = us.findByUsername(expected);
		assertEquals(newUser.getUsername(), actual.getUsername());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"testUser5", "testEmail"})
	void testFindByUsernameOrEmail() {
		User newUser = new User("testUser5", "testEmail", "testPassword");
		String expected = newUser.getUsername();
		us.save(newUser);
		User actual = us.findByUsername(expected);
		boolean nameCheck = newUser.getUsername().equals(actual.getUsername());
		boolean emailCheck = newUser.getEmail().equals(actual.getEmail());
		assertTrue(nameCheck || emailCheck);
	}

}
