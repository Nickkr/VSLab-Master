package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

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
				.getForObject(AuthFactory.WEB_SHOP_API + "/products/", Product[].class);
		System.out.println(products.length);
		return Arrays.asList(products);
	}

	public List<Product> getProductsForSearchValues(String searchDescription,
			Double searchMinPrice, Double searchMaxPrice) {	
		Product[] products = AuthFactory.getOAuth2RestTemplateWithPassword().getForObject(AuthFactory.WEB_SHOP_API + "/products/" + "?minPrice={searchMinPrice}&maxPrice={searchMaxPrice}&searchText={searchDescription}", Product[].class, searchMinPrice, searchMaxPrice, searchDescription);
		return Arrays.asList(products);
	}

	public Product getProductById(int id) {
		Product product = AuthFactory.getOAuth2RestTemplateWithPassword().getForObject(AuthFactory.WEB_SHOP_API +"/products/" + id, Product.class);
		return product;
	}

	public Product getProductByName(String name) {
		throw new NotImplementedException("WILL NEVER BE USED");
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
			Product productResp = AuthFactory.getOAuth2RestTemplateWithPassword().postForObject(AuthFactory.WEB_SHOP_API +"/products/", product, Product.class );
			productId = productResp.getId();
		}

		return productId;
	}

	public void deleteProductById(int id) {
		AuthFactory.getOAuth2RestTemplateWithPassword().delete(AuthFactory.WEB_SHOP_API +"/products/" + id);
	}

	public boolean deleteProductsByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

}
