package com.composite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.core.categoryservice.Category;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	@LoadBalanced
	public WebClient.Builder webClientBuilder;

	private static final String CATEGORY_BASE_URL = "http://category-service/categories";
	private static final String PRODUCT_BASE_URL = "http://product-service/products";

	@GetMapping("/test")
	public	String test() {
		WebClient categoryClient = webClientBuilder.baseUrl(CATEGORY_BASE_URL).build();
		WebClient productClient = webClientBuilder.baseUrl(PRODUCT_BASE_URL).build();

		ResponseEntity<Void> categoryGetResponseEntity = categoryClient.get().retrieve().toBodilessEntity().block();
		ResponseEntity<Void> productGetResponseEntity = productClient.get().retrieve().toBodilessEntity().block();
		return "Call result code: " + categoryGetResponseEntity.getStatusCodeValue() + " and " + productGetResponseEntity.getStatusCodeValue();
	}

	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;

	// TODO Temporary for tests.
	@GetMapping("/products")
	ResponseEntity<?> getProducts() {
		return restTemplate.getForEntity("http://CATEGORY-SERVICE/categories/", Object.class);
	}

	// TODO Temporary for tests.
	@GetMapping("/local")
	ResponseEntity<?> getProducts2() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForEntity("http://localhost:18082/categories/", Object.class);
	}

	@GetMapping
	public ResponseEntity<?> getCategories() {

		return ResponseEntity.ok().build();
//		List<Category> allCategories = repository.findAll();
//		return ResponseEntity.ok(allCategories);
	}

	
}
