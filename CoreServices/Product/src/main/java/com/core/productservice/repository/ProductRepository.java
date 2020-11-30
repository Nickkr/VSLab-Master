package com.core.productservice.repository;

import java.util.List;

import com.core.productservice.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE (:minPrice is null or price >= :minPrice)"
     + " and (:maxPrice is null or price <= :maxPrice)" 
     + " and (:categoryId is null or categoryId = :categoryId)" 
     + " and (:text is null or details like %:text%)")
    List<Product> findByPriceGreaterThanEqualAndPriceLessThanEqualAndCategoryIdEqualsAndDetailsContains(Double minPrice,
            Double maxPrice, Integer categoryId, String text);

}
