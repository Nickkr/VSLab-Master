package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.RoleDAO;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.UserDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.RestTemplate;

import hska.iwi.eShopMaster.Configuration;
import hska.iwi.eShopMaster.auth.AuthFactory;
/**
 * 
 * @author knad0001
 */

public class UserManagerImpl implements UserManager {
	private final RestTemplate restTemplate;
	UserDAO helper;
	
	public UserManagerImpl(RestTemplate restTemplate) {
		helper = new UserDAO();
		this.restTemplate = restTemplate;
	}

	
	public void registerUser(String username, String name, String lastname, String password, String role) {
		User user = new User(username, name, lastname, password, role);
		restTemplate.postForObject(Configuration.WEB_SHOP_API + "/users", user, User.class);
	}

	
	public User getUserByUsername(String username) {
		User user = restTemplate.getForObject(Configuration.WEB_SHOP_API + "/users/" + username, User.class);
		return user;
	/* 	if (username == null || username.equals("")) {
			return null;
		}
		return helper.getUserByUsername(username); */
	}

	public boolean deleteUserById(int id) {
		User user = new User();
		user.setId(id);
		helper.deleteObject(user);
		return true;
	}

	public Role getRoleByLevel(int level) {
		RoleDAO roleHelper = new RoleDAO();
		return roleHelper.getRoleByLevel(level);
	}

	public boolean doesUserAlreadyExist(String username) {
		
    	User dbUser = this.getUserByUsername(username);
    	
    	if (dbUser != null){
    		return true;
    	}
    	else {
    		return false;
    	}
	}
	

	public boolean validate(User user) {
		if (user.getFirstname().isEmpty() || user.getPassword().isEmpty() || user.getRole() == null || user.getLastname() == null || user.getUsername() == null) {
			return false;
		}
		
		return true;
	}

}
