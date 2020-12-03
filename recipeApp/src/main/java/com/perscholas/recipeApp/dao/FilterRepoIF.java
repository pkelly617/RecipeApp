package com.perscholas.recipeApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.perscholas.recipeApp.models.Filter;

public interface FilterRepoIF extends JpaRepository<Filter, Integer> {
	Filter findByName(String name);
}
