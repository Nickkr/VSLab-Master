package hska.iwi.eShopMaster.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import hska.iwi.eShopMaster.Configuration;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

public class AuthFactory {

	private static final String ACCESS_TOKEN_URI = Configuration.WEB_SHOP_API + "/auth/oauth/token";
	private static final String CLIENT_ID = "WebShop";
	private static final String CLIENT_SECRET = "secret";
	private static final Map<String, OAuth2RestTemplate> OAUTH2_REST_TEMPLATE_PASSWORD_MAP = new HashMap<>();
	private static final OAuth2RestTemplate OAUTH2_REST_TEMPLATE_CLIENT = createOAuth2RestTemplateWithClientCredentails();

	private static OAuth2RestTemplate createOAuth2RestTemplateWithPassword(String username, String password) {
		ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
		resourceDetails.setUsername(username);
		resourceDetails.setPassword(password);
		resourceDetails.setAccessTokenUri(AuthFactory.ACCESS_TOKEN_URI);
		resourceDetails.setClientId(AuthFactory.CLIENT_ID);
		resourceDetails.setClientSecret(AuthFactory.CLIENT_SECRET);
		resourceDetails.setScope(Arrays.asList("read"));
		return new OAuth2RestTemplate(resourceDetails);
	}

	private static OAuth2RestTemplate createOAuth2RestTemplateWithClientCredentails() {
		ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setAccessTokenUri(AuthFactory.ACCESS_TOKEN_URI);
		resourceDetails.setClientId(AuthFactory.CLIENT_ID);
		resourceDetails.setClientSecret(AuthFactory.CLIENT_SECRET);
		resourceDetails.setScope(Arrays.asList("read"));
		return new OAuth2RestTemplate(resourceDetails);
	}

	public static OAuth2RestTemplate login(String username, String password) {
		final OAuth2RestTemplate restTemplate = createOAuth2RestTemplateWithPassword(username, password);
		OAUTH2_REST_TEMPLATE_PASSWORD_MAP.put(username, restTemplate);
		return restTemplate;
	}

	public static void logout(String username) {
		OAUTH2_REST_TEMPLATE_PASSWORD_MAP.remove(username);
	}

	public static OAuth2RestTemplate getOAuth2RestTemplateWithPassword(User user) {
		final OAuth2RestTemplate restTemplate = OAUTH2_REST_TEMPLATE_PASSWORD_MAP.get(user.getUsername());
		return restTemplate;
	}

	public static OAuth2RestTemplate getOAuth2RestTemplateWithClientCredentails() {
		return OAUTH2_REST_TEMPLATE_CLIENT;
	}

}
