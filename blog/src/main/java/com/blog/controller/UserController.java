package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.*;
import com.blog.model.User;
import com.blog.model.UserLevel;
import com.blog.model.UserStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @throws BlogException
     */
    private static void requestPromotion(String accessToken, JSONObject input) throws BlogException {
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
            throw new BlogException("User already has an equal or higher user level.");
        }

        // Save the request to database
        Database.requestPromotion(user.getUserID(), target, reason);
    }

    /**
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "userID":    int,     // The user to retrieve.
     *              "user_status": String // New user status of the user
     *              }
     * @return Updates the User Status and saves the changes in the database
     * @throws BlogException
     * @deprecated Feature to be removed.
     */
    @Deprecated
    private static void updateUserStatus(JSONObject input) throws BlogException {
        User user;
        String status;

        try {
            String userID = input.getString("userID");
            user = User.retrieveByUserID(userID);
            status = input.getString("user_status");

            if (status.toUpperCase() == "OFFLINE")
                user.setLastLoginNow();

            user.setUserStatus(UserStatus.valueOf(status));
            Database.save(user);
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }
    }

    /**
     * Updates user level of the promotee only if the promoter has the authorization to do so.
     *
     * @param input JSONObject would contain the following key-value pairs:
     *              {
     *              "promoterID": userID of promoter,
     *              "promoteeID": userID of promotee, and
     *              "new_level": the new user level for the promotee
     *              }
     * @throws BlogException
     * @deprecated Feature moved to AdminController.
     */
    @Deprecated
    private void updateUserLevel(JSONObject input) throws BlogException {
        String promoterID;
        String promoteeID;
        String new_level;
        //READ DATA FROM JSON
        try {
            promoterID = input.getString("promoterID");
            promoteeID = input.getString("promoteeID");

        } catch (JSONException e) {
            throw new BlogException("Failed to read data from json. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        //retrieves info about the user from the database
        User promoter = User.retrieveByUserID(promoterID);
        User promotee = User.retrieveByUserID(promoteeID);

        if (promoter.getUserLevel() != ADMIN)
            throw new BlogException("Promoter not authorized to make the required changes");

        new_level = input.getString("new_level");
        promotee.setUserLevel(UserLevel.valueOf(new_level)); //converting string to enum
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<String> get(@RequestHeader(value = "Authorization", required = false) String accessToken,
                                      @RequestParam(value = "userID", required = false) String userID) {
        try {
            if (accessToken != null) {
                return ResponseEntity.ok(getUserByAccessToken(accessToken));
            } else if (userID != null) {
                return ResponseEntity.ok(getUserByUserID(userID));
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
        } catch (IsDeletedException e) {
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
            requestPromotion(accessToken, new JSONObject(body));
            return ResponseEntity.ok().build();
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


