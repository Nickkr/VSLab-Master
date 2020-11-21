package com.composite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private WebClient categoryClient = WebClient.create("http://localhost:18082/categories");

	@GetMapping("/test")
	String test() {
		ResponseEntity<Void> categoryDeleteResponseEntity = categoryClient.get().retrieve().toBodilessEntity().block();
		return "Call result code: " + categoryDeleteResponseEntity.getStatusCodeValue();
	}

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;
	
    // TODO Temporary for tests.
    @GetMapping("/products")
    ResponseEntity<?> getProducts() {
        return restTemplate.getForEntity("http://CATEGORY-SERVICE/categories/",Object.class);
    }
    
    // TODO Temporary for tests.
    @GetMapping("/local")
    ResponseEntity<?> getProducts2() {
    	RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity("http://localhost:18082/categories/",Object.class);
    }
	
}
