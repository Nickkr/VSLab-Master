package com.authorization.AuthServer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SuppressWarnings("deprecation")
@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfig {

	@ConfigurationProperties(prefix = "security.oauth2.client.auth-server-client-credentials")
	@Bean
	public OAuth2ProtectedResourceDetails authServerClientCredentialsResourceDetails() {
		return new ClientCredentialsResourceDetails();
	}

	@Bean
	@LoadBalanced
	public OAuth2RestTemplate authServerClientCredentialsOAuth2RestTemplate(
			@Qualifier("authServerClientCredentialsResourceDetails") OAuth2ProtectedResourceDetails resourceDetails) {
		return new OAuth2RestTemplate(resourceDetails);
	}

}
