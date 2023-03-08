package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.BlogException;
import com.blog.model.*;
import com.blog.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UserController {

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
    public static void createUser(JSONObject input) throws BlogException{
        String username;
        UserLevel ul;
        String profile_picture;

        try {
            username = input.getString("username");
            ul = UserLevel.READER;
            profile_picture = input.getString("profilepicture");
            String currenttime = Utility.getCurrentTime();

            if (username == null || username.length() == 0) throw new BlogException("Invalid Username");

            User user = new User(User.NEW_USER_ID,
                    username,
                    ul,
                    currenttime,
                    currenttime,
                    UserStatus.ONLINE,
                    profile_picture,
                    false);

            Database.save(user);
        }

        catch (JSONException e){
            throw new BlogException("Failed to read data from json: "+e.getMessage());
        }
        catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }
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
    public static void deletePost(JSONObject input) throws BlogException {
        // Retrieve the post
        User user = retrieveUser(input);

        // Delete post in database
        Database.delete(user);
    }



    /* JSONObject would contain the following key-value pairs:
    1) userID of promoter,
    2) userID of promotee, and
    3) the new user level for the promotee.
    */
    public void changeUserLevel(JSONObject input) throws BlogException{
        int promoterID;
        int promoteeID;

        //READ DATA FROM JSON
        try {
            promoterID = input.getInt("promoterID");
            promoteeID = input.getInt("promoteeID");

            //retrieves info about the user from the database
            User promoter = new User(promoterID);
            User promotee = new User(promoteeID);

            if(promoter.getUserLevel() != UserLevel.ADMIN)
                throw new BlogException("Promoter not authorized to make the required changes");

            String new_level = input.getString("new_level");
            promotee.setUserLevel(UserLevel.valueOf(new_level)); //converting string to enum
            //promotee.UserLevel = UserLevel.valueOf(new_level);

        }
        catch(JSONException e){
            throw new BlogException("Failed to read data from json. \n" + e.getMessage());
        }
        catch (NullPointerException e) {
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

        // Return the retrieved post
        return new User(userID);
    }
    }


