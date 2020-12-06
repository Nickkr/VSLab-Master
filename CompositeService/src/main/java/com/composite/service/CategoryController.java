package com.composite.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/categories")
public class CategoryController implements CategoryDelegateInterface {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Deprecated
	private static final Category cachedCategory = new Category(0, "Cached category!");

	@Deprecated
	private List<Category> cache = List.of(cachedCategory);
	
	@Autowired
	private CategoryDelegateService categoryService;

	@HystrixCommand(fallbackMethod = "getCategoriesFallback")
	@GetMapping
	public ResponseEntity<Category[]> getCategories() {
		return categoryService.getCategories();
	}

	@HystrixCommand
	public ResponseEntity<Category[]> getCategoriesFallback(Throwable throwable) {
		logger.info(throwable.getLocalizedMessage());
		return getCachedCategories();
	}

	public ResponseEntity<Category[]> getCachedCategories() {
		return categoryService.getCachedCategories();
	}

	@HystrixCommand(fallbackMethod = "getFilteredCategoriesFallback")
	@GetMapping(params = "searchName")
	public ResponseEntity<Category[]> getFilteredCategories(@RequestParam String searchName) {
		return categoryService.getFilteredCategories(searchName);
	}

	@HystrixCommand
	public ResponseEntity<Category[]> getFilteredCategoriesFallback(String searchName, Throwable throwable) {
		logger.info(throwable.getLocalizedMessage());
		return getCachedFilteredCategories(searchName);
	}

	public ResponseEntity<Category[]> getCachedFilteredCategories(String searchName) {
		return categoryService.getCachedFilteredCategories(searchName);
	}

	@HystrixCommand
	@PostMapping
	public ResponseEntity<Category> createCategory(@RequestBody Category newCategory) {
		return categoryService.createCategory(newCategory);
	}

	@HystrixCommand(fallbackMethod = "getCategoryFallback")
	@GetMapping("{id}")
	public ResponseEntity<Category> getCategory(@PathVariable Integer id) {
		return categoryService.getCategory(id);
	}

	@HystrixCommand
	public ResponseEntity<Category> getCategoryFallback(@PathVariable Integer id, Throwable throwable) {
		logger.info(throwable.getLocalizedMessage());
		return getCachedCategory(id);
	}

	public ResponseEntity<Category> getCachedCategory(Integer id) {
		return categoryService.getCachedCategory(id);
	}

	@HystrixCommand
	@PutMapping("{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody Category newCategory) {
		return categoryService.updateCategory(id, newCategory);
	}

	@HystrixCommand
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
		return categoryService.deleteCategory(id);
	}

}
