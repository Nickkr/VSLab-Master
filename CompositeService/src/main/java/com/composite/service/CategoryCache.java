package com.composite.service;

import java.util.List;
import java.util.TreeMap;

import org.springframework.stereotype.Repository;

@Repository
public class CategoryCache extends TreeMap<Integer, Category> {
	
	private static final long serialVersionUID = -2060332784605672379L;

	public static CategoryCache getDefaultData() {
		CategoryCache cache = new CategoryCache();
		List<Category> data = List.of(new Category(1, "Obst"), new Category(2, "Gemüse"));

		for (Category category : data) {
			cache.putIfAbsent(category.getId(), category);
		}
		
		return cache;
	}

	public CategoryCache() {
		super();
	}
	
	
	
}
