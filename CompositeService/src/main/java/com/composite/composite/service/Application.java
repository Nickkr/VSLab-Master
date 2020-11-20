package com.composite.composite.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
@EnableDiscoveryClient
@SpringBootApplication
@RibbonClient("product-category-composite-service")
public class Application {

    public static final String BASE_URI = "http://localhost:18084/";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
