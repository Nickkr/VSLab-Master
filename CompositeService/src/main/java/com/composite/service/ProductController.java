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
public class ProductController {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    public static String productApiUrl = "http://product-service/products";

    @GetMapping("/products")
    Product[] getProducts() {
        return restTemplate.getForObject(productApiUrl, Product[].class);
    }

    @PostMapping("/products")
    Product postMethodName(@RequestBody String newProduct) {
        return restTemplate.postForObject(productApiUrl, newProduct, Product.class);
    }

    @GetMapping("/products/{id}")
     Product getProductById(@PathVariable int id) {
         return restTemplate.getForObject(productApiUrl + "/" + id, Product.class);
     }    

     @DeleteMapping("/products/{id}")
     void deleteProductById(@PathVariable int id) {
         restTemplate.delete(productApiUrl + "/" + id);
     }

     @PutMapping("/products/{id}")
     void updateProductById(@PathVariable int id, @RequestBody String product) {
         restTemplate.put(productApiUrl + "/" + id, product);
     }
}
