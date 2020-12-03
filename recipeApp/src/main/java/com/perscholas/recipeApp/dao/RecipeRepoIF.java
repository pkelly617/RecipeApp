package com.perscholas.recipeApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perscholas.recipeApp.models.Recipe;

public interface RecipeRepoIF extends JpaRepository<Recipe, Integer> {
	List<Recipe> findByTitle(String title);
	List<Recipe> findByTitleOrTypeOrCuisine(String title, String type, String cuisine);
	List<Recipe> findByMinutesLessThanEqual(Integer minutes);
	List<Recipe> findByType(String type);
	List<Recipe> findByTitleContainingOrTypeContainingOrCuisineContaining(String title, String type, String cuisine);
}
