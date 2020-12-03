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
class RecipeServiceTest {
	
	@Autowired
	RecipeService rs;
	@Autowired
	UserService us;
	List<Recipe> originalRecipes;

	@BeforeEach
	void setUp() throws Exception {
		originalRecipes = rs.findAll();
	}

	@AfterEach
	void tearDown() throws Exception {
		for (Recipe recipe : rs.findAll()) {
			if (recipe.getCuisine().equals("testcuisine")) {
				rs.deleteById(recipe.getId());
			}
		}
	}

	@Test
	void testFindAll() {
		assertTrue(true);
	}
	
	@Test
	void testFindById() {
		List<String> empty = new ArrayList<String>();
		Recipe newRecipe = new Recipe("testRecipe1", 0, empty, empty, "type", "testcuisine", false, false);
		rs.save(newRecipe);
		List<Recipe> results = rs.findByTitle(newRecipe.getTitle());
		boolean check = false;
		int id = 0;
		for (Recipe r : results) {
			id = r.getId();
			check = r.getTitle().equals(newRecipe.getTitle());
			if (check) break;
		}
		assertTrue(check && rs.findById(id).getTitle().equals(newRecipe.getTitle()));
	}
	
	@Test
	void testExistsById() {
		List<String> empty = new ArrayList<String>();
		Recipe newRecipe = new Recipe("testRecipe2", 0, empty, empty, "type", "testcuisine", false, false);
		rs.save(newRecipe);
		int id = rs.findByTitle(newRecipe.getTitle()).get(0).getId();
		assertTrue(rs.existsById(id));
	}
	
	@Test
	void testDeleteById() {
		List<String> empty = new ArrayList<String>();
		Recipe newRecipe = new Recipe("testRecipe3", 0, empty, empty, "type", "testcuisine", false, false);
		rs.save(newRecipe);
		int id = rs.findByTitle(newRecipe.getTitle()).get(0).getId();
		rs.deleteById(id);
		assertThrows(NoSuchElementException.class, () -> { rs.findById(id);});
	}
	
	@Test
	void testSave() {
		List<String> empty = new ArrayList<String>();
		Recipe newRecipe = new Recipe("testRecipe4", 0, empty, empty, "type", "testcuisine", false, false);
		int preSize = rs.findAll().size();
		rs.save(newRecipe);
		int postSize = rs.findAll().size();
		boolean sizesCorrect = ((Integer) (postSize - preSize)).equals(1);
		assertTrue(sizesCorrect);
	}
	
	@Test
	void testFindByTitle() {
		List<String> empty = new ArrayList<String>();
		Recipe newRecipe = new Recipe("testRecipe5", 0, empty, empty, "type", "testcuisine", false, false);
		String expected = newRecipe.getTitle();
		rs.save(newRecipe);
		List<Recipe> results = rs.findByTitle(expected);
		System.out.println(results.contains(newRecipe));
		boolean check = false;
		for (Recipe r : results) {
			check = r.getTitle().equals(newRecipe.getTitle());
			if (check) break;
		}
		assertTrue(check);
	}
	
	@Test
	void testFindByType() {
		List<String> empty = new ArrayList<String>();
		Recipe newRecipe = new Recipe("testRecipe6", 0, empty, empty, "type", "testcuisine", false, false);
		String expected = newRecipe.getType();
		rs.save(newRecipe);
		List<Recipe> results = rs.findByType(expected);
		boolean check = false;
		for (Recipe r : results) {
			check = r.getType().equals(newRecipe.getType());
			if (check) break;
		}
		assertTrue(check);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"testRecipe7", "Recipe7", "sub"})
	void testFindByPartOfTitle(String searchStr) {
		List<String> empty = new ArrayList<String>();
		Recipe newRecipe = new Recipe("testRecipe7 sub", 0, empty, empty, "type", "testcuisine", false, false);
		rs.save(newRecipe);
		List<Recipe> results = rs.simpleSearch(searchStr);
		boolean check = false;
		for (Recipe r : results) {
			check = r.getTitle().equals(newRecipe.getTitle());
			if (check) break;
		}
		assertTrue(check);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"testRecipe9", "testType", "testcuisine"})
	void testSimpleSearch(String searchStr) {
		List<String> empty = new ArrayList<String>();
		Recipe newRecipe = new Recipe("testRecipe9", 0, empty, empty, "testType", "testcuisine", false, false);
		rs.save(newRecipe);
		List<Recipe> results = rs.simpleSearch(searchStr);
		boolean check = false;
		for (Recipe r : results) {
			check = r.getType().equals(newRecipe.getType()) || 
					r.getTitle().equals(newRecipe.getTitle()) ||
					r.getCuisine().equals(newRecipe.getCuisine());
			if (check) break;
		}
		assertTrue(check);
		
	}

}
