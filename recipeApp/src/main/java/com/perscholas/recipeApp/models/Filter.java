package com.perscholas.recipeApp.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

/**
 * @author Peter Kelly
 *
 */
@Entity
public class Filter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	@Range(min=0, max= 500000, message="Minutes must be between {2} and {1}")
	private int minutes;
	@ElementCollection(targetClass=String.class)
	@Column(length=8000)
	private List<String> include;
	@ElementCollection(targetClass=String.class)
	@Column(length=8000)
	private List<String> exclude;
	private String type;
	private String cuisine;
	private boolean favorite;
	
	public Filter() {
		this.include = new ArrayList<String>();
		this.exclude = new ArrayList<String>();
	}
	
	public Filter(String name, int minutes, List<String> include, List<String> exclude, String type, String cuisine, boolean favorite) {
		super();
		this.name = name;
		this.minutes = minutes;
		this.include = include;
		this.exclude = exclude;
		this.type = type;
		this.cuisine = cuisine;
		this.favorite = favorite;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public List<String> getInclude() {
		return include;
	}
	public void setInclude(List<String> include) {
		this.include = include;
	}
	public List<String> getExclude() {
		return exclude;
	}
	public void setExclude(List<String> exclude) {
		this.exclude = exclude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cuisine == null) ? 0 : cuisine.hashCode());
		result = prime * result + (favorite ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + minutes;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Filter other = (Filter) obj;
		if (cuisine == null) {
			if (other.cuisine != null)
				return false;
		} else if (!cuisine.equals(other.cuisine))
			return false;
		if (favorite != other.favorite)
			return false;
		if (id != other.id)
			return false;
		if (minutes != other.minutes)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	

}
