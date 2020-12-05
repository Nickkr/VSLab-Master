package com.composite.service;

public class ProductComposite {
    
	private long id;
	private String name;
	private double price;
	private String category;
    private String details;
    
    public ProductComposite() {
	}

	public ProductComposite(String name, double price, String category) {
		this.name = name;
		this.price = price;
		this.category = category;
	}

	public ProductComposite(String name, double price, String category, String details) {
		this.name = name;
		this.price = price;
		this.category = category;
		this.details = details;
    }

    public ProductComposite(Product product, String category) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.details = product.getDetails();
        this.category = category;
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

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}


}
