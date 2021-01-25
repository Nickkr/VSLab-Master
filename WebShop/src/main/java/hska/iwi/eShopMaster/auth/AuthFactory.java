package hska.iwi.eShopMaster.auth;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.oauth2.client.resource.*;
import java.util.*;

public class AuthFactory {
    public static String username;
    public static String password;
	public static String WEB_SHOP_API = "http://host.docker.internal:8081/webshop-api";
	public static String ACCESS_TOKEN_URI = AuthFactory.WEB_SHOP_API + "/auth/oauth/token";
    public static String CLIENT_ID = "WebShop";
    public static String CLIENT_SECRET = "secret";
    public static OAuth2RestTemplate OAUTH2_REST_TEMPLATE_PASSWORD = null;
    public static OAuth2RestTemplate OAUTH2_REST_TEMPLATE_CREDENTIALS = null;

    public static OAuth2RestTemplate getOAuth2RestTemplateWithPassword() {
            ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails(); 
            resourceDetails.setUsername(AuthFactory.username); 
            resourceDetails.setPassword(AuthFactory.password); 
            resourceDetails.setAccessTokenUri(AuthFactory.ACCESS_TOKEN_URI);
            resourceDetails.setClientId(AuthFactory.CLIENT_ID); 
            resourceDetails.setClientSecret(AuthFactory.CLIENT_SECRET); 
            resourceDetails.setScope(Arrays.asList("read"));
            return new OAuth2RestTemplate(resourceDetails);
    }

    public static OAuth2RestTemplate getOAuth2RestTemplateWithClientCredentails() {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails(); 
        resourceDetails.setAccessTokenUri(AuthFactory.ACCESS_TOKEN_URI);
        resourceDetails.setClientId(AuthFactory.CLIENT_ID); 
        resourceDetails.setClientSecret(AuthFactory.CLIENT_SECRET); 
        resourceDetails.setScope(Arrays.asList("read"));
        return new OAuth2RestTemplate(resourceDetails);
    }
}
