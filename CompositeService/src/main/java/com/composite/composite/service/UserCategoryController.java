package com.composite.composite.service;

import java.net.URI;
import javax.ws.rs.DELETE;
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
    public static final String BASE_URI = "http://localhost:18083/";


    UserCategoryController() {
        client = WebClient.create(UserCategoryController.BASE_URI);
    }

    @DeleteMapping("/categories/{id}")
    String deleteCategoryAndProducts(@PathVariable int id) {
        // Category service anfragen nach category.
        Mono<ClientResponse> categoryResponse = client.delete().uri("/categories/" + id).exchange();

        System.out.println(categoryResponse);
        return "YOLO";
        //Response productResponse = target.path("products?category=" + id).request().get();
    }
}
