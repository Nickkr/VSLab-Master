package com.composite.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CategoryCacheService {

	private Map<Integer, Category> cache = new LinkedHashMap<Integer, Category>();

	public Map<Integer, Category> getCachedCategories() {
		return cache;
	}

	public CategoryCacheService() {
		// TODO Call the core service to retrieve the real data.
		List<Category> data = List.of(new Category(1, "Obst"), new Category(2, "Gemüse"));

		for (Category category : data) {
			cache.putIfAbsent(category.getId(), category);
		}
	}

}
