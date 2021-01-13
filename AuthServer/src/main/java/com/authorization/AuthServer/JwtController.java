package com.authorization.AuthServer;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Autowired
    private JWKSet jwkSet;

	@GetMapping(value = "/oauth2/keys", produces = "application/json; charset=UTF-8")
	public String keys() {
		return this.jwkSet.toString();
	}
}
