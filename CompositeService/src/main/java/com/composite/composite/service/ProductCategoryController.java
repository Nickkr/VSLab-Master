package com.composite.composite.service;

import com.composite.composite.service.Product;
import java.net.URI;
import javax.ws.rs.DELETE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;


import reactor.core.publisher.Mono;

@RestController
@Component
public class ProductCategoryController {

    private WebClient clientCategory;
    private WebClient clientProduct;

	private RestTemplate restTemplate = new RestTemplate();


    ProductCategoryController() {
        clientCategory = WebClient.create("http://category-service/");
        clientProduct = WebClient.create("http://product-service/");
    }

    @DeleteMapping("/categories/{id}")
    String deleteCategoryAndProducts(@PathVariable int id) {
        Mono<ResponseEntity<Void>> categoryResponse = clientCategory.delete().uri("/categories/" + id).retrieve().toBodilessEntity();
        ResponseEntity<Void> response = categoryResponse.block();

        if(response.getStatusCode() != HttpStatus.NO_CONTENT) {
            return "Category not found!";
        }

        Product[] productResponse = clientProduct.get().uri("/products?categoryId=" + id).retrieve().bodyToMono(Product[].class).block();

        for (Product product : productResponse) {
            clientProduct.delete().uri("/products/" + product.getId()).exchange().block();
        }

        return "DELETED";
    }

    @GetMapping("/products")
    Product[] getCategories() {
        return restTemplate.getForObject("http://product-service/products/", Product[].class);
    }

}
