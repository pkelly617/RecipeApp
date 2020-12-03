package com.perscholas.recipeApp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perscholas.recipeApp.dao.UserRepoIF;
import com.perscholas.recipeApp.models.Recipe;
import com.perscholas.recipeApp.models.User;

@Service
public class UserService {
	
	@Autowired
	UserRepoIF userRepo;
	
	public List<User> findAll() {

		return userRepo.findAll();
	}
	
	public User findById(Integer id) {
		return userRepo.findById(id).get();
	}
	
	public boolean existsById(Integer id) {
		return userRepo.existsById(id);
	}
	
	public void deleteById(Integer id) {

		if (existsById(id))
			userRepo.deleteById(id);

	}
	
	public void save(User u) {
		userRepo.save(u);
	}
	
	public List<User> findByUsernameOrEmail(String username, String email) {
		return userRepo.findByUsernameOrEmail(username, email);
	}
	
	public User findByUsername(String userName) {
		return userRepo.findByUsername(userName).get();
	}

}
