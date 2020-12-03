package com.perscholas.recipeApp.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;


/**
 * @author Peter Kelly
 *
 */
@Entity
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String title;
	@Range(min=0, max= 500000, message="Minutes must be between {2} and {1}")
	private int minutes;
	@ElementCollection(targetClass=String.class)
	@Column(length=8000)
	private List<String> ingredients;
	@ElementCollection(targetClass=String.class)
	@Column(length=8000)
	private List<String> instructions;
	private String type;
	private String cuisine;
	private boolean favorite;
	private boolean secret;
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
												   CascadeType.MERGE,
												   CascadeType.REMOVE
	}, mappedBy = "recipes")
	private Set<User> users;
	@Transient
	private int remove;
	
	public Recipe() {
		this.favorite = false;
		this.secret = false;
		this.ingredients = new ArrayList<String>();
		this.instructions = new ArrayList<String>();
		this.users = new HashSet<User>();
	}
	
	public Recipe(int id, String title, int minutes, List<String> ingredients, List<String> instructions, String type,
			String cuisine, boolean favorite, boolean secret) {
		super();
		this.id = id;
		this.title = title;
		this.minutes = minutes;
		this.ingredients = ingredients;
		this.instructions = instructions;
		this.type = type;
		this.cuisine = cuisine;
		this.favorite = favorite;
		this.secret = secret;
	}
	
	public Recipe(String title, int minutes, List<String> ingredients, List<String> instructions, String type,
			String cuisine, boolean favorite, boolean secret) {
		super();
		this.title = title;
		this.minutes = minutes;
		this.ingredients = ingredients;
		this.instructions = instructions;
		this.type = type;
		this.cuisine = cuisine;
		this.favorite = favorite;
		this.secret = secret;
	}
	
	public Recipe(Recipe recipe) {
		this.title = recipe.getTitle();
		this.minutes = recipe.getMinutes();
		this.ingredients = recipe.getIngredients();
		this.instructions = recipe.getInstructions();
		this.type = recipe.getType();
		this.cuisine = recipe.getCuisine();
		this.favorite = recipe.isFavorite();
		this.secret = recipe.isSecret();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public List<String> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}
	public boolean removeIngredient(String ingredient) {
		return this.ingredients.remove(ingredient);
	}
	public List<String> getInstructions() {
		return instructions;
	}
	public void setInstructions(List<String> instructions) {
		this.instructions = instructions;
	}
	public boolean removeInstruction(String step) {
		return this.instructions.remove(step);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCuisine() {
		return cuisine;
	}
	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public boolean isSecret() {
		return secret;
	}
	public void setSecret(boolean secret) {
		this.secret = secret;
	}
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public void addUser(User user) {
		users.add(user);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns true if the user is found in this recipe's user
	 * list. This method takes a user's id and iterates over all
	 * of the users belonging to this recipe and if the user id
	 * is found return true; otherwise return false.
	 * 
	 * @param userId  the id of a user to be searched for
	 * @return a boolean value indicating whether the desired user is
	 * 		   in the users list (which also indicates that this recipe
	 * 		   is in the user's recipe list)
	 */
	public boolean containsUser(int userId) {
		for (User user : users) {
			if (((Integer) userId).equals(user.getId())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the userId is found and the user is deleted;
	 * otherwise it returns false. This method iterates over each user
	 * and if the id matches with the parameter id, we remove the user
	 * from the set.
	 * 
	 * @param userId  the id of a user to be deleted
	 * @return a boolean value indicating whether the user was 
	 * 		   deleted or not
	 */
	public boolean removeUser(int userId) {
		
		for (User user : users) {
			if (((Integer) userId).equals(user.getId())) {
				users.remove(user);
				return true;
			}
		}
		return false;
		
	}

	@Override
	public String toString() {
		return "Recipe [title=" + title + ", minutes=" + minutes + ", type=" + type + ", cuisine=" + cuisine + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cuisine == null) ? 0 : cuisine.hashCode());
		result = prime * result + (favorite ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + ((ingredients == null) ? 0 : ingredients.hashCode());
		result = prime * result + ((instructions == null) ? 0 : instructions.hashCode());
		result = prime * result + minutes;
		result = prime * result + remove;
		result = prime * result + (secret ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		if (cuisine == null) {
			if (other.cuisine != null)
				return false;
		} else if (!cuisine.equals(other.cuisine))
			return false;
		if (favorite != other.favorite)
			return false;
		if (id != other.id)
			return false;
		if (ingredients == null) {
			if (other.ingredients != null)
				return false;
		} else if (!ingredients.equals(other.ingredients))
			return false;
		if (instructions == null) {
			if (other.instructions != null)
				return false;
		} else if (!instructions.equals(other.instructions))
			return false;
		if (minutes != other.minutes)
			return false;
		if (remove != other.remove)
			return false;
		if (secret != other.secret)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}
	
	
	

}
