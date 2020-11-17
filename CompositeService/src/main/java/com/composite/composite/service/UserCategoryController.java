package com.composite.composite.service;


import com.composite.composite.service.Product;
import java.net.URI;
import javax.ws.rs.DELETE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class UserCategoryController {

    private WebClient client;
    private WebClient client2;
    public static final String BASE_URI = "http://localhost:18082/";


    UserCategoryController() {
        client = WebClient.create(UserCategoryController.BASE_URI);
        client2 = WebClient.create("http://localhost:18081/");
    }

    @DeleteMapping("/categories/{id}")
    String deleteCategoryAndProducts(@PathVariable int id) {
        Mono<ResponseEntity<Void>> categoryResponse = client.delete().uri("/categories/" + id).retrieve().toBodilessEntity();
        ResponseEntity<Void> response = categoryResponse.block();

        if(response.getStatusCode() != HttpStatus.NO_CONTENT) {
            return "Category not found!";
        }

        Product[] productResponse = client2.get().uri("/products?categoryId=" + id).retrieve().bodyToMono(Product[].class).block();

        for (Product product : productResponse) {
            client2.delete().uri("/products/" + product.getId()).exchange().block();
        }

        return "DELETED";
    }
}
