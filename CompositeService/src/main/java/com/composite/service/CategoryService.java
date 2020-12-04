package com.composite.service;

import java.util.List;

public interface CategoryService {
	
	List<Category> getCategories();

	Category getCategory(Integer id);

}
