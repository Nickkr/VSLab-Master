package com.composite.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class ExceptionAdvices {

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> GeneralExceptionHandler(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
	}

	@ResponseBody
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<String> HttpClientErrorExceptionHandler(HttpClientErrorException ex) {
		return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(WebClientResponseException.class)
	public ResponseEntity<String> WebClientResponseExceptionHandler(WebClientResponseException ex) {
		return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
	}

}