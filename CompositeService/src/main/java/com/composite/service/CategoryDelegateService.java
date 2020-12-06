package com.composite.service;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

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
		final ResponseEntity<Category[]> entity = restTemplate.getForEntity(CATEGORY_BASE_URL, Category[].class);

		if (entity.getStatusCode() == HttpStatus.OK) {
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
			cache.initialize(entity.getBody());
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
		}

		return entity;
	}

	public ResponseEntity<Category[]> getCachedCategories() {
		final Category[] body = cache.values().toArray(Category[]::new);
		return ResponseEntity.ok(body);
	}

	public ResponseEntity<Category[]> getFilteredCategories(String searchName) {
		final ResponseEntity<Category[]> entity = restTemplate.getForEntity(CATEGORY_BASE_URL + "?searchName={id}", Category[].class, searchName);

		if (entity.getStatusCode() == HttpStatus.OK) {
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
			cache.putAll(entity.getBody());
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
		}

		return entity;
	}

	public ResponseEntity<Category[]> getCachedFilteredCategories(String searchName) {
		// Includes a category if its name contains the search name case insensitive.
		final Predicate<Category> matcher = category -> {
			return category.getName().toLowerCase().contains(searchName.toLowerCase());
		};

		final Category[] body = cache.values().stream().filter(matcher).toArray(Category[]::new);
		return ResponseEntity.ok(body);
	}

	public ResponseEntity<Category> createCategory(Category newCategory) {
		final ResponseEntity<Category> entity = restTemplate.postForEntity(CATEGORY_BASE_URL, newCategory, Category.class);

		if (entity.getStatusCode() == HttpStatus.CREATED) {
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
			cache.putIfAbsent(entity.getBody());
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
		}

		return entity;
	}

	public ResponseEntity<Category> getCategory(Integer id) {
		final ResponseEntity<Category> entity = restTemplate.getForEntity(CATEGORY_BASE_URL + "/{id}", Category.class, id);

		if (entity.getStatusCode() == HttpStatus.OK) {
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
			cache.put(entity.getBody());
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
		}

		return entity;
	}

	public ResponseEntity<Category> getCachedCategory(Integer id) {
		final Category body = cache.get(id);
		
		if (body != null) {
			return ResponseEntity.ok(body);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<Category> updateCategory(Integer id, Category newCategory) {
		final ResponseEntity<Category> entity = restTemplate.exchange(CATEGORY_BASE_URL + "/{id}", HttpMethod.PUT, new HttpEntity<Category>(newCategory), Category.class, id);

		if (entity.getStatusCode() == HttpStatus.CREATED) {
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
			cache.put(entity.getBody());
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
		}

		return entity;
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
		ResponseEntity<Void> entity = categoryClient.delete().uri("/{id}", id).retrieve().toBodilessEntity().block();

		// Remove deleted category from cache.
		if (entity.getStatusCode() == HttpStatus.NO_CONTENT) {
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
			cache.remove(id);
			System.err.println(Arrays.deepToString(cache.values().toArray(Category[]::new)));
		}

		// When no error occurred return success.
		return ResponseEntity.noContent().build();
	}

}
