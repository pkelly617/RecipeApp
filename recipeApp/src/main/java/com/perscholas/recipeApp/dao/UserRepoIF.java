package com.perscholas.recipeApp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.perscholas.recipeApp.models.Recipe;
import com.perscholas.recipeApp.models.User;

public interface UserRepoIF extends JpaRepository<User, Integer> {
	@EntityGraph(value = "User.recipes")
	Optional<User> findByUsername(String userName);
	@EntityGraph(value = "User.filters")
	Optional<User> findByEmail(String email);
	List<User> findByUsernameOrEmail(String username, String email);
}
