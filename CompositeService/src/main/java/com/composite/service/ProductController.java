package com.composite.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ProductController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;

	private final Map<Integer, ProductComposite> productCache = new LinkedHashMap<Integer, ProductComposite>();

	public static String PRODUCT_BASE_URL = "http://product-service/products";

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@HystrixCommand(fallbackMethod = "getProductsCache")
	@GetMapping("/products")
	List<ProductComposite> getProducts(@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String searchText) {

		Product[] products = restTemplate.getForObject(PRODUCT_BASE_URL + "?minPrice={minPrice}&maxPrice={maxPrice}&categoryId={categoryId}&searchText={searchText}", Product[].class,
				minPrice,
				maxPrice,
				categoryId,
				searchText);

		List<ProductComposite> tmpList = new ArrayList<ProductComposite>();
		//Iterate over products and put into cache if absent
		for (Product product : products) {
			ProductComposite productComposite =  new ProductComposite(product, categoryService.getCategoryById(product.getCategoryId()));			
			tmpList.add(productComposite);
			this.productCache.putIfAbsent(product.getId().intValue(), productComposite);
		}
		return tmpList;
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@HystrixCommand(fallbackMethod = "getProductCache")
	@GetMapping("/products/{id}")
	ProductComposite getProductById(@PathVariable int id) {
		Product product = restTemplate.getForObject(PRODUCT_BASE_URL + "/{id}", Product.class, id);
		productCache.putIfAbsent(id, new ProductComposite(product, this.categoryService.getCategoryById(product.getCategoryId())));
		return productCache.get(id);
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@HystrixCommand
	ProductComposite getProductCache(int id) {
		return productCache.get(id);
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@HystrixCommand
	List<ProductComposite> getProductsCache(Double minPrice, Double maxPrice, Integer categoryId, String details) {
		Predicate<ProductComposite> matcher = product -> {
			return (minPrice != null ? product.getPrice() >= minPrice : true) 
				&& (maxPrice != null ? product.getPrice() <= maxPrice : true)
				&& (categoryId != null ? product.getCategory().getId() == categoryId : true)
				&& (details != null ? product.getDetails().toLowerCase().contains(details.toLowerCase()) : true);
		};
		return this.productCache.values().stream().filter(matcher).collect(Collectors.toList());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@HystrixCommand()
	@PostMapping("/products")
	Product createNewProduct(@RequestBody String newProduct) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(newProduct, headers);
		Product product = restTemplate.postForObject(PRODUCT_BASE_URL, request, Product.class);
		if(product != null) {
			this.productCache.put(product.getId().intValue(), new ProductComposite(product, this.categoryService.getCategoryById(product.getCategoryId())));
		}
		return product;

	}

	@PreAuthorize("hasRole('ADMIN')")
	@HystrixCommand()
	@DeleteMapping("/products/{id}")
	ResponseEntity<String> deleteProductById(@PathVariable int id) {
		ResponseEntity<String> response = restTemplate.exchange(PRODUCT_BASE_URL + "/{id}", HttpMethod.DELETE, null, String.class, id);
		if(response.getStatusCode() == HttpStatus.OK) {
			this.productCache.remove(id);
		}
		return response;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@HystrixCommand()
	@PutMapping("/products/{id}")
	ResponseEntity<Product> updateProductById(@PathVariable int id, @RequestBody String product) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Product> response = restTemplate.exchange(PRODUCT_BASE_URL + "/{id}", HttpMethod.PUT, new HttpEntity<String>(product, headers), Product.class, id);
		if(response.getStatusCode() == HttpStatus.OK) {
			this.productCache.replace(id, new ProductComposite(response.getBody(), this.categoryService.getCategoryById(response.getBody().getCategoryId())));
		}
		return response;
	}
}
