package com.authorization.AuthServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;

@RestController
public class JwtController {

	@Autowired
	private JWKSet jwkSet;

	@GetMapping(value = "/oauth2/keys", produces = "application/json; charset=UTF-8")
	public String keys() {
		return this.jwkSet.toString();
	}
}
