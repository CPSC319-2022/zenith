package com.blog.controller;

import com.blog.exception.BlogException;
import com.blog.exception.InitializationException;
import com.blog.exception.LoginFailedException;
import com.blog.model.User;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
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
    private static GoogleIdTokenVerifier VERIFIER;

    static {
        // Create the GoogleIDTokenVerifier
        try {
            VERIFIER = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("137046975675-86mneph4bv1sfafa1788famgv2ot695r.apps.googleusercontent.com"))  // TODO: hide this
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            VERIFIER = null;
        }
    }

    /**
     * Gets the userID from the given Google access token.
     *
     * @param accessToken The Google access token.
     * @return The userID.
     * @throws LoginFailedException Invalid access token.
     * @throws InitializationException Unable to create GoogleIDTokenVerifier.
     */
    public static String getUserID(String accessToken) throws LoginFailedException, InitializationException {
        return getPayload(accessToken).get("sub").toString();
    }

    /**
     * Gets the payload from the given Google access token.
     *
     * @param accessToken The Google access token.
     * @return The payload.
     * @throws LoginFailedException Invalid access token.
     * @throws InitializationException Unable to create GoogleIDTokenVerifier.
     */
    public static Payload getPayload(String accessToken) throws LoginFailedException, InitializationException {
        // Remove the "Bearer " prefix from the access token
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring("Bearer ".length());
        }

        // Verify the access token
        GoogleIdToken idToken;
        try {
            idToken = VERIFIER.verify(accessToken);
        } catch (GeneralSecurityException | IOException e) {
            throw new LoginFailedException("Invalid access token.");
        } catch (NullPointerException e) {
            throw new InitializationException("Unable to create GoogleIDTokenVerifier.");
        }

        // Return the userID
        return idToken.getPayload();
    }

    // TODO: replaced by getPayload above?
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
            User user = User.retrieve(userId);
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