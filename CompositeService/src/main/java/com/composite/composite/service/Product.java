package com.composite.composite.service;

public class Product {

	private long id;
	private String name;
	private double price;
	private int categoryId;
    private String details;
    
    public Product() {
	}

	public Product(String name, double price, int categoryId) {
		this.name = name;
		this.price = price;
		this.categoryId = categoryId;
	}

	public Product(String name, double price, int categoryId, String details) {
		this.name = name;
		this.price = price;
		this.categoryId = categoryId;
		this.details = details;
    }
    
    public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

    
}
