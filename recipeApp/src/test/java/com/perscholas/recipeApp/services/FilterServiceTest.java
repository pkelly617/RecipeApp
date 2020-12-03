package com.perscholas.recipeApp.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.perscholas.recipeApp.models.Filter;
import com.perscholas.recipeApp.utilities.FilterDeletionException;

@RunWith(SpringRunner.class)
@SpringBootTest
class FilterServiceTest {
	
	@Autowired
	FilterService fs;
	List<Filter> originalFilters;

	@BeforeEach
	void setUp() throws Exception {
		originalFilters = fs.findAll();
	}

	@AfterEach
	void tearDown() throws Exception {
		for (Filter filter : fs.findAll()) {
			if (!originalFilters.contains(filter)) {
				fs.deleteById(filter.getId());
			}
		}
	}

	@Test
	void testFindAll() {
		assertEquals(originalFilters, fs.findAll());
	}
	
	@Test
	void testFindById() {
		List<String> empty = new ArrayList<String>();
		Filter newFilter = new Filter("testFilter", 0, empty, empty, "type", "cuisine", false);
		fs.save(newFilter);
		int id = fs.findByName(newFilter.getName()).getId();
		Filter actual = fs.findById(id);
		assertEquals(newFilter, actual);
	}
	
	@Test
	void testExistsById() {
		List<String> empty = new ArrayList<String>();
		Filter newFilter = new Filter("testFilter", 0, empty, empty, "type", "cuisine", false);
		fs.save(newFilter);
		int id = fs.findByName(newFilter.getName()).getId();
		assertTrue(fs.existsById(id));
	}
	
	@Test
	void testDeleteById() {
		List<String> empty = new ArrayList<String>();
		Filter newFilter = new Filter("testFilter", 0, empty, empty, "type", "cuisine", false);
		fs.save(newFilter);
		int id = fs.findByName(newFilter.getName()).getId();
		try {
			fs.deleteById(id);
		} catch (FilterDeletionException e) {
			e.printStackTrace();
		}
		assertThrows(NoSuchElementException.class, () -> { fs.findById(id);});
	}
	
	@Test
	void testSave() {
		List<String> empty = new ArrayList<String>();
		Filter newFilter = new Filter("testFilter", 0, empty, empty, "type", "cuisine", false);
		int preSize = fs.findAll().size();
		fs.save(newFilter);
		int postSize = fs.findAll().size();
		boolean sizesCorrect = ((Integer) (postSize - preSize)).equals(1);
		boolean nameCorrect = fs.findByName(newFilter.getName()).getName().equals(newFilter.getName());
		assertTrue(nameCorrect && sizesCorrect);
	}
	
	@Test
	void testFindByName() {
		List<String> empty = new ArrayList<String>();
		Filter newFilter = new Filter("testFilter", 0, empty, empty, "type", "cuisine", false);
		String expected = newFilter.getName();
		fs.save(newFilter);
		Filter actual = fs.findByName(expected);
		assertEquals(newFilter, actual);
	}

}