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
                .antMatchers(HttpMethod.GET,"/profile/{id}").permitAll()
                .antMatchers(HttpMethod.POST,"/profile").permitAll()
                .antMatchers(HttpMethod.PUT, "/profile/put/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE,"/profile/delete/{id}").permitAll()

                .antMatchers(HttpMethod.GET,"/contact/{id}").permitAll()
                .antMatchers(HttpMethod.POST,"/contact").permitAll()
                .antMatchers(HttpMethod.PUT, "/contact/put/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE,"/contact/delete/{id}").permitAll()

                .antMatchers(HttpMethod.GET,"/transactions/made/{id}").permitAll()
                .antMatchers(HttpMethod.GET,"/transactions/received/{id}").permitAll()
                .antMatchers(HttpMethod.POST,"/transactions").permitAll()
                .anyRequest().authenticated();
    }
}