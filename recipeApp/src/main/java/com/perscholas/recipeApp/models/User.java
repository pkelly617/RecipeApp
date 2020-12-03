package com.perscholas.recipeApp.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@NamedEntityGraph(name = "User.recipes", 
attributeNodes = { @NamedAttributeNode(value = "recipes"),
				   @NamedAttributeNode(value = "filters")
})
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message = "Username is required!")
	private String username;
	@NotBlank(message = "Email is required!")
	private String email;
	@NotBlank(message = "Password is required!")
	private String password;
	@Column(columnDefinition = "varchar(255) default 'USER'")
	private String role;
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
               										CascadeType.MERGE
    })
	private Set<Recipe> recipes;
	@OneToMany
	private Set<Filter> filters;
	
	public User() {
		this.role = "USER";
		this.recipes = new HashSet<Recipe>();
		this.filters = new HashSet<Filter>();
	}
	
	public User(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Set<Recipe> getRecipes() {
		return recipes;
	}
	public void setRecipes(Set<Recipe> recipes) {
		this.recipes = recipes;
	}
	public void addRecipe(Recipe recipe) {
		recipes.add(recipe);
	}
	public Set<Filter> getFilters() {
		return filters;
	}
	public void setFilters(Set<Filter> filters) {
		this.filters = filters;
	}
	
	public boolean containsRecipe(int recipeId) {
		for (Recipe recipe : recipes) {
			if (((Integer) recipeId).equals(recipe.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean removeRecipe(int recipeId) {
		for (Recipe recipe : recipes) {
			if (((Integer) recipeId).equals(recipe.getId())) {
				recipes.remove(recipe);
				return true;
			}
		}
		return false;
	}
	
	public boolean removeFilter(int filterId) {
		for (Filter filter : filters) {
			if (((Integer) filterId).equals(filter.getId())) {
				filters.remove(filter);
				return true;
			}
		}
		return false;
	}
	
	

}
