package com.core.productservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    Product newProduct(@RequestBody Product newProduct) {
        return repository.save(newProduct);
    }

    @GetMapping("/products/{id}")
    Product one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PutMapping("/products/{id}")
    Product editProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        return repository.findById(id).map(product -> {
            product.setName(newProduct.getName());
            product.setCategoryId(newProduct.getCategoryId());
            product.setDetails(newProduct.getDetails());
            product.setPrice(newProduct.getPrice());
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
