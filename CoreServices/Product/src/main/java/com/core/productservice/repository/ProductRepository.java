package com.core.productservice.repository;

import java.util.List;

import com.core.productservice.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
 
    List<Product> findByPriceGreaterThanEqualAndPriceLessThanEqual(double minPrice, double maxPrice);
    
    List<Product> findByPriceGreaterThanEqualAndPriceLessThanEqualAndCategoryIdEquals(double minPrice, double maxPrice, int categoryId);

    List<Product> findByPriceGreaterThanEqualAndPriceLessThanEqualAndDetailsContains(double minPrice, double maxPrice, String text);

    List<Product> findByPriceGreaterThanEqualAndPriceLessThanEqualAndCategoryIdEqualsAndDetailsContains(double minPrice, double maxPrice, int categoryId, String text);

}
