package hska.iwi.eShopMaster.controller;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import hska.iwi.eShopMaster.auth.AuthFactory;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

public class LogoutAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -530488065543708898L;

	public String execute() throws Exception {

		// Remove user specific rest template if exists.
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("webshop_user");
		if (user != null) {
			AuthFactory.logout(user.getUsername());
		}

		// Clear session:
		ActionContext.getContext().getSession().clear();

		return "success";

	}
}
