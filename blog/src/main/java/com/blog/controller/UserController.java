package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.*;
import com.blog.model.*;
import com.blog.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import static com.blog.model.PromotionRequest.NEW_PROMOTION_REQUEST_ID;
import static com.blog.model.UserLevel.ADMIN;

@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * Returns a JSON string containing the requested user.
     *
     * @param userID The user ID of the requested user.
     * @return The JSON string representing the user using the following syntax:
     * {
     * "userID":         String,
     * "username":       String,
     * "userLevel":      String,
     * "creationDate":   String,
     * "lastLogin":      String,
     * "userStatus":     String,
     * "profilePicture": String,
     * "bio":            String,
     * "isDeleted":      boolean
     * }
     * @throws DoesNotExistException If the requested user does not exist.
     * @throws IsDeletedException    If the requested user is deleted.
     */
    private static String getUserByUserID(String userID) throws DoesNotExistException, IsDeletedException {
        // Retrieve the user
        User user = User.retrieveByUserID(userID);

        // Return the JSON string
        return user.asJSONString();
    }

    /**
     * Returns a JSON string containing the requested user.
     *
     * @param accessToken The access token of the requested user.
     * @return The JSON string representing the user using the following syntax:
     * {
     * "userID":         String,
     * "username":       String,
     * "userLevel":      String,
     * "creationDate":   String,
     * "lastLogin":      String,
     * "userStatus":     String,
     * "profilePicture": String,
     * "bio":            String,
     * "isDeleted":      boolean
     * }
     * @throws LoginFailedException    If the access token is invalid.
     * @throws IsDeletedException      If the requested user is deleted.
     * @throws InitializationException If unable to create GoogleIDTokenVerifier.
     */
    private static String getUserByAccessToken(String accessToken) throws IsDeletedException, LoginFailedException, InitializationException {
        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Return the JSON string
        return user.asJSONString();
    }

    /**
     * Deletes a user in the database.
     *
     * @param accessToken The access token of the user attempting to delete.
     * @param userID      The user to delete.
     */
    private static void deleteUser(String accessToken, String userID) throws BlogException {
        User deleter;
        User user;

        // Retrieve the deleter
        try {
            deleter = User.retrieveByAccessToken(accessToken);
        } catch (IsDeletedException e) {
            throw new IsDeletedException("The user attempting to delete is deleted.");
        }

        // Retrieve the user
        try {
            user = User.retrieveByUserID(userID);
        } catch (IsDeletedException e) {
            throw new IsDeletedException("The user to be deleted is already deleted.");
        }

        // Check whether deleter has permission to delete user
        if (!deleter.is(user) && !deleter.is(ADMIN)) {
            throw new InvalidPermissionException("User does not have the necessary permission to delete this user.");
        }

        // Delete user in database
        Database.delete(user);
    }

    /**
     * Edits a user in the database.
     *
     * @param accessToken The access token of the user attempting to edit.
     * @param input       A JSON string containing the following key-value pairs:
     *                    {
     *                    "userID":         String,  // The user to edit.
     *                    "username":       String,  // The new username of the user.
     *                    "profilePicture": String,  // The new profile picture of the user.
     *                    "bio":            String   // The new bio of the user.
     *                    }
     * @throws BlogException
     */
    private static void editUser(String accessToken, JSONObject input) throws BlogException {
        String userID;
        String username;
        String profilePicture;
        String bio;

        // Read data from JSON
        try {
            userID = input.getString("userID");
            username = input.getString("username");
            profilePicture = input.getString("profilePicture");
            bio = input.getString("bio");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Validate the data
        User.validateUsername(username);
        User.validateBio(bio);

        // Retrieve the users
        User editor = User.retrieveByAccessToken(accessToken);
        User user = User.retrieveByUserID(userID);

        // Check whether editor has permission to edit user
        if (!editor.is(user)) {
            throw new InvalidPermissionException("User does not have the necessary permission to edit this user.");
        }

        // Apply edit to user
        user.setUsername(username);
        user.setProfilePicture(profilePicture);
        user.setBio(bio);

        // Save user to database
        Database.save(user);
    }

    /**
     * Makes a request for a promotion.
     *
     * @param accessToken The user requesting for a promotion.
     * @param input A JSON string containing the following key-value pairs:
     *                    {
     *                    "target": String,  // The target user level to be promoted to.
     *                    "reason": String,  // The reason for the request.
     *                    }
     * @return The JSON string representing the created promotion request.
     * @throws BlogException
     */
    private static String requestPromotion(String accessToken, JSONObject input) throws BlogException {
        UserLevel target;
        String reason;

        // Read data from JSON
        try {
            target = UserLevel.valueOf(input.getString("target").toUpperCase());
            reason = input.getString("reason");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BlogException("Requested target user level is invalid. \n" + e.getMessage());
        }

        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Check whether this request is necessary
        if (!user.below(target)) {
            throw new InvalidPermissionException("User already has an equal or higher user level.");
        }

        // Create new promotion request
        PromotionRequest request = new PromotionRequest(
                NEW_PROMOTION_REQUEST_ID,
                user.getUserID(),
                target,
                Utility.getCurrentTime(),
                reason,
                false
        );

        // Save the request to database
        Database.save(request);

        // Return the created request
        return request.asJSONString();
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<String> get(@RequestHeader(value = "Authorization", required = false) String accessToken,
                                      @RequestParam(value = "userID", required = false) String userID) {
        try {
            if (userID != null) {
                return ResponseEntity.ok(getUserByUserID(userID));
            } else if (accessToken != null) {
                return ResponseEntity.ok(getUserByAccessToken(accessToken));
            } else {
                throw new BlogException("Neither accessToken nor userID was provided.");
            }
        } catch (IsDeletedException e) {
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

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String accessToken,
                                         @RequestParam("userID") String userID) {
        try {
            deleteUser(accessToken, userID);
            return ResponseEntity.noContent().build();
        } catch (IsDeletedException | InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InitializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> edit(@RequestHeader("Authorization") String accessToken,
                                       @RequestBody String body) {
        try {
            editUser(accessToken, new JSONObject(body));
            return ResponseEntity.noContent().build();
        } catch (IsDeletedException | InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InitializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/requestPromotion")
    @ResponseBody
    public ResponseEntity<String> requestPromotion(@RequestHeader("Authorization") String accessToken,
                                                   @RequestBody String body) {
        try {
            return new ResponseEntity<>(requestPromotion(accessToken, new JSONObject(body)), HttpStatus.CREATED);
        } catch (IsDeletedException e) {
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


