package com.perscholas.recipeApp.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.websocket.Session;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AutoPopulatingList;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.perscholas.recipeApp.models.Filter;
import com.perscholas.recipeApp.models.Recipe;
import com.perscholas.recipeApp.models.User;
import com.perscholas.recipeApp.security.MyUserDetails;
import com.perscholas.recipeApp.services.FilterService;
import com.perscholas.recipeApp.services.RecipeService;
import com.perscholas.recipeApp.services.UserService;
import com.perscholas.recipeApp.utilities.FilterDeletionException;
import com.perscholas.recipeApp.utilities.FilterWrapper;
import com.perscholas.recipeApp.utilities.Utilities;

/**
 * @author Peter Kelly
 *
 */
@Controller
public class SearchController {

	@Autowired
	UserService userService;
	@Autowired
	RecipeService recipeService;
	@Autowired
	FilterService filterService;
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

	@GetMapping("/recipe_search")
	public String searchRecipes(@AuthenticationPrincipal MyUserDetails user, Model model) {
		
		User u = userService.findByUsername(user.getUsername());
		Set<Recipe> recipes = u.getRecipes();
		Set<Recipe> results = new HashSet<Recipe>();
		ArrayList<Filter> appliedFilters = new ArrayList<Filter>(u.getFilters());
		FilterWrapper wrapper = new FilterWrapper();
		wrapper.setFilters(appliedFilters);
		model.addAttribute("user", u);
		model.addAttribute("recipes", recipes);
		model.addAttribute("searchStr", new String());
		model.addAttribute("results", results);
		model.addAttribute("wrapper", wrapper);
		return "recipe_search";
	}
	
	@PostMapping("recipeSearch")
	public String showRecipes(@RequestParam String searchStr, @ModelAttribute("wrapper") FilterWrapper wrapper , @AuthenticationPrincipal MyUserDetails user, Model model) {
		User u = userService.findByUsername(user.getUsername());

		Set<Recipe> results = new HashSet<Recipe>(recipeService.basicSearch(searchStr));
		if (!wrapper.getFilters().isEmpty()) {
			List<Recipe> searchResults = new ArrayList<Recipe>(results);
			results = recipeService.passThroughFilters(searchResults, wrapper.getFilters());
		}
		
		

		ArrayList<Filter> appliedFilters = new ArrayList<Filter>(u.getFilters());
		FilterWrapper wrapper2 = new FilterWrapper();
		wrapper2.setFilters(appliedFilters);
		Set<Recipe> recipes = u.getRecipes();
		model.addAttribute("user", u);
		model.addAttribute("recipes", recipes);
		model.addAttribute("results", results);
		model.addAttribute("filters", u.getFilters());
		model.addAttribute("wrapper", wrapper2);

		return "recipe_search";
	}
	
	@GetMapping("/ingredient_search")
	public String searchIngredient(@AuthenticationPrincipal MyUserDetails user, Model model) {
		
		User u = userService.findByUsername(user.getUsername());
		
		Set<Recipe> recipes = u.getRecipes();
		Set<Recipe> results = new HashSet<Recipe>();
		model.addAttribute("user", u);
		model.addAttribute("recipes", recipes);
		model.addAttribute("searchStr", new String());
		model.addAttribute("results", results);
		return "ingredient_search";
	}
	
	@PostMapping("ingredientSearch")
	public String showIngredientRecipes(@RequestParam String searchStr, @AuthenticationPrincipal MyUserDetails user, Model model) {
		User u = userService.findByUsername(user.getUsername());

		String[] ingredients = searchStr.split(",");
		//System.out.println("WHY " + searchStr + " = search");
		List<Recipe> test = recipeService.findAll();
		List<Recipe> results = recipeService.getRecipesWithIngredients(test, ingredients);
		Set<Recipe> recipes = u.getRecipes();
		model.addAttribute("user", u);
		model.addAttribute("recipes", recipes);
		model.addAttribute("results", results);
		System.out.println(test.size());
		for (Recipe r : results) {
			System.out.println(r);
		}
		//return "recipe_search";
		return "ingredient_search";
	}
	
	

}
