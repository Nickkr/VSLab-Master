package com.composite.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

@Service
public class CategoryDelegateService implements CategoryDelegateInterface {

	private static final String CATEGORY_BASE_URL = "http://category-service/categories";
	private static final String PRODUCT_BASE_URL = "http://product-service/products";

	@Autowired
	private CategoryCache cache;

	@Autowired
	@LoadBalanced
	private WebClient.Builder webClientBuilder;

	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	public ResponseEntity<Category[]> getCategories() {
		return restTemplate.getForEntity(CATEGORY_BASE_URL, Category[].class);
	}

	public ResponseEntity<Category[]> getCachedCategories() {
		Category[] body = new ArrayList<Category>(cache.values());
		return ResponseEntity.ok(body);
	}

	public ResponseEntity<Category[]> getFilteredCategories(String searchName) {
		return restTemplate.getForEntity(CATEGORY_BASE_URL + "?searchName={id}", Category[].class, searchName);
	}

	public ResponseEntity<Category[]> getCachedFilteredCategories(String searchName) {
		// Includes a category if its name contains the search name case insensitive.
		Predicate<Category> matcher = category -> {
			return category.getName().toLowerCase().contains(searchName.toLowerCase());
		};

		Category[] body = cache.values().stream().filter(matcher).toArray(Category[]::new);
		return ResponseEntity.ok(body);
	}

	public ResponseEntity<Category> createCategory(Category newCategory) {
		return restTemplate.postForEntity(CATEGORY_BASE_URL, newCategory, Category.class);
	}

	public ResponseEntity<Category> getCategory(Integer id) {
		return restTemplate.getForEntity(CATEGORY_BASE_URL + "/{id}", Category.class, id);
	}

	public ResponseEntity<Category> getCachedCategory(Integer id) {
		Category body = cache.get(id);
		return ResponseEntity.ok(body);
	}

	public ResponseEntity<Category> updateCategory(Integer id, Category newCategory) {
		return restTemplate.exchange(CATEGORY_BASE_URL + "/{id}", HttpMethod.PUT, new HttpEntity<Category>(newCategory), Category.class, id);
	}

	public ResponseEntity<Void> deleteCategory(Integer id) {

		final WebClient categoryClient = webClientBuilder.baseUrl(CATEGORY_BASE_URL).build();
		final WebClient productClient = webClientBuilder.baseUrl(PRODUCT_BASE_URL).build();

		// Return failure if the category does not exists.
		if (getCategory(id).getStatusCode() != HttpStatus.OK) {
			return ResponseEntity.notFound().build();
		}

		// Get all product for the given category id.
		Product[] products = productClient.get().uri("?categoryId={id}", id).retrieve().bodyToMono(Product[].class).block();

		// Function call to delete a single product.
		Function<Product, Mono<ResponseEntity<Void>>> deleteProductByID = (product) -> {
			return productClient.delete().uri("/{id}", product.getId()).retrieve().toBodilessEntity();
		};

		// Delete all products by its id and throw exceptions if an error occurs.
		ParallelFlux<Product> parallel = Flux.fromArray(products).parallel().runOn(Schedulers.elastic());
		parallel.flatMap(deleteProductByID, true).sequential().collectList().block();

		// Delete the category after all products were deleted and throw exceptions if an error occurs.
		categoryClient.delete().uri("/{id}", id).retrieve().toBodilessEntity().block();

		// When no error occurred return success.
		return ResponseEntity.noContent().build();
	}

}
