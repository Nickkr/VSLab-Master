package hska.iwi.eShopMaster.model.businessLogic.manager.impl;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.client.RestTemplate;

import hska.iwi.eShopMaster.Configuration;
import hska.iwi.eShopMaster.auth.AuthFactory;
import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.CategoryDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;

public class CategoryManagerImpl implements CategoryManager{
	private final RestTemplate restTemplate;
	private CategoryDAO helper;
	
	public CategoryManagerImpl(RestTemplate restTemplate) {
		helper = new CategoryDAO();
		this.restTemplate = restTemplate;
	}

	public List<Category> getCategories() {
		Category[] categories = restTemplate.getForObject(Configuration.WEB_SHOP_API + "/categories/", Category[].class);
		return Arrays.asList(categories);
	}

	public Category getCategory(int id) {
		Category category = restTemplate.getForObject(Configuration.WEB_SHOP_API + "/categories/" + id , Category.class);
		return category;
	}

	public Category getCategoryByName(String name) {
		throw new NotImplementedException("WILL NOT BE USED");
	}

	public void addCategory(String name) {
		Category cat = new Category(name);
		restTemplate.postForObject(Configuration.WEB_SHOP_API + "/categories/", cat, Category.class);
	}

	public void delCategory(Category cat) {
		restTemplate.delete(Configuration.WEB_SHOP_API +"/categories/" + cat.getId());
	}

	public void delCategoryById(int id) {
		restTemplate.delete(Configuration.WEB_SHOP_API +"/categories/" + id);
	}
}
