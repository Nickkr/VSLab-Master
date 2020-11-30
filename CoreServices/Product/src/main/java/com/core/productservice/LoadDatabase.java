package com.core.productservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.core.productservice.model.Product;
import com.core.productservice.repository.ProductRepository;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
    log.info("Hello World");
    System.out.println("Hello");

    return args -> {
      log.info("Preloading " + repository.save(new Product("Hello", 5, 1)));
    };
  }
}
