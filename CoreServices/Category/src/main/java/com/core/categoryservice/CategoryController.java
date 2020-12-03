package com.core.categoryservice;

import java.util.List;

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

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<Category>> getCategories() {
		List<Category> allCategories = service.getCategories();
		return ResponseEntity.ok(allCategories);
	}

	@GetMapping(params = "searchName")
	public ResponseEntity<List<Category>> getFilteredCategories(@RequestParam String searchName) {
		List<Category> filteredCategories = service.getFilteredCategories(searchName);
		return ResponseEntity.ok(filteredCategories);
	}

	@PostMapping
	public ResponseEntity<Category> createCategory(@RequestBody Category newCategory) {
		Category createdCategory = service.createCategory(newCategory);
		return ResponseEntity.created(null).body(createdCategory);
	}

	@GetMapping("{id}")
	public ResponseEntity<Category> getCategory(@PathVariable Integer id) {
		Category category = service.getCategory(id);
		return ResponseEntity.ok(category);
	}

	@PutMapping("{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody Category newCategory) {
		Category updatedCategory = service.updateCategory(id, newCategory);
		return ResponseEntity.created(null).body(updatedCategory);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
		service.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}

}
