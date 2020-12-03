package com.composite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ProductController {

	@Autowired
	CategoryCacheService categoryService;

	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;

	private final Map<Integer, Product> productCache = new LinkedHashMap<Integer, Product>();

	public static String PRODUCT_BASE_URL = "http://product-service/products";

	@HystrixCommand()
	@GetMapping("/products")
	Product[] getProducts(@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String searchText) {
		return restTemplate.getForObject(PRODUCT_BASE_URL + "?minPrice={minPrice}&maxPrice={maxPrice}&categoryId={categoryId}&searchText={searchText}", Product[].class,
				minPrice,
				maxPrice,
				categoryId,
				searchText);
	}

	@HystrixCommand()
	@PostMapping("/products")
	Product createNewProduct(@RequestBody String newProduct) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(newProduct, headers);
		return restTemplate.postForObject(PRODUCT_BASE_URL, request, Product.class);
	}

	@HystrixCommand(fallbackMethod = "getProductCache")
	@GetMapping("/products/{id}")
	Product getProductById(@PathVariable int id) {
		Product product = restTemplate.getForObject(PRODUCT_BASE_URL + "/{id}", Product.class, id);
		productCache.putIfAbsent(id, product);
		return product;
	}

	Product getProductCache(int id) {
		return productCache.get(id);
	}

	@HystrixCommand()
	@DeleteMapping("/products/{id}")
	void deleteProductById(@PathVariable int id) {
		restTemplate.delete(PRODUCT_BASE_URL + "/{id}", id);
	}

	@HystrixCommand()
	@PutMapping("/products/{id}")
	ResponseEntity<Product> updateProductById(@PathVariable int id, @RequestBody String product) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return restTemplate.exchange(PRODUCT_BASE_URL + "/{id}", HttpMethod.PUT, new HttpEntity<String>(product, headers), Product.class, id);
	}
}
