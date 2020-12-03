package com.composite.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface CategoryDelegateInterface {

	@SuppressWarnings("rawtypes")
	ResponseEntity<List> getCategories();

	@SuppressWarnings("rawtypes")
	ResponseEntity<List> getCachedCategories();

	@SuppressWarnings("rawtypes")
	ResponseEntity<List> getFilteredCategories(String searchName);

	@SuppressWarnings("rawtypes")
	ResponseEntity<List> getCachedFilteredCategories(String searchName);

	ResponseEntity<Category> createCategory(Category newCategory);

	ResponseEntity<Category> getCategory(Integer id);

	ResponseEntity<Category> getCachedCategory(Integer id);

	ResponseEntity<Category> updateCategory(Integer id, Category newCategory);

	ResponseEntity<Void> deleteCategory(Integer id);

}