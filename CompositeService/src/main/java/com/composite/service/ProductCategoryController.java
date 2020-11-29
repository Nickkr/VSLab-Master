package com.composite.service;

import com.composite.service.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import java.net.URI;
import javax.ws.rs.DELETE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@Component
//@RequestMapping("/categories")
public class ProductCategoryController {

	private static final String CATEGORY_BASE_URL = "http://category-service/categories";
	private static final String PRODUCT_BASE_URL = "http://product-service/products";

    @LoadBalanced
	private WebClient categoryClient = WebClient.create(CATEGORY_BASE_URL);
    
    @LoadBalanced
    private WebClient productClient = WebClient.create(PRODUCT_BASE_URL);

    /*
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<?> deleteCategoryAndProducts(@PathVariable Integer id) {

		// TODO Should we delete the category not at first, but after all products where deleted, to ensure consistent data in case a product deletion failed.

		// Delete category
		ResponseEntity<Void> categoryDeleteResponseEntity = categoryClient.delete().uri("/{id}", id).retrieve().toBodilessEntity().block();

		// If the category was deleted successfully, then delete the associated products.
		if (categoryDeleteResponseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
			Product[] productResponse = productClient.get().uri("?categoryId={id}", id).retrieve().bodyToMono(Product[].class).block();

			for (Product product : productResponse) {
				ResponseEntity<Void> productDeleteResponseEntity = productClient.delete().uri("/{id}" + product.getId()).retrieve().toBodilessEntity().block();

				// TODO Check call logging
				System.out.println("Call to " + productDeleteResponseEntity.getHeaders().getOrigin() + " returned " + productDeleteResponseEntity.getStatusCodeValue());
			}

			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
    }
    */
}
