package com.perscholas.recipeApp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perscholas.recipeApp.dao.FilterRepoIF;
import com.perscholas.recipeApp.models.Filter;
import com.perscholas.recipeApp.utilities.FilterDeletionException;

@Service
public class FilterService {
	
	@Autowired
	FilterRepoIF filterRepo;
	
	public List<Filter> findAll() {
		return filterRepo.findAll();
	}
	
	public Filter findById(Integer id) {
		return filterRepo.findById(id).get();
	}
	
	public boolean existsById(Integer id) {
		return filterRepo.existsById(id);
	}
	
	public void deleteById(Integer id) throws FilterDeletionException {

		if (existsById(id)) {
				filterRepo.deleteById(id);
				if (existsById(id)) {
					throw new FilterDeletionException("Failed Deletion of Filter : " + id);
				}
		}

	}
	
	public void save(Filter f) {
		filterRepo.save(f);
	}
	
	public Filter findByName(String name) {
		return filterRepo.findByName(name);
	}

}
