package com.authorization.AuthServer;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@SuppressWarnings("deprecation")
//@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 /*    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsManager();
    } */
}