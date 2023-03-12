package com.blog.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
public class loginController {

    @GetMapping
    public Map<String, Object> currentUser(OAuth2AuthenticationToken token){
        return token.getPrincipal().getAttributes();
    }
}
