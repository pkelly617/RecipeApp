package com.perscholas.recipeApp.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perscholas.recipeApp.dao.RecipeRepoIF;
import com.perscholas.recipeApp.models.Filter;
import com.perscholas.recipeApp.models.Recipe;

/**
 * @author Peter Kelly
 *
 */
@Service
public class RecipeService {
	
	@Autowired
	RecipeRepoIF recipeRepo;
	
	public List<Recipe> findAll() {
		return recipeRepo.findAll();
	}
	
	public Recipe findById(Integer id) {
		return recipeRepo.findById(id).get();
	}
	
	public boolean existsById(Integer id) {
		return recipeRepo.existsById(id);
	}
	
	public void deleteById(Integer id) {

		if (existsById(id))
			recipeRepo.deleteById(id);

	}
	public void save(Recipe r) {
		recipeRepo.save(r);
	}
	
	public List<Recipe> simpleSearch(String search) {
		
		List<Recipe> results = recipeRepo.findByTitleOrTypeOrCuisine(search, search, search);
		results.addAll(findByPartOfTitle(search));
		return results;
		
	}
	
	public List<Recipe> basicSearch(String search) {
		search = search.trim();
		List<Recipe> results = recipeRepo.findByTitleContainingOrTypeContainingOrCuisineContaining(search, search, search);
		return results;
		
	}
	
	public List<Recipe> findByTitle(String title) {
		return recipeRepo.findByTitle(title);
	}
	
	public List<Recipe> findByType(String type) {
		return recipeRepo.findByType(type);
	}
	
	public List<Recipe> findByTitleTypeCuisine(String title, String type, String cuisine) {
		return recipeRepo.findByTitleContainingOrTypeContainingOrCuisineContaining(title, type, cuisine);
	}
	
	/**
	 * Returns a list of recipes that contain all of the ingredients within the 
	 * ingredients array. For each recipe, this method iterates over each ingredient
	 * checking whether the recipe's ingredients list contains the desired ingredients.
	 * If the ingredient is found, this method updates a boolean variable (includesAll), 
	 * which acts as a flag to signal that the recipe contains the ingredient. If for 
	 * at any point during the iteration of ingredients the flag is switched to false, 
	 * this method breaks. If at the end of iteration the flag is true, the recipe is
	 * added to the list of recipes to be returned.
	 * 
	 * @param recipes      a collection of recipes
	 * @param ingredients  an array of ingredients 
	 * @return a list of recipes that contain all of the ingredients in the 
	 * 		   ingredients array
	 */
	public List<Recipe> getRecipesWithIngredients(List<Recipe> recipes, String[] ingredients) {
		
//		ArrayList<Recipe> contains = new ArrayList<Recipe>();
//		boolean includesAll = false;
//		
//		for (Recipe recipe : recipes) {
//			for (String ingredient : ingredients) {
//				includesAll = recipe.getIngredients().contains(ingredient.trim());
//				if (!includesAll)
//					break;
//			}
//			if (includesAll) contains.add(recipe);
//		}
//		
//		return contains;
		
		ArrayList<Recipe> contains = new ArrayList<Recipe>();
		boolean includes = false;
		Integer count = 0;
		for (Recipe recipe : recipes) {
			count = 0;
			for (String ingredient : ingredients) {
				for (String check : recipe.getIngredients()) {
					includes = check.toUpperCase().equals(ingredient.trim().toUpperCase());
					if (includes)
						count++;
				}
			}
			if (count >= ingredients.length) 
				contains.add(recipe);
		}
		
		return contains;
		
	}
	
	/**
	 * Returns a set of recipes that pass through at least one of the filters in 
	 * the collection of filters. This method iterates over recipes and checks if
	 * it passes through at least one of the filters. If so, this method adds the 
	 * recipe to the set of recipes that also passed through a filter and that 
	 * will be returned. This method breaks after adding the recipe because the 
	 * recipe is only required to pass through at least one filter to be eligible 
	 * to be added to the return set.
	 * 
	 * @param recipes a collection of recipes to be filtered
	 * @param filters a collection of filters to apply to the collection of recipes
	 * @return a set of recipes that passed through the filters
	 */
	public Set<Recipe> passThroughFilters(List<Recipe> recipes, List<Filter> filters) {
		Set<Recipe> results = new HashSet<Recipe>();
		for (Recipe recipe : recipes) {
			for (Filter filter : filters) {
				if (passesFilter(recipe, filter)) {
					results.add(recipe);
					break;
				}
			}
		}
		
		return results;
	}
	
	/**
	 * Returns a List of recipes that include the string substr within their
	 * Title.
	 * 
	 * @param substr  a string to be searched for in all recipe Titles 
	 * @return recipes that contain the string in their title
	 */
	private List<Recipe> findByPartOfTitle(String substr) {
		ArrayList<Recipe> results = new ArrayList<Recipe>();
		for (Recipe recipe : findAll()) {
			if (recipe.getTitle().contains(substr)) {
				results.add(recipe);
			}
		}
		return results;
	}
	
	/**
	 * Returns true if the given recipe passes through the given filter and 
	 * false otherwise. The method compares the common attributes (Cuisine, Type,
	 * Minutes, Ingredients, and Favorite) to see whether the recipe passes or not.
	 * This method also checks against default values so that if a user did not 
	 * specify one of the attributes to be considered when creating the filter,
	 * a recipe is not filtered out based on that attribute not being initialized.
	 * This method makes sure to execute the most lengthy check last (checkIngredients).
	 * Since that check runs the longest, this method takes advantage of short circuiting 
	 * so that if one of the other attributes doesn't match up, this method can forgo that
	 * check.
	 * 
	 * @param recipe  a recipe which must be compared to a filter
	 * @param filter  the filter that must be compared with the recipe
	 * @return a boolean value indicating whether the recipe has passed through
	 * 		   the given filter
	 */
	private boolean passesFilter(Recipe recipe, Filter filter) {
		
		boolean cuisine = (filter.getCuisine() == null) ||
				((filter.getCuisine().equals(recipe.getCuisine())) ||
				(filter.getCuisine().equals("None"))			   ||
				(filter.getCuisine().equals("")));
		
		boolean type = (filter.getType() == null) || 
				((filter.getType().equals(recipe.getType())) ||
				(filter.getType().equals("None"))			 ||
				(filter.getType().equals("")));
		
		boolean minutes = (filter.getMinutes() == 0) ||
				(((Integer) filter.getMinutes()) >= (recipe.getMinutes()));
		
		boolean favorite = (filter.isFavorite()) ?  recipe.isFavorite() : true;
		
		return cuisine && type && minutes && favorite && checkIngredients(recipe, filter);
		
	}
	
	/**
	 * Returns true if the ingredients required for the recipe parameter are valid
	 * (ingredients are found within the include list of the filter) and returns 
	 * false otherwise (ingredients are found within the exclude list of the filter).
	 * 
	 * 
	 * @param recipe  a recipe that must be checked for ingredients
	 * @param filter  a filter that specifies what ingredients are valid/invalid
	 * @return a boolean value indicating whether the ingredients included in the
	 * 		   the recipe are valid/invalid
	 */
	private boolean checkIngredients(Recipe recipe, Filter filter) {
		
		if (filter.getExclude().isEmpty() && filter.getInclude().isEmpty())
			return true;
		
		Integer check = 0;
		
		for (String ingredient : recipe.getIngredients()) {
			for (String exclude : filter.getExclude()) {
				if (ingredient.toUpperCase().contains(exclude.toUpperCase())) {
					return false;
				}
			}
			for (String include : filter.getInclude()) {
				// put in hashmap if not seen, check hashmap first
				if (ingredient.toUpperCase().contains(include.toUpperCase())) {
					check++;
				}
			}
			
		}
		System.out.println("Check =" + check);
		return check >= filter.getInclude().size();
		
	}

}
