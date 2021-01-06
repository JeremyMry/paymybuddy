package com.paymybuddy.app.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST,"/profile").permitAll()
                .antMatchers(HttpMethod.PUT, "/profile/put/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE,"/profile/delete/{id}").permitAll()
                .antMatchers(HttpMethod.GET,"/profile/{id}").permitAll()
                .anyRequest().authenticated();
    }
}