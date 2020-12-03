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
public class FilterController {

	@Autowired
	UserService userService;
	@Autowired
	RecipeService recipeService;
	@Autowired
	FilterService filterService;
	
	private int editId = 0;
	
	private Utilities utilities = new Utilities();
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();



	@GetMapping("/new_recipe")
	public String addRecipe(Model modelRecipes) {

		Recipe newRecipe = new Recipe();
		modelRecipes.addAttribute("newRecipe", newRecipe);
		modelRecipes.addAttribute("typeList", utilities.getTypes());
		modelRecipes.addAttribute("cuisineList", utilities.getCuisines());

		return "new_recipe";
	}

	@PostMapping("saverecipe")
	public String saveRecipe(@Valid @ModelAttribute("newRecipe") Recipe newRecipe, @AuthenticationPrincipal MyUserDetails user, Authentication auth, BindingResult bind) {
		
		User u = userService.findByUsername(user.getUsername());
		u.getRecipes().add(newRecipe);
		newRecipe.getUsers().add(u);
		recipeService.save(newRecipe);
		userService.save(u);
		u = userService.findByUsername(user.getUsername());
		return "redirect:/user_recipes";
		
	}
	
	@GetMapping("/add_recipe/{id}")
	public String addRecipe(@PathVariable int id, @AuthenticationPrincipal MyUserDetails user, Model model) {

		Recipe recipe = recipeService.findById(id);
		User u = userService.findByUsername(user.getUsername());

		
		if (!u.containsRecipe(recipe.getId())) {
			u.addRecipe(recipe);
			recipe.addUser(u);
			userService.save(u);
			recipeService.save(recipe);
		}
		
		return "redirect:/user_recipes";
	}
	
	@GetMapping("/remove_recipe/{id}")
	public String removeRecipe(@PathVariable int id, @AuthenticationPrincipal MyUserDetails user, Model model) {

		Recipe recipe = recipeService.findById(id);
		User u = userService.findByUsername(user.getUsername());

		u.removeRecipe(recipe.getId());
		recipe.removeUser(u.getId());

		if (recipe.isSecret()) {
			userService.save(u);
			recipeService.deleteById(recipe.getId());
		} else {
			recipeService.save(recipe);
			userService.save(u);
		}
		
		return "redirect:/user_recipes";
	}

	@GetMapping("/user_recipes")
	public String showUserRecipes(@AuthenticationPrincipal MyUserDetails user, Model model) {
		User u = userService.findByUsername(user.getUsername());
		model.addAttribute("user", u);
		return "user_recipes";
	}
	
	@GetMapping("/view_recipe/{id}")
	public String postShowRecipe(@PathVariable int id, @AuthenticationPrincipal MyUserDetails user, Model model) {
		User u = userService.findByUsername(user.getUsername());
		Recipe r = recipeService.findById(id);
		
		model.addAttribute("user", u);
		model.addAttribute("recipe", r);
		
		return "view_recipe";
	}
	
	
	@GetMapping("/edit_recipe/{id}")
	public String editRecipe(@PathVariable int id, @AuthenticationPrincipal MyUserDetails user, Model model) {
		// object of Users
		Recipe recipe = recipeService.findById(id);
		editId = id;
		// container for new user
		model.addAttribute("newRecipe", recipe);
		model.addAttribute("typeList", utilities.getTypes());
		model.addAttribute("cuisineList", utilities.getCuisines());
		return "edit_recipe";
	}
	
	@PostMapping("edit_recipe")
	public String saveEditRecipe(@Valid @ModelAttribute("newRecipe") Recipe newRecipe, @AuthenticationPrincipal MyUserDetails user, BindingResult bind) {
		
		Recipe newCopy = new Recipe(newRecipe);
		recipeService.save(newCopy);
		User u = userService.findByUsername(user.getUsername());
		System.out.println(newRecipe.getId() + " ------> " + editId);
		u.getRecipes().remove(recipeService.findById(editId));
		u.getRecipes().add(newCopy);
		userService.save(u);

		return "redirect:/user_recipes";
	}

	

}
