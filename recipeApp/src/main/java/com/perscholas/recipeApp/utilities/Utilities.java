package com.perscholas.recipeApp.utilities;

import java.util.ArrayList;
import java.util.List;

import com.perscholas.recipeApp.models.Recipe;

public class Utilities {
	
	List<String> cuisines;
	List<String> types;
	
	public Utilities() {
		this.cuisines = cuisineFill();
		this.types = typeFill();
	}
	
	public Utilities(List<String> cuisines, List<String> types) {
		super();
		this.cuisines = cuisines;
		this.types = types;
	}
	
	public List<String> getCuisines() {
		return cuisines;
	}

	public void setCuisines(List<String> cuisines) {
		this.cuisines = cuisines;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	private ArrayList<String> cuisineFill() {
		ArrayList<String> filler = new ArrayList<String>(16);
		filler.add("African");
		filler.add("American");
		filler.add("British");
		filler.add("Cajun");
		filler.add("Chinese");
		filler.add("French");
		filler.add("German");
		filler.add("Indian");
		filler.add("Irish");
		filler.add("Italian");
		filler.add("Japanese");
		filler.add("Latin");
		filler.add("Mediterranean");
		filler.add("Mexican");
		filler.add("Thai");
		filler.add("None");
		return filler;
	}
	
	private ArrayList<String> typeFill() {
		ArrayList<String> filler = new ArrayList<String>(9);
		filler.add("Appetizer");
		filler.add("Breakfast");
		filler.add("Dessert");
		filler.add("Dinner");
		filler.add("Drink");
		filler.add("Lunch");
		filler.add("Sauce");
		filler.add("Snack");
		filler.add("None");
		return filler;
	}
	

}
