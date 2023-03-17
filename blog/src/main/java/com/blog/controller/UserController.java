package com.blog.controller;

import com.blog.database.Database;
import com.blog.database.Database;
import com.blog.exception.BlogException;
import com.blog.model.*;
import com.blog.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class UserController {
    @GetMapping("/getUser")
    @ResponseBody
    public ResponseEntity<String> getUser(@RequestParam("userID") int userID) {
        try {
            JSONObject input = new JSONObject();
            input.put("userID", userID);
            return ResponseEntity.ok(getUser(input).toString());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createUser")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody String input) {
        try {
            createUser(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteUser")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestBody String input) {
        try {
            deleteUser(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUserLevel")
    @ResponseBody
    public ResponseEntity<String> updateUserLevel(@RequestBody String input) {
        try {
            updateUserLevel(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUserStatus")
    @ResponseBody
    public ResponseEntity<String> updateUserStatus(@RequestBody String input) {
        try {
            updateUserStatus(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateProfilePicture")
    @ResponseBody
    public ResponseEntity<String> updateProfilePicture(@RequestBody String input) {
        try {
            updateProfilePicture(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Returns a json containing the requested user
    public static JSONObject getUser(JSONObject input) throws BlogException {
        // Retrieve the user
        User user = retrieveUser(input);

        return user.asJSONObject();
    }



    /**
     * Creates a user in the database
     *
     * @param input JSONObject would contain the following key-value pairs:
     *              {
     *                  "username": username for the new user,
     *                  "profilepicture": URL for the profile picture (if any)
     *              }
     * @throws BlogException
     */
    public static void createUser(JSONObject input) throws BlogException {
        String username;
        UserLevel ul;
        String profile_picture;
        String bio;

        try {
            username = input.getString("username");
            ul = UserLevel.READER;
            profile_picture = input.getString("profilepicture");
            String currenttime = Utility.getCurrentTime();
            bio = "";
            // todo for bio

            validateUser(username);

            User user = new User(User.NEW_USER_ID,
                    username,
                    ul,
                    currenttime,
                    currenttime,
                    UserStatus.ONLINE,
                    profile_picture,
                    bio,
                    false);

            Database.save(user);
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from json: " + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }
    }

    //helper method
    private static void validateUser(String username) throws BlogException {
        if (username == null || username.length() == 0)
            throw new BlogException("Username not valid");
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
        User user = retrieveUser(input);

        // Delete user in database
        Database.delete(user);
    }


    /**
     * Updates user level of the promotee only if the promoter has the authorization to do so.
     * @param input JSONObject would contain the following key-value pairs:
     *              {
     *              "promoterID": userID of promoter,
     *              "promoteeID": userID of promotee, and
     *              "new_level": the new user level for the promotee
     *              }
     * @throws BlogException
     */
    public void updateUserLevel(JSONObject input) throws BlogException {
        int promoterID;
        int promoteeID;
        String new_level;
        //READ DATA FROM JSON
        try {
            promoterID = input.getInt("promoterID");
            promoteeID = input.getInt("promoteeID");

            //retrieves info about the user from the database
            User promoter = new User(promoterID);
            User promotee = new User(promoteeID);

            if (promoter.getUserLevel() != UserLevel.ADMIN)
                throw new BlogException("Promoter not authorized to make the required changes");

            new_level = input.getString("new_level");
            promotee.setUserLevel(UserLevel.valueOf(new_level)); //converting string to enum
            //promotee.UserLevel = UserLevel.valueOf(new_level);

        } catch (JSONException e) {
            throw new BlogException("Failed to read data from json. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }
    }


    /**
     * Retrieves a user from the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "userID":    int,  // The user to retrieve.
     *              }
     * @return The retrieved User.
     * @throws BlogException
     */
    public static User retrieveUser(JSONObject input) throws BlogException {
        int userID;

        // Read data from JSON
        try {
            userID = input.getInt("userID");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Return the retrieved user
        return new User(userID);
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
            user = retrieveUser(input);
            status = input.getString("user_status");

            if (status.toUpperCase() == "OFFLINE")
                user.setLastLogin(Utility.getCurrentTime());

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
            user = retrieveUser(input);
            profile_picture = input.getString("profile_picture");
            user.setProfilePicture(profile_picture);
            Database.save(user);
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }
    }
}


