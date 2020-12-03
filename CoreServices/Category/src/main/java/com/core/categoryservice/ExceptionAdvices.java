package com.core.categoryservice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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

	private Throwable GetRootException(Throwable throwable) {
		if (throwable.getCause() == null) {
			return throwable;
		} else {
			return GetRootException(throwable.getCause());
		}
	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> GeneralExceptionHandler(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GetExceptionMessages(ex));
	}

	@ResponseBody
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<String> CategoryNotFoundHandler(CategoryNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> IllegalArgumentExceptionHandler(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<String> EmptyResultDataAccessExceptionHandler(EmptyResultDataAccessException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> DataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex) {
		return ResponseEntity.badRequest().body(GetRootException(ex).getMessage());
	}

}