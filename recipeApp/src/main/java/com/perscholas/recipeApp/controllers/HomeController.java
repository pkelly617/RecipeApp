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
public class HomeController {

	@Autowired
	UserService userService;
	@Autowired
	RecipeService recipeService;
	@Autowired
	FilterService filterService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

	@RequestMapping("/")
	public String index() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String showLogin(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "login";
	}

	@GetMapping("/register_user")
	public String adduser(Model modelUsers) {
		User newUser = new User();
		modelUsers.addAttribute("newUser", newUser);

		return "register_user";
	}

	@PostMapping("saveuser")
	public String saveUser(@Valid @ModelAttribute("newUser") User newUser, BindingResult bind) {
		Set<ConstraintViolation<User>> violations = validator.validate(newUser);
		if (!violations.isEmpty()) {
			for (ConstraintViolation<User> violation : violations) {
			    System.out.println(violation.getMessage());
			    return "redirect:/login";
			}
		}
		if (!userService.findByUsernameOrEmail(newUser.getUsername(), newUser.getEmail()).isEmpty()) {
			return "redirect:/login";
		}
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		userService.save(newUser);

		return "redirect:/";
	}

	

}