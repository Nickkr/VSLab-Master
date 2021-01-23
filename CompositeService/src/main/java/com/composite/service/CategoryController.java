package com.composite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class CategoryController implements CategoryDelegate {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryDelegateService categoryService;

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@HystrixCommand(fallbackMethod = "getCategoriesFallback")
	@GetMapping
	public ResponseEntity<Category[]> getCategories() {
		return categoryService.getCategories();
	}

	@HystrixCommand
	public ResponseEntity<Category[]> getCategoriesFallback(Throwable throwable) {
		logger.info("Hystrix fallback called: {}", throwable.getLocalizedMessage());
		return getCachedCategories();
	}

	public ResponseEntity<Category[]> getCachedCategories() {
		return categoryService.getCachedCategories();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@HystrixCommand(fallbackMethod = "getFilteredCategoriesFallback")
	@GetMapping(params = "searchName")
	public ResponseEntity<Category[]> getFilteredCategories(@RequestParam String searchName) {
		return categoryService.getFilteredCategories(searchName);
	}

	@HystrixCommand
	public ResponseEntity<Category[]> getFilteredCategoriesFallback(String searchName, Throwable throwable) {
		logger.info("Hystrix fallback called: {}", throwable.getLocalizedMessage());
		return getCachedFilteredCategories(searchName);
	}

	public ResponseEntity<Category[]> getCachedFilteredCategories(String searchName) {
		return categoryService.getCachedFilteredCategories(searchName);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@HystrixCommand
	@PostMapping
	public ResponseEntity<Category> createCategory(@RequestBody Category newCategory) {
		return categoryService.createCategory(newCategory);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@HystrixCommand(fallbackMethod = "getCategoryFallback")
	@GetMapping("{id}")
	public ResponseEntity<Category> getCategory(@PathVariable Integer id) {
		return categoryService.getCategory(id);
	}

	@HystrixCommand
	public ResponseEntity<Category> getCategoryFallback(@PathVariable Integer id, Throwable throwable) {
		logger.info("Hystrix fallback called: {}", throwable.getLocalizedMessage());
		return getCachedCategory(id);
	}

	public ResponseEntity<Category> getCachedCategory(Integer id) {
		return categoryService.getCachedCategory(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@HystrixCommand
	@PutMapping("{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody Category newCategory) {
		return categoryService.updateCategory(id, newCategory);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@HystrixCommand
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
		return categoryService.deleteCategory(id);
	}

}
