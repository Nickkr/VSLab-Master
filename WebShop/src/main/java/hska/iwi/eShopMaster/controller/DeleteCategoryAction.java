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

public class DeleteCategoryAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1254575994729199914L;
	
	private int catId;
	private List<Category> categories;

	public String execute() throws Exception {
		
		String res = "input";
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("webshop_user");
		
		if(user != null && (user.getRole().equals("ADMIN"))) {

			RestTemplate restTemplate = AuthFactory.getOAuth2RestTemplateWithPassword(user);
			if (restTemplate == null) {
				return res;
			}

			// Helper inserts new Category in DB:
			CategoryManager categoryManager = new CategoryManagerImpl(restTemplate);
		
			categoryManager.delCategoryById(catId);

			categories = categoryManager.getCategories();
				
			res = "success";

		}
		
		return res;
		
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
