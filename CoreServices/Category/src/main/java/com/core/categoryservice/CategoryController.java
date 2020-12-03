package com.core.categoryservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController implements CategoryInterface {

	@Autowired
	private CategoryService service;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Category> getCategories() {
		return service.getCategories();
	}

	@GetMapping(params = "searchName")
	@ResponseStatus(HttpStatus.OK)
	public List<Category> getFilteredCategories(@RequestParam String searchName) {
		return service.getFilteredCategories(searchName);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Category createCategory(@RequestBody Category newCategory) {
		return service.createCategory(newCategory);
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public Category getCategory(@PathVariable Integer id) {
		return service.getCategory(id);
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Category updateCategory(@PathVariable Integer id, @RequestBody Category newCategory) {
		return service.updateCategory(id, newCategory);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable Integer id) {
		service.deleteCategory(id);
	}

}
