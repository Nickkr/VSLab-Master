package com.composite.service;

import org.springframework.http.ResponseEntity;

public interface CategoryDelegateInterface {

	ResponseEntity<Category[]> getCategories();

	ResponseEntity<Category[]> getCachedCategories();

	ResponseEntity<Category[]> getFilteredCategories(String searchName);

	ResponseEntity<Category[]> getCachedFilteredCategories(String searchName);

	ResponseEntity<Category> createCategory(Category newCategory);

	ResponseEntity<Category> getCategory(Integer id);

	ResponseEntity<Category> getCachedCategory(Integer id);

	ResponseEntity<Category> updateCategory(Integer id, Category newCategory);

	ResponseEntity<Void> deleteCategory(Integer id);

}