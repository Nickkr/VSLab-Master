package com.composite.composite.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/categories")
public class ProductCategoryController {

	private static final String PRODUCT_BASE_URL = "http://localhost:18081/product";
	private static final String CATEGORY_BASE_URL = "http://localhost:18082/categories";

	private WebClient categoryClient = WebClient.create(ProductCategoryController.CATEGORY_BASE_URL);
	private WebClient productClient = WebClient.create(PRODUCT_BASE_URL);

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteCategoryAndProducts(@PathVariable Integer id) {

		// TODO Should we delete the category not at first, but after all products where deleted, to ensure consistent data in case a product deletion failed.

		// Delete category
		ResponseEntity<Void> categoryDeleteResponseEntity = categoryClient.delete().uri("/{id}", id).retrieve().toBodilessEntity().block();

		// If the category was deleted successfully, then delete the associated products.
		if (categoryDeleteResponseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
			Product[] productResponse = productClient.get().uri("?categoryId={id}", id).retrieve().bodyToMono(Product[].class).block();

			for (Product product : productResponse) {
				ResponseEntity<Void> productDeleteResponseEntity = productClient.delete().uri("/{id}" + product.getId()).retrieve().toBodilessEntity().block();

				System.out.println("Call to " + productDeleteResponseEntity.getHeaders().getOrigin() + " returned " + productDeleteResponseEntity.getStatusCodeValue());
			}

			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}
}
