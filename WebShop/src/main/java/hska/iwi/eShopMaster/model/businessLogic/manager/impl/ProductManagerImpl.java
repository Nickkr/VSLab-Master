package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import java.util.Arrays;
import java.util.List;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.ProductDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.auth.*;

public class ProductManagerImpl implements ProductManager {
	private ProductDAO helper;

	public ProductManagerImpl() {
		helper = new ProductDAO();
	}

	public List<Product> getProducts() {
		Product[] products = AuthFactory.getOAuth2RestTemplateWithPassword()
				.getForObject("http://192.168.178.37:8081/webshop-api/products/", Product[].class);
		System.out.println(products.length);
		return Arrays.asList(products);
		// return helper.getObjectList();
		// products = AuthFactory.
	}

	public List<Product> getProductsForSearchValues(String searchDescription,
			Double searchMinPrice, Double searchMaxPrice) {	
		Product[] products = AuthFactory.getOAuth2RestTemplateWithPassword().getForObject("http://192.168.178.37:8081/webshop-api/products/" + "?minPrice={searchMinPrice}&maxPrice={searchMaxPrice}&searchText={searchDescription}", Product[].class, searchMinPrice, searchMaxPrice, searchDescription);
		return Arrays.asList(products);
		//return new ProductDAO().getProductListByCriteria(searchDescription, searchMinPrice, searchMaxPrice);
	}

	public Product getProductById(int id) {
		Product product = AuthFactory.getOAuth2RestTemplateWithPassword().getForObject("http://192.168.178.37:8081/webshop-api/products/" + id, Product.class);
		//return helper.getObjectById(id);
		return product;
	}

	public Product getProductByName(String name) {
		return helper.getObjectByName(name);
	}

	public int addProduct(String name, double price, int categoryId, String details) {
		int productId = -1;

		CategoryManager categoryManager = new CategoryManagerImpl();
		Category category = categoryManager.getCategory(categoryId);

		if (category != null) {
			Product product;
			if (details == null) {
				product = new Product(name, price, category);
			} else {
				product = new Product(name, price, category, details);
			}

			helper.saveObject(product);
			productId = product.getId();
		}

		return productId;
	}

	public void deleteProductById(int id) {
		helper.deleteById(id);
	}

	public boolean deleteProductsByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

}
