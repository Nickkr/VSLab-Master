package com.composite.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SuppressWarnings("deprecation")
public class CompositeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompositeServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder builder() {
		return WebClient.builder();
	}

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder.build();
	}

}
