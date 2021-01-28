package hska.iwi.eShopMaster.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import hska.iwi.eShopMaster.auth.AuthFactory;
import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.CategoryManagerImpl;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

public class AddCategoryAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6704600867133294378L;

	private String newCatName = null;

	private List<Category> categories;

	User user;

	public String execute() throws Exception {

		String res = "input";

		Map<String, Object> session = ActionContext.getContext().getSession();
		user = (User) session.get("webshop_user");
		if (user != null && (user.getRole().equals("ADMIN"))) {

			RestTemplate restTemplate = AuthFactory.getOAuth2RestTemplateWithPassword(user);
			if (restTemplate == null) {
				return res;
			}

			CategoryManager categoryManager = new CategoryManagerImpl(restTemplate);

			// Add category
			categoryManager.addCategory(newCatName);

			// Go and get new Category list
			this.setCategories(categoryManager.getCategories());

			res = "success";
		}

		return res;

	}

	@Override
	public void validate() {
		if (getNewCatName().length() == 0) {
			addActionError(getText("error.catname.required"));
		}

		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("webshop_user");

		if (user != null) {

			RestTemplate restTemplate = AuthFactory.getOAuth2RestTemplateWithPassword(user);
			if (restTemplate == null) {
				addActionError("User login not available.");
				return;
			}

			// Go and get new Category list
			CategoryManager categoryManager = new CategoryManagerImpl(restTemplate);
			this.setCategories(categoryManager.getCategories());

		} else {
			addActionError("User login not available.");
		}
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getNewCatName() {
		return newCatName;
	}

	public void setNewCatName(String newCatName) {
		this.newCatName = newCatName;
	}
}
