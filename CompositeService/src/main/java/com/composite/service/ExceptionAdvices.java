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

	private String GetExceptionMessages(Exception ex) {
		StringBuilder builder = new StringBuilder();

		builder.append(ex.getClass().getName());
		builder.append(": ");
		builder.append(ex.getLocalizedMessage());
		builder.append(System.lineSeparator());

		GetExceptionMessages(ex.getCause(), builder);
		return builder.toString();
	}

	private void GetExceptionMessages(Throwable ex, StringBuilder builder) {
		if (ex == null)
			return;

		builder.append("Caused by");
		builder.append(": ");
		builder.append(ex.getClass().getName());
		builder.append(": ");
		builder.append(ex.getLocalizedMessage());
		builder.append(System.lineSeparator());

		GetExceptionMessages(ex.getCause(), builder);
	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> GeneralExceptionHandler(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GetExceptionMessages(ex));
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