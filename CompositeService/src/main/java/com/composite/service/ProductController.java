package com.composite.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;

@RestController
public class ProductController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;

	private final Map<Integer, ProductComposite> productCache = new LinkedHashMap<Integer, ProductComposite>();

	public static String PRODUCT_BASE_URL = "http://product-service/products";

	@HystrixCommand(fallbackMethod = "getProductsCache")
	@GetMapping("/products")
	Product[] getProducts(@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String searchText) {

		Product[] products = restTemplate.getForObject(PRODUCT_BASE_URL + "?minPrice={minPrice}&maxPrice={maxPrice}&categoryId={categoryId}&searchText={searchText}", Product[].class,
				minPrice,
				maxPrice,
				categoryId,
				searchText);

		// Get all categories.


		//Iterate over products and put into cache if absent
		for (Product product : products) {
			String category = "";
			this.productCache.putIfAbsent(product.getId().intValue(), new ProductComposite(product, category));
		}
		return products;
	}

	@HystrixCommand(fallbackMethod = "getProductCache")
	@GetMapping("/products/{id}")
	Product getProductById(@PathVariable int id) {
		Product product = restTemplate.getForObject(PRODUCT_BASE_URL + "/{id}", Product.class, id);

		//replace categoryId with categoryName
		String category = "";
		productCache.putIfAbsent(id, new ProductComposite(product, category));
		return product;
	}


	@HystrixCommand
	ProductComposite getProductCache(int id) {
		return productCache.get(id);
	}

	@HystrixCommand
	List<ProductComposite> getProductsCache() {
		return new ArrayList<ProductComposite>(this.productCache.values());
	}

	@HystrixCommand()
	@PostMapping("/products")
	Product createNewProduct(@RequestBody String newProduct) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(newProduct, headers);
		Product product = restTemplate.postForObject(PRODUCT_BASE_URL, request, Product.class);
		if(product != null) {
			String category = "";
			this.productCache.put(product.getId().intValue(), new ProductComposite(product, category));
		}
		return product;

	}

	@HystrixCommand()
	@DeleteMapping("/products/{id}")
	ResponseEntity<String> deleteProductById(@PathVariable int id) {
		ResponseEntity<String> response = restTemplate.exchange(PRODUCT_BASE_URL + "/{id}", HttpMethod.DELETE, null, String.class, id);
		if(response.getStatusCode() == HttpStatus.OK) {
			this.productCache.remove(id);
		}
		return response;
	}

	@HystrixCommand()
	@PutMapping("/products/{id}")
	ResponseEntity<Product> updateProductById(@PathVariable int id, @RequestBody String product) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Product> response = restTemplate.exchange(PRODUCT_BASE_URL + "/{id}", HttpMethod.PUT, new HttpEntity<String>(product, headers), Product.class, id);
		if(response.getStatusCode() == HttpStatus.OK) {
			String category = "";
			this.productCache.replace(id, new ProductComposite(response.getBody(), category));
		}
		return response;
	}
}
