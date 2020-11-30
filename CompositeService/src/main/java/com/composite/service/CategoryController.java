package com.composite.service;

import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private static final String CATEGORY_BASE_URL = "http://category-service/categories";
	private static final String PRODUCT_BASE_URL = "http://product-service/products";

	@Autowired
	@LoadBalanced
	public WebClient.Builder webClientBuilder;

	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;

	@SuppressWarnings("rawtypes")
	@HystrixCommand
	@GetMapping
	public ResponseEntity<List> getCategories() {
		return restTemplate.getForEntity(CATEGORY_BASE_URL, List.class);
	}

	@SuppressWarnings("rawtypes")
	@HystrixCommand
	@GetMapping(params = "searchName")
	public ResponseEntity<List> getFilteredCategories(@RequestParam String searchName) {
		return restTemplate.getForEntity(CATEGORY_BASE_URL + "?searchName={id}", List.class, searchName);
	}

	@HystrixCommand
	@PostMapping
	public ResponseEntity<Category> createCategory(@RequestBody Category newCategory) {
		return restTemplate.postForEntity(CATEGORY_BASE_URL, newCategory, Category.class);
	}

	@HystrixCommand
	@GetMapping("{id}")
	public ResponseEntity<Category> getCategory(@PathVariable Integer id) {
		return restTemplate.getForEntity(CATEGORY_BASE_URL + "/{id}", Category.class, id);
	}

	@HystrixCommand
	@PutMapping("{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Integer id, @RequestBody Category newCategory) {
		return restTemplate.exchange(CATEGORY_BASE_URL + "/{id}", HttpMethod.PUT, new HttpEntity<Category>(newCategory), Category.class, id);
	}

	@HystrixCommand
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {

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
