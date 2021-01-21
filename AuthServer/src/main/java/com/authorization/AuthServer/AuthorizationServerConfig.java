package com.authorization.AuthServer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

@SuppressWarnings("deprecation")
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

/*
	@Autowired
	private ClientDetailsService clientDetailsService;
 */

	@Autowired
	private UserDetailsService userDetailsManager;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("messaging-client")
				.authorizedGrantTypes("authorization_code", "refresh_token", "client_credentials", "password")
				.scopes("message.read", "message.write")
				.secret("{noop}secret")
				.redirectUris("http://localhost:8080/authorized")
				.authorities("MessagingTest")
				.and()
				.withClient("Postman").secret("{noop}secret").authorizedGrantTypes("client_credentials", "password").scopes("message.read").authorities("Test");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
				.authenticationManager(this.authenticationManager)
				.tokenStore(tokenStore())
				.userDetailsService(userDetailsManager)
				// .userApprovalHandler(userApprovalHandler())
				.accessTokenConverter(accessTokenConverter());
	}

/* 
	@Bean
	public UserApprovalHandler userApprovalHandler() {
		ApprovalStoreUserApprovalHandler userApprovalHandler = new ApprovalStoreUserApprovalHandler();
		userApprovalHandler.setApprovalStore(approvalStore());
		userApprovalHandler.setClientDetailsService(this.clientDetailsService);
		userApprovalHandler.setRequestFactory(new DefaultOAuth2RequestFactory(this.clientDetailsService));
		return userApprovalHandler;
	}
 */

	@Bean
	public TokenStore tokenStore() {
		JwtTokenStore tokenStore = new JwtTokenStore(accessTokenConverter());
		tokenStore.setApprovalStore(approvalStore());
		return tokenStore;
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		final RsaSigner signer = new RsaSigner(KeyConfig.getSignerKey());

		JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
			private JsonParser objectMapper = JsonParserFactory.create();

			@Override
			protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
				String content;
				try {
					content = this.objectMapper.formatMap(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
				} catch (Exception ex) {
					throw new IllegalStateException("Cannot convert access token to JSON", ex);
				}
				Map<String, String> headers = new HashMap<>();
				headers.put("kid", KeyConfig.VERIFIER_KEY_ID);
				String token = JwtHelper.encode(content, signer, headers).getEncoded();
				return token;
			}
		};
		converter.setSigner(signer);
		converter.setVerifier(new RsaVerifier(KeyConfig.getVerifierKey()));
		return converter;
	}

	@Bean
	public ApprovalStore approvalStore() {
		return new InMemoryApprovalStore();
	}

	@Bean
	public JWKSet jwkSet() {
		RSAKey.Builder builder = new RSAKey.Builder(KeyConfig.getVerifierKey())
				.keyUse(KeyUse.SIGNATURE)
				.algorithm(JWSAlgorithm.RS256)
				.keyID(KeyConfig.VERIFIER_KEY_ID);
		return new JWKSet(builder.build());
	}
}
