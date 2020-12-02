package com.core.productservice.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.ipc.http.HttpSender.Response;

import com.core.productservice.model.Product;
import com.core.productservice.repository.ProductRepository;

@RestController
public class ProductController {
    private final ProductRepository repository;

    ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/products")
    List<Product> all(@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String searchText) {

        return repository.findByPriceGreaterThanEqualAndPriceLessThanEqualAndCategoryIdEqualsAndDetailsContains(
                minPrice, maxPrice, categoryId, searchText);
    }

    @PostMapping("/products")
    ResponseEntity<Product> newProduct(@RequestBody Product newProduct) {
        try {
            Product product = repository.save(newProduct);
            return ResponseEntity.ok(product);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/products/{id}")
    ResponseEntity<Product> getProductById(@PathVariable Long id) {
            try {
                Product product = repository.findById(id).get();
                return ResponseEntity.ok(product);
            } catch(NoSuchElementException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
    }

    @PutMapping("/products/{id}")
    Product editProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        return repository.findById(id).map(product -> {
            product.setName(newProduct.getName() == null ? product.getName() : newProduct.getName());
            product.setCategoryId(newProduct.getCategoryId() == 0 ? product.getCategoryId() : newProduct.getCategoryId());
            product.setDetails(newProduct.getDetails() == null ? product.getDetails() : newProduct.getDetails());
            product.setPrice(newProduct.getPrice() == 0.0 ? product.getPrice() : newProduct.getPrice());
            return repository.save(product);
        }).orElseGet(() -> {
            newProduct.setId(id);
            return repository.save(newProduct);
        });
    }

    @DeleteMapping("/products/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
