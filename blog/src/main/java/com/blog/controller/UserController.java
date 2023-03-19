package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.BlogException;
import com.blog.model.User;
import com.blog.model.UserLevel;
import com.blog.model.UserStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    //Returns a json containing the requested user
    public static JSONObject getUser(JSONObject input) throws BlogException {
        // Retrieve the user
        User user = retrieveUserByUserID(input);

        return user.asJSONObject();
    }

    /**
     * Deletes a user in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "userID":    int,  // The user to delete.
     *              }
     * @throws BlogException
     */
    public static void deleteUser(JSONObject input) throws BlogException {
        // Retrieve the user
        User user = retrieveUserByUserID(input);

        // Delete user in database
        Database.delete(user);
    }

    /**
     * Retrieves a user from the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "userID":    string,  // The user to retrieve.
     *              }
     * @return The retrieved User.
     * @throws BlogException
     */
    public static User retrieveUserByUserID(JSONObject input) throws BlogException {
        String userID;

        // Read data from JSON
        try {
            userID = input.getString("userID");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Return the retrieved user
        return User.retrieveByUserID(userID);
    }

    /**
     * Retrieves a user from the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "accessToken":  String,  // The access token of the user to retrieve.
     *              }
     * @return The retrieved User.
     * @throws BlogException
     */

    //todo: change this part
    public static User retrieveUserByAccessToken(JSONObject input) throws BlogException {
        String accessToken;

        // Read data from JSON
        try {
            accessToken = input.getString("accessToken");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Return the retrieved user
        return User.retrieveByAccessToken(accessToken);
    }

    /**
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "userID":    int,     // The user to retrieve.
     *              "user_status": String // New user status of the user
     *              }
     * @return Updates the User Status and saves the changes in the database
     * @throws BlogException
     */
    private static void updateUserStatus(JSONObject input) throws BlogException {
        User user;
        String status;

        try {
            //userID = input.getInt("userID");
            user = retrieveUserByUserID(input);
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
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "userID":    int,     // The user to retrieve.
     *              "profile_picture": String // URL of the new profile picture of the user
     *              }
     * @return Updates the profile picture and saves the changes in the database
     * @throws BlogException
     */
    public static void updateProfilePicture(JSONObject input) throws BlogException {
        User user;
        String profile_picture;

        try {
            user = retrieveUserByUserID(input);
            profile_picture = input.getString("profile_picture");
            user.setProfilePicture(profile_picture);
            Database.save(user);
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }
    }

    /**
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "userID": int,    // The user to update.
     *              "bio":    String  // The content of the user bio.
     *              }
     * @return Updates the bio of the user and saves the changes in the database
     * @throws BlogException
     */
    public static void updateBio(JSONObject input) throws BlogException {
        String bio;

        try {
            bio = input.getString("bio");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        User user = retrieveUserByUserID(input);

        user.setBio(bio);

        Database.save(user);
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
     */
    public void updateUserLevel(JSONObject input) throws BlogException {
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

        if (promoter.getUserLevel() != UserLevel.ADMIN)
            throw new BlogException("Promoter not authorized to make the required changes");

        new_level = input.getString("new_level");
        promotee.setUserLevel(UserLevel.valueOf(new_level)); //converting string to enum
    }

    @GetMapping("/getUser")
    @ResponseBody
    public ResponseEntity<String> getUser(@RequestParam("userID") int userID) {
        try {
            JSONObject input = new JSONObject()
                    .put("userID", userID);
            return ResponseEntity.ok(getUser(input).toString());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUser")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestBody String body) {
        try {
            deleteUser(new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUserLevel")
    @ResponseBody
    public ResponseEntity<String> updateUserLevel(@RequestBody String body) {
        try {
            updateUserLevel(new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUserStatus")
    @ResponseBody
    public ResponseEntity<String> updateUserStatus(@RequestBody String body) {
        try {
            updateUserStatus(new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateProfilePicture")
    @ResponseBody
    public ResponseEntity<String> updateProfilePicture(@RequestBody String body) {
        try {
            updateProfilePicture(new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateBio")
    @ResponseBody
    public ResponseEntity<String> updateBio(@RequestBody String body) {
        try {
            updateBio(new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}


