package com.composite.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	public List<Category> getCategories() {
		return List.of(new Category(1, "Obst"), new Category(1, "Gemüse"));
	};

}
