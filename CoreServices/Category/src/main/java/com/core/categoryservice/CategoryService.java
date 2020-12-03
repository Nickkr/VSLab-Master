package com.core.categoryservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public List<Category> getCategories() {
		return repository.findAll();
	}

	public List<Category> getFilteredCategories(String searchName) {
		ExampleMatcher matcher = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withIgnorePaths("id");
	
		Example<Category> example = Example.of(new Category(searchName), matcher);
	
		return repository.findAll(example);
	}

	public Category createCategory(Category newCategory) {
		if (newCategory == null || newCategory.getName() == null || newCategory.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("The category name is required and cannot be empty or whitespace.");
		}
	
		newCategory.setName(newCategory.getName().trim());

		// TODO Check if the new category name already exists in the database.
		
		return repository.save(newCategory);
	}

	public Category getCategory(Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException(id));
	}

	public Category updateCategory(Integer id, Category newCategory) {
		if (newCategory == null || newCategory.getName() == null || newCategory.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("The category name is required and cannot be empty or whitespace.");
		}
	
		Category updatedCategory = repository.findById(id)
				.map(category -> {
					category.setName(newCategory.getName());
					return category;
				})
				.orElseThrow(() -> new CategoryNotFoundException(id));

		// TODO Check if the new category name already exists in the database.

		return repository.save(updatedCategory);
	}

	public void deleteCategory(Integer id) {
		repository.deleteById(id);
	}

}