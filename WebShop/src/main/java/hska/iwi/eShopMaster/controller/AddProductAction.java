package hska.iwi.eShopMaster.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import hska.iwi.eShopMaster.auth.AuthFactory;
import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.CategoryManagerImpl;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.ProductManagerImpl;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

public class AddProductAction extends ActionSupport {

	private static final long serialVersionUID = 39979991339088L;

	private String name = null;
	private String price = null;
	private int categoryId = 0;
	private String details = null;
	private List<Category> categories;

	public String execute() throws Exception {
		String result = "input";
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("webshop_user");

		if (user != null && (user.getRole().equals("ADMIN"))) {

			RestTemplate restTemplate = AuthFactory.getOAuth2RestTemplateWithPassword(user);
			if (restTemplate == null) {
				return result;
			}

			ProductManager productManager = new ProductManagerImpl(restTemplate);
			int productId = productManager.addProduct(name, Double.parseDouble(price), categoryId, details);

			if (productId > 0) {
				result = "success";
			}
		}

		return result;
	}

	@Override
	public void validate() {

		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("webshop_user");

		if (user != null) {

			RestTemplate restTemplate = AuthFactory.getOAuth2RestTemplateWithPassword(user);
			if (restTemplate == null) {
				addActionError("User login not available.");
				return;
			}

			CategoryManager categoryManager = new CategoryManagerImpl(restTemplate);
			this.setCategories(categoryManager.getCategories());

			// Validate name:
			if (getName() == null || getName().length() == 0) {
				addActionError(getText("error.product.name.required"));
			}

			// Validate price:
			if (String.valueOf(getPrice()).length() > 0) {
				if (!getPrice().matches("[0-9]+(.[0-9][0-9]?)?")
						|| Double.parseDouble(getPrice()) < 0.0) {
					addActionError(getText("error.product.price.regex"));
				}
			} else {
				addActionError(getText("error.product.price.required"));
			}
			
		} else {
			addActionError("User login not available.");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
