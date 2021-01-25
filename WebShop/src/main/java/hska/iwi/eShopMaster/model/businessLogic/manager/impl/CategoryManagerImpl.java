package hska.iwi.eShopMaster.model.businessLogic.manager.impl;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import hska.iwi.eShopMaster.auth.AuthFactory;
import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.CategoryDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;

public class CategoryManagerImpl implements CategoryManager{
	private CategoryDAO helper;
	
	public CategoryManagerImpl() {
		helper = new CategoryDAO();
	}

	public List<Category> getCategories() {
		Category[] categories = AuthFactory.getOAuth2RestTemplateWithPassword().getForObject(AuthFactory.WEB_SHOP_API + "/categories/", Category[].class);
		return Arrays.asList(categories);
	}

	public Category getCategory(int id) {
		Category category = AuthFactory.getOAuth2RestTemplateWithPassword().getForObject(AuthFactory.WEB_SHOP_API + "/categories/" + id , Category.class);
		return category;
	}

	public Category getCategoryByName(String name) {
		throw new NotImplementedException("WILL NOT BE USED");
	}

	public void addCategory(String name) {
		Category cat = new Category(name);
		AuthFactory.getOAuth2RestTemplateWithPassword().postForObject(AuthFactory.WEB_SHOP_API + "/categories/", cat, Category.class);
	}

	public void delCategory(Category cat) {
		AuthFactory.getOAuth2RestTemplateWithPassword().delete(AuthFactory.WEB_SHOP_API +"/categories/" + cat.getId());
	}

	public void delCategoryById(int id) {
		AuthFactory.getOAuth2RestTemplateWithPassword().delete(AuthFactory.WEB_SHOP_API +"/categories/" + id);
	}
}
