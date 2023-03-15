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
    public ResponseEntity<String> getUser(@RequestBody String input) {
        try {
            return ResponseEntity.ok(getUser(new JSONObject(input)).toString());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createUser")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody String input) {
        try {
            createUser(new JSONObject(input));
            return ResponseEntity.ok(getUser(new JSONObject(input)).toString());
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
            return ResponseEntity.ok(getUser(new JSONObject(input)).toString());
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

//    /**
//     * understand and complete this method
//     * @param input
//     * @return
//     * @throws BlogException
//     */
//    public static JSONArray getUsers(JSONObject input) throws BlogException {
//        int userID;
//        int commentIDStart;
//        int count;
//        boolean reverse;
//
//        // Read data from JSON
//        try {
//            userID = input.getInt("userID");
//            commentIDStart = input.getInt("commentIDStart");
//            count = input.getInt("count");
//            reverse = input.getBoolean("reverse");
//        } catch (JSONException e) {
//            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
//        } catch (NullPointerException e) {
//            throw new BlogException("JSON object received is null. \n" + e.getMessage());
//        }
//
//        // Create array to store retrieved comments
//        ArrayList<Comment> comments = new ArrayList<>();
//
//        // Retrieve the comments
//        Database.retrieve(comments, userID, commentIDStart, count, reverse);
//
//        // Build the JSON response
//        JSONArray response = new JSONArray();
//        for (Comment comment : comments) {
//            response.put(comment.asJSONObject());
//        }
//
//        // Return the JSON response
//        return response;
//    }


    /* JSONObject would contain the following key-value pairs:
    1) username for the new user,
    2) URL for the profile picture (if any), and
    */
    public static void createUser(JSONObject input) throws BlogException {
        String username;
        UserLevel ul;
        String profile_picture;

        try {
            username = input.getString("username");
            ul = UserLevel.READER;
            profile_picture = input.getString("profilepicture");
            String currenttime = Utility.getCurrentTime();

            validateUser(username);

            User user = new User(User.NEW_USER_ID,
                    username,
                    ul,
                    currenttime,
                    currenttime,
                    UserStatus.ONLINE,
                    profile_picture,
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
     * JSONObject would contain the following key-value pairs:
     * userID of promoter,
     * userID of promotee, and
     * the new user level for the promotee
     */
    public void updateUserLevel(JSONObject input) throws BlogException {
        int promoterID;
        int promoteeID;

        //READ DATA FROM JSON
        try {
            promoterID = input.getInt("promoterID");
            promoteeID = input.getInt("promoteeID");

            //retrieves info about the user from the database
            User promoter = new User(promoterID);
            User promotee = new User(promoteeID);

            if (promoter.getUserLevel() != UserLevel.ADMIN)
                throw new BlogException("Promoter not authorized to make the required changes");

            String new_level = input.getString("new_level");
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
    private static User retrieveUser(JSONObject input) throws BlogException {
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
    private static void updateProfilePicture(JSONObject input) throws BlogException {
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

    /* TODO: DELETE?
    private static void updateLastLogin(JSONObject input) throws BlogException {
        User user;
        String last_login;

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
    }*/
}


