package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.BlogException;
import com.blog.model.User;
import com.blog.model.UserLevel;
import com.blog.model.UserStatus;
import com.blog.utils.Utility;
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
        User user = retrieveUser(input);

        return user.asJSONObject();
    }

    /**
     * @param name            Username passed from Google OAUTH2 token
     * @param profile_picture profile_picture url passed from Google OAUTH2 token
     * @param userId          A string with max length of 48 that is unique for each user
     * @return User object
     * @throws BlogException when validate user failed or Database has an error
     */

    public static User createUser(String name, String profile_picture, String userId) throws BlogException {
        String currenttime = Utility.getCurrentTime();
        // todo for bio

        validateUser(name);

        User user = new User(userId,
                name,
                UserLevel.READER,
                currenttime,
                currenttime,
                UserStatus.ONLINE,
                profile_picture,
                "",
                false);

        Database.save(user);
        return user;
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
     * Retrieves a user from the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "userID":    string,  // The user to retrieve.
     *              }
     * @return The retrieved User.
     * @throws BlogException
     */

    //todo: change this part
    public static User retrieveUser(JSONObject input) throws BlogException {
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
        return User.retrieve(userID);
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
                user.setLastLoginNow();

            user.setUserStatus(UserStatus.valueOf(status));
            Database.save(user);
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

    }


//    /**
//     * Creates a user in the database
//     *
//     * @param input JSONObject would contain the following key-value pairs:
//     *              {
//     *                  "username": username for the new user,
//     *                  "profilepicture": URL for the profile picture (if any)
//     *              }
//     * @throws BlogException
//     */
//    public static void createUser(JSONObject input) throws BlogException {
//        String username;
//        UserLevel ul;
//        String profile_picture;
//        String bio;
//
//        try {
//            username = input.getString("username");
//            ul = UserLevel.READER;
//            profile_picture = input.getString("profilepicture");
//            String currenttime = Utility.getCurrentTime();
//            bio = "";
//            // todo for bio
//
//            validateUser(username);
//
//            User user = new User(User.NEW_USER_ID,
//                    username,
//                    ul,
//                    currenttime,
//                    currenttime,
//                    UserStatus.ONLINE,
//                    profile_picture,
//                    bio,
//                    false);
//
//            Database.save(user);
//        } catch (JSONException e) {
//            throw new BlogException("Failed to read data from json: " + e.getMessage());
//        } catch (NullPointerException e) {
//            throw new BlogException("JSON object received is null. \n" + e.getMessage());
//        }
//    }

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
        User promoter = User.retrieve(promoterID);
        User promotee = User.retrieve(promoteeID);

        if (promoter.getUserLevel() != UserLevel.ADMIN)
            throw new BlogException("Promoter not authorized to make the required changes");

        new_level = input.getString("new_level");
        promotee.setUserLevel(UserLevel.valueOf(new_level)); //converting string to enum
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

        User user = retrieveUser(input);

        user.setBio(bio);

        Database.save(user);
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


