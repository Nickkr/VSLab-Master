package com.core.categoryservice;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private final CategoryRepository repository;

	public CategoryController(CategoryRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Category> getCategories() {
		return repository.findAll();
	}

	@PostMapping
	public ResponseEntity<?> createCategory(@RequestBody Category newCategory) {
		Category createdCategory = repository.save(newCategory);
		return ResponseEntity.created(null).body(createdCategory);
	}

	@GetMapping("{id}")
	public Category getCategory(@PathVariable Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException(id));
	}

	@PutMapping("{id}")
	public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody Category newCategory) {
		Category updatedCategory = repository.findById(id)
				.map(Category -> {
					Category.setName(newCategory.getName());
					return repository.save(Category);
				})
				.orElseThrow(() -> new CategoryNotFoundException(id));

		return ResponseEntity.created(null).body(updatedCategory);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
		repository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}

}
