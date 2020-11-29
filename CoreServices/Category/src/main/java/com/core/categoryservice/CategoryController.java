package com.core.categoryservice;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

	private final CategoryRepository repository;

	public CategoryController(CategoryRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public ResponseEntity<List<Category>> getCategories() {
		List<Category> allCategories = repository.findAll();
		return ResponseEntity.ok(allCategories);
	}

	@GetMapping(params = "searchName")
	public ResponseEntity<List<Category>> getFilteredCategories(@RequestParam String searchName) {
		ExampleMatcher matcher = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withIgnorePaths("id");

		Example<Category> example = Example.of(new Category(searchName), matcher);
		
		List<Category> filteredCategories = repository.findAll(example);
		return ResponseEntity.ok(filteredCategories);
	}

	@PostMapping
	public ResponseEntity<Category> createCategory(@RequestBody Category newCategory) {
		Category createdCategory = repository.save(newCategory);
		return ResponseEntity.created(null).body(createdCategory);
	}

	@GetMapping("{id}")
	public ResponseEntity<Category> getCategory(@PathVariable Integer id) {
		Category category = repository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException(id));

		return ResponseEntity.ok(category);
	}

	@PutMapping("{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody Category newCategory) {
		Category updatedCategory = repository.findById(id)
				.map(Category -> {
					Category.setName(newCategory.getName());
					return repository.save(Category);
				})
				.orElseThrow(() -> new CategoryNotFoundException(id));

		return ResponseEntity.created(null).body(updatedCategory);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
		repository.deleteById(id);

		return ResponseEntity.noContent().build();
	}

}
