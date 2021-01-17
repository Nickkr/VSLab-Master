package com.authorization.AuthServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("deprecation")
public class UserDetailsManager implements UserDetailsService {

	public static String USER_BASE_URL = "http://user-service/users";
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsManager.class);

	@Autowired
	RestTemplate restTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CoreUser coreUser = restTemplate.getForObject(USER_BASE_URL + "/{username}", CoreUser.class, username);
		if (coreUser == null) {
			throw new UsernameNotFoundException("Test");
		}
		UserDetails user = User.withDefaultPasswordEncoder()
				.username(coreUser.getUsername())
				.password(coreUser.getPassword())
				.roles(coreUser.getRole().name())
				.build();
		logger.info(coreUser.getRole().name());
		logger.info(user.toString());
		return user;
	}

}
