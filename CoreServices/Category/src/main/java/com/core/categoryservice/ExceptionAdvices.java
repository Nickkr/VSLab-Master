package com.core.categoryservice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvices {

	private String getExceptionMessages(Exception ex) {
		StringBuilder builder = new StringBuilder();

		builder.append(ex.getClass().getName());
		builder.append(": ");
		builder.append(ex.getLocalizedMessage());
		builder.append(System.lineSeparator());

		getExceptionMessages(ex.getCause(), builder);
		return builder.toString();
	}

	private void getExceptionMessages(Throwable ex, StringBuilder builder) {
		if (ex == null)
			return;

		builder.append("Caused by");
		builder.append(": ");
		builder.append(ex.getClass().getName());
		builder.append(": ");
		builder.append(ex.getLocalizedMessage());
		builder.append(System.lineSeparator());

		getExceptionMessages(ex.getCause(), builder);
	}

	private Throwable getRootException(Throwable throwable) {
		if (throwable.getCause() == null) {
			return throwable;
		} else {
			return getRootException(throwable.getCause());
		}
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String GeneralExceptionHandler(Exception ex) {
		return getExceptionMessages(ex);
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String CategoryNotFoundHandler(CategoryNotFoundException ex) {
		return ex.getLocalizedMessage();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String IllegalArgumentExceptionHandler(IllegalArgumentException ex) {
		return ex.getLocalizedMessage();
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String EmptyResultDataAccessExceptionHandler(EmptyResultDataAccessException ex) {
		return ex.getLocalizedMessage();
	}

	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String DataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex) {
		return getRootException(ex).getLocalizedMessage();
	}

}