package hska.iwi.eShopMaster.controller;

import java.util.Map;

import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import hska.iwi.eShopMaster.ExceptionHelper;
import hska.iwi.eShopMaster.auth.AuthFactory;
import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.impl.UserManagerImpl;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

public class LoginAction extends ActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = -983183915002226000L;
	private String username = null;
	private String password = null;
	private String firstname;
	private String lastname;
	private String role;

	@Override
	public String execute() throws Exception {

		// Return string:
		String result = "input";

		try {
			RestTemplate restTemplate = AuthFactory.login(getUsername(), getPassword());

			UserManager myCManager = new UserManagerImpl(restTemplate);
			User user = myCManager.getUserByUsername(getUsername());

			// Get session to save user role and login:
			Map<String, Object> session = ActionContext.getContext().getSession();

			// Does user exist?
			if (user != null) {
				// Is the password correct?
				if (user.getPassword().equals(getPassword())) {
					// Save user object in session:
					session.put("webshop_user", user);
					session.put("message", "");
					firstname = user.getFirstname();
					lastname = user.getLastname();
					role = user.getRole();
					result = "success";
				} else {
					addActionError(getText("error.password.wrong"));
				}
			} else {
				addActionError(getText("error.username.wrong"));
			}

		} catch (OAuth2AccessDeniedException ex) {
			addActionError(ex.getMessage());

			if (ex.getMessage() == "Error requesting access token.") {
				addActionError(getText("error.username.wrong"));
			} else if (ex.getMessage() == "Access token denied.") {
				addActionError(getText("error.password.wrong"));
			}
			
		} catch (OAuth2Exception ex) {
			addActionError(ex.getMessage());
		} catch (Exception ex) {
			addActionError(ex.getClass().getCanonicalName());
			ExceptionHelper.addExceptionMessages(ex, this);
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public void validate() {
		if (getUsername() != null && getUsername().length() == 0) {
			addActionError(getText("error.username.required"));
		}
		if (getPassword() != null && getPassword().length() == 0) {
			addActionError(getText("error.password.required"));
		}
	}

	public String getUsername() {
		return (this.username);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return (this.password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
