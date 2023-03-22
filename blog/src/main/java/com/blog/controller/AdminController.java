package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.*;
import com.blog.model.User;
import com.blog.model.UserLevel;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;

import static com.blog.model.UserLevel.ADMIN;

@RestController
@RequestMapping("/admin")
public class AdminController {
    /**
     * Promotes a user.
     *
     * @param accessToken The access token of the user attempting to promote.
     * @param input       A JSON string containing the following key-value pairs:
     *                    {
     *                    "userID": String,  // The user to promote.
     *                    "target": String   // The user level to promote to.
     *                    }
     * @throws BlogException
     */
    private static void promote(String accessToken, JSONObject input) throws BlogException {
        String userID;
        UserLevel target;

        // Read data from JSON
        try {
            userID = input.getString("userID");
            target = UserLevel.valueOf(input.getString("target").toUpperCase());
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BlogException("Target user level is invalid. \n" + e.getMessage());
        }

        // Retrieve the users
        User promoter = User.retrieveByAccessToken(accessToken);
        User promotee = User.retrieveByUserID(userID);

        // Check whether promoter has permission to promote
        if (!promoter.is(ADMIN)) {
            throw new InvalidPermissionException("User must be an admin to change the user level of other users.");
        }

        // Check whether promotee is an admin
        if (promotee.is(ADMIN)) {
            throw new InvalidPermissionException("User cannot change the user level of an admin.");
        }

        // Save the change to database
        Database.promote(userID, target);
    }

    @PutMapping("/promote")
    @ResponseBody
    public ResponseEntity<String> promote(@RequestHeader("Authorization") String accessToken,
                                          @RequestBody String body) {
        try {
            promote(accessToken, new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (IsDeletedException | InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InitializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
