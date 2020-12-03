package com.core.categoryservice;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface CategoryInterface {

	ResponseEntity<List<Category>> getCategories();

	ResponseEntity<List<Category>> getFilteredCategories(String searchName);

	ResponseEntity<Category> createCategory(Category newCategory);

	ResponseEntity<Category> getCategory(Integer id);

	ResponseEntity<Category> updateCategory(Integer id, Category newCategory);

	ResponseEntity<Void> deleteCategory(Integer id);

}