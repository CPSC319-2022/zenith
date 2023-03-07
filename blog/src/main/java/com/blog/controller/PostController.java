package com.blog.controller;

import com.blog.exception.BlogException;
import com.blog.model.Post;
import org.json.*;

import java.time.Clock;

public class PostController {


    //todo:after we have database ready, we will change signature to return void
    //todo:connect front end and database
    public Post createPost(JSONObject input) throws BlogException {
        try{
            String title = input.getString("title");
            String content = input.getString("content");
            int authorID = input.getInt("authorID");
            boolean allowComments = input.getBoolean("allowComments");
            validatePost(title, content, authorID);
            Clock time = Clock.systemDefaultZone();
            Post p = new Post(Post.newPostIDFlag,authorID,title,content,time,time,
                    0,0,false,0,allowComments);
            return p;
        } catch (Exception e){
            throw new BlogException("invalid input for create post: "+e.getMessage());
        }
    }

    //todo: delete

    //todo: modify

    public boolean validatePost(String title,String content, int authorID) throws BlogException {
        if(title == null || title.length() != 0) throw new BlogException("invalid title");
        if(content== null || content.length() != 0) throw new BlogException("invalid content");
        if(UserController.isValidContributor(UserController.getUserByID(authorID)))
            throw new BlogException("invalid user level");
        return true;
    }
}
