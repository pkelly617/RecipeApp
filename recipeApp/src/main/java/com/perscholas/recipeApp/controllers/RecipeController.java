package com.perscholas.recipeApp.controllers;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.perscholas.recipeApp.models.Filter;
import com.perscholas.recipeApp.models.User;
import com.perscholas.recipeApp.security.MyUserDetails;
import com.perscholas.recipeApp.services.FilterService;
import com.perscholas.recipeApp.services.RecipeService;
import com.perscholas.recipeApp.services.UserService;
import com.perscholas.recipeApp.utilities.FilterDeletionException;
import com.perscholas.recipeApp.utilities.Utilities;

/**
 * @author Peter Kelly
 *
 */
@Controller
public class RecipeController {

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
	
	@GetMapping("/new_filter")
	public String addFilter(Model model) {
		Filter newFilter = new Filter();
		model.addAttribute("newFilter", newFilter);
		model.addAttribute("typeList", utilities.getTypes());
		model.addAttribute("cuisineList", utilities.getCuisines());
		return "new_filter";
	}
	
	@PostMapping("savefilter")
	public String saveFilter(@Valid @ModelAttribute("newFilter") Filter newFilter, @AuthenticationPrincipal MyUserDetails user, BindingResult bind) {
		filterService.save(newFilter);
		User u = userService.findByUsername(user.getUsername());
		u.getFilters().add(newFilter);
		userService.save(u);
		return "redirect:/user_filters";
	}
	
	@GetMapping("/remove_filter/{id}")
	public String removeFilter(@PathVariable int id, @AuthenticationPrincipal MyUserDetails user, Model model) {

		Filter filter = filterService.findById(id);
		User u = userService.findByUsername(user.getUsername());

		u.removeFilter(filter.getId());


		userService.save(u);
		
		try {
			filterService.deleteById(filter.getId());
		} catch (FilterDeletionException e) {
			e.printStackTrace();
		}

		
		return "redirect:/user_filters";
	}
	
	@GetMapping("/user_filters")
	public String showUserFilters(@AuthenticationPrincipal MyUserDetails user, Model model) {
		User u = userService.findByUsername(user.getUsername());
		model.addAttribute("user", u);
		return "user_filters";
	}
	
	@GetMapping("/view_filter/{id}")
	public String postShowFilter(@PathVariable int id, @AuthenticationPrincipal MyUserDetails user, Model model) {
		User u = userService.findByUsername(user.getUsername());
		Filter f = filterService.findById(id);
		
		model.addAttribute("user", u);
		model.addAttribute("filter", f);
		
		return "view_filter";
	}
	
	@GetMapping("/edit_filter/{id}")
	public String editFilter(@PathVariable int id, @AuthenticationPrincipal MyUserDetails user, Model model) {

		Filter filter = filterService.findById(id);
		editId = id;
		// container for new user
		model.addAttribute("newFilter", filter);
		model.addAttribute("typeList", utilities.getTypes());
		model.addAttribute("cuisineList", utilities.getCuisines());
		return "edit_filter";
	}
	
	@PostMapping("editFilter")
	public String saveFilterRecipe(@Valid @ModelAttribute("newFilter") Filter newFilter, @AuthenticationPrincipal MyUserDetails user, BindingResult bind) {
		
		Filter filter = filterService.findById(editId);
		filter.setCuisine(newFilter.getCuisine());
		filter.setFavorite(newFilter.isFavorite());
		filter.setMinutes(newFilter.getMinutes());
		filter.setName(newFilter.getName());
		filter.setType(newFilter.getType());
		filterService.save(filter);

		return "redirect:/user_filters";
	}
	
	
	
	

}