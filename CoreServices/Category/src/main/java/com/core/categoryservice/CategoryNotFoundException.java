package com.core.categoryservice;

public class CategoryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3475157728738290035L;

	public CategoryNotFoundException(Integer id) {
		super("Could not find category " + id);
	}

	
}
