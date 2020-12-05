package com.core.categoryservice;

import java.util.List;

public interface CategoryInterface {

	List<Category> getCategories();

	List<Category> getFilteredCategories(String searchName);

	Category createCategory(Category newCategory);

	Category getCategory(Integer id);

	Category updateCategory(Integer id, Category newCategory);

	void deleteCategory(Integer id);

}