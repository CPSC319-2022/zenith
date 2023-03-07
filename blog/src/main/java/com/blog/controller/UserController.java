package com.blog.controller;

import com.blog.exception.BlogException;
import com.blog.model.*;
import org.json.*;
import java.time.Clock;


public class UserController {

    /* JSONObject would contain the following key-value pairs:
    1) username for the new user,
    2) URL for the profile picture (if any), and
    */
    public User createUser(JSONObject input) throws BlogException{
        try{
            String username = input.getString("username");
            UserLevel ul = UserLevel.READER;
            String profile_picture = input.getString("profilepicture");
            Clock creationDate = Clock.systemUTC();
            //validateUser();

            if(username == null || username.length() == 0) throw new BlogException("Invalid Username");

            User u = new User(0, username, ul, "", "", UserStatus.ONLINE, profile_picture, false);
            return u;
        }

        catch (JSONException e){
            throw new BlogException("Failed to read data from json: "+e.getMessage());
        }
        catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }
    }
    /* JSONObject would contain the following key-value pairs:
        1) UserID of the user to be deleted
    */
    public void deleteUser(JSONObject input) throws BlogException{
    }

    /* JSONObject would contain the following key-value pairs:
    1) userID of promoter,
    2) userID of promotee, and
    3) the new user level for the promotee.
    */
    public void changeUserLevel(JSONObject input) throws BlogException{

        try {
            int promoterID = input.getInt("promoterID");
            int promoteeID = input.getInt("promoteeID");

            //retrieves info about the user from the database
            User promoter = new User(promoterID);
            User promotee = new User(promoteeID);

            if(promoter.getUserLevel() != UserLevel.ADMIN)
                throw new BlogException("Promoter not authorized to make the required changes");

            String new_level = input.getString("new_level");
            promotee.setUserLevel(UserLevel.valueOf(new_level));
            //promotee.UserLevel = UserLevel.valueOf(new_level);

        }
        catch(JSONException e){
            throw new BlogException("Failed to read data from json. \n" + e.getMessage());
        }
        catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }
    }
    }


