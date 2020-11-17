package com.composite.composite.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static final String BASE_URI = "http://localhost:18084/";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
