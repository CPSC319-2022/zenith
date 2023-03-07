package com.blog.controller;

import com.blog.exception.BlogException;
import com.blog.model.*;
import org.json.*;
import java.time.Clock;


public class UserController {

    public User createUser(JSONObject input) throws BlogException{
        try{
            String username = input.getString("username");
            UserLevel ul = UserLevel.READER;
            String profile_picture = input.getString("profilepicture");
            Clock creationDate = Clock.systemDefaultZone();
            //validateUser();

            if(username == null || username.length() == 0) throw new BlogException("Invalid Username");

            User u = new User(0, username, ul, creationDate, creationDate, UserStatus.ONLINE, profile_picture, false);
            return u;
        }

        catch (Exception e){

            throw new BlogException("invalid input for create user: "+e.getMessage());
        }
    }

    public void deleteUser(JSONObject input) throws BlogException{

    }

    //SHOULD I CHANGE THE PARAMETERS?
    //params: promoter_id, promotee_id, new_level (json keys)
    public void promoteUser(JSONObject input){
//        try {
//            User promoter = new User(promoterID);
//            User promotee = new User(promoteeID);
//
//            if (promoter.getUserLevel() != UserLevel.ADMIN) throw error;
//
//            if (promoter.userLevel.equals(UserLevel.ADMIN)) {
//                if (promotee.userLevel.equals(UserLevel.GUEST))
//                    promotee.userLevel = UserLevel.READER;
//                else if (promotee.userLevel.equals(UserLevel.READER))
//                    promotee.userLevel = UserLevel.CONTRIBUTOR;
//                else if (promotee.userLevel.equals(UserLevel.CONTRIBUTOR))
//                    promotee.userLevel = UserLevel.ADMIN;
//                else
//                    System.out.println("User is already an ADMIN");
//
//            } else {
//                System.out.println("User " + promoter.userID + "doesn't have the permission to promote another user");
//            }
//            return promotee;
//        }
//            catch(Exception e){
//
//            }

        }
    }


