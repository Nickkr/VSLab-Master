package com.composite.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class ExceptionAdvices {

	@ResponseBody
	@ExceptionHandler(HttpClientErrorException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String HttpClientErrorExceptionHandler(HttpClientErrorException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(WebClientResponseException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String WebClientResponseExceptionHandler(WebClientResponseException ex) {
		return ex.getMessage();
	}
}