package com.blog.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(new CustomUserDetailsFilter(), OAuth2LoginAuthenticationFilter.class)
                .cors().and()
                .authorizeRequests()
                .antMatchers("/","/post/highest/**", "/post/gets/**", "/post/get/**", "/post/create/**",
                        "/post/edit/**", "/post/upvote/**", "/post/downvote/**", "/post/byUser/**",
                        "/post/delete/**", "/post/search/**", "/comment/get/**", "/comment/gets/**", "/comment/create/**",
                        "/comment/upvote/**", "/comment/downvote/**", "/comment/edit/**",
                        "/comment/delete/**", "/user/get/**", "/user/requestPromotion/**", "/user/edit/**","/admin/gets/**",
                        "/admin/delete/**","/admin/promote/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .and()
                .csrf().disable()
                .headers().addHeaderWriter((request, response) -> {
                    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
                    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                    response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Oauth-Credential, X-Oauth-Provider");
                    response.setHeader("Access-Control-Allow-Credentials", "true");
                });
    }
}

