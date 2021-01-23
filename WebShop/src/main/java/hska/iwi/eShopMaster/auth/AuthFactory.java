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
    
    public static OAuth2RestTemplate getOAuthRestTemplate(String grantType) {
        BaseOAuth2ProtectedResourceDetails resourceDetails = null;
        switch(grantType) {
            case "password": 
            resourceDetails = new ResourceOwnerPasswordResourceDetails(); 
            ((ResourceOwnerPasswordResourceDetails) resourceDetails).setUsername(AuthFactory.username); 
            ((ResourceOwnerPasswordResourceDetails) resourceDetails).setPassword(AuthFactory.password); 
            break;
            case "client":
            resourceDetails = new ClientCredentialsResourceDetails(); 
            break;
        }
        resourceDetails.setAccessTokenUri("http://192.168.178.37:8081/webshop-api/auth/oauth/token");
        resourceDetails.setClientId("WebShop"); 
        resourceDetails.setClientSecret("secret"); 
        resourceDetails.setScope(Arrays.asList("read"));
        return new OAuth2RestTemplate(resourceDetails);
    }
}
