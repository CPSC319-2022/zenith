package com.blog.controller;

import com.blog.exception.BlogException;
import com.blog.exception.LoginFailedException;
import com.blog.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

import static com.blog.controller.UserController.createUser;

@Controller
public class LoginController {

    public Payload verifyGoogleIdToken(String idTokenString) throws LoginFailedException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jsonFactory)
                .setAudience(Collections.singletonList("YOUR_CLIENT_ID"))
                .build();

        GoogleIdToken idToken = null;

        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        // Invalid token or token verification failed
        if (idToken == null) throw new LoginFailedException("Invalid token or token verification failed");

        return idToken.getPayload();
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, Object> loginData) {
        String idTokenString = (String) loginData.get("idToken");

        Payload payload = null;
        try {
            payload = verifyGoogleIdToken(idTokenString);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String userId = payload.getSubject();


        try {
            User user = new User(userId);
            return ResponseEntity.ok(user.getUserID());
        } catch (BlogException e) {
            //ignored
        }


        try {
            User user = createUser(name,pictureUrl,userId);
            return ResponseEntity.ok(user.getUserID());
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}