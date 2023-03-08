package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.BlogException;
import com.blog.model.Post;
import com.blog.model.User;
import com.blog.model.UserLevel;
import com.blog.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostController {
    /**
     * Returns a JSON containing the requested post.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The requested post.
     *              }
     * @return The JSON representing the post using the following syntax:
     * {
     * "postID":        int,
     * "authorID":      int,
     * "title":         String,
     * "content":       String,
     * "creationDate":  String,
     * "lastModified":  String,
     * "upvotes":       int,
     * "downvotes":     int,
     * "isDeleted":     boolean,
     * "views":         int,
     * "allowComments": boolean
     * }
     * @throws BlogException
     */
    public static JSONObject getPost(JSONObject input) throws BlogException {
        // Retrieve the post
        Post post = retrievePost(input);

        // Return the JSON response
        return post.asJSONObject();
    }

    /**
     * Returns a JSON containing the requested posts.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postIDStart": int,     // The first requested post.
     *              "count":       int,     // The number of requested posts.
     *              "reverse":     boolean  // Whether to get posts incrementally or decrementally.
     *              }
     * @return The JSON representing the posts using the following syntax:
     * [
     * {                           //
     * "postID":        int,       //
     * "authorID":      int,       //
     * "title":         String,    //
     * "content":       String,    //
     * "creationDate":  String,    //
     * "lastModified":  String,    // <--- This represents one post!
     * "upvotes":       int,       //
     * "downvotes":     int,       //
     * "isDeleted":     boolean,   //
     * "views":         int,       //
     * "allowComments": boolean    //
     * },                          //
     * ...  // The JSON array will contain at most <code>count</code> number of post representations.
     * ]
     * @throws BlogException
     */
    public static JSONArray getPosts(JSONObject input) throws BlogException {
        int postIDStart;
        int count;
        boolean reverse;

        // Read data from JSON
        try {
            postIDStart = input.getInt("postIDStart");
            count = input.getInt("count");
            reverse = input.getBoolean("reverse");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Create array to store retrieved posts
        ArrayList<Post> posts = new ArrayList<>();

        // Retrieve the posts
        Database.retrieve(posts, postIDStart, count, reverse);

        // Build the JSON response
        JSONArray response = new JSONArray();
        for (Post post : posts) {
            response.put(post.asJSONObject());
        }

        // Return the JSON response
        return response;
    }

    /**
     * Creates a new post in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "authorID": int,          // The author of the post.
     *              "title":    String        // The title of the post.
     *              "content":  String        // The content of the post.
     *              "allowcomments": boolean  // Whether to allow comments
     *              }
     * @throws BlogException
     */
    public static void createPost(JSONObject input) throws BlogException {
        int authorID;
        String title;
        String content;
        boolean allowComments;

        // Read data from JSON
        try {
            authorID = input.getInt("authorID");
            title = input.getString("title");
            content = input.getString("content");
            allowComments = input.getBoolean("allowComments");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Check whether author has permission to make a post
        validatePermission(authorID);

        // Validate the data
        Post.validateTitle(title);
        Post.validateContent(content);

        // Create new post
        String currentTime = Utility.getCurrentTime();
        Post post = new Post(
                Post.NEW_POST_ID,
                authorID,
                title,
                content,
                currentTime,
                currentTime,
                0,
                0,
                false,
                0,
                allowComments
        );

        // Save post to database
        Database.save(post);
    }

    /**
     * Deletes a post in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post to delete.
     *              }
     * @throws BlogException
     */
    public static void deletePost(JSONObject input) throws BlogException {
        // Retrieve the post
        Post post = retrievePost(input);

        // Delete post in database
        Database.delete(post);
    }

    /**
     * Edits a post in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":        int,     // The post to edit.
     *              "title":         String   // The new title of the post.
     *              "content":       String   // The new content of the post.
     *              "allowcomments": boolean  // Whether to allow comments
     *              }
     * @throws BlogException
     */
    public static void editPost(JSONObject input) throws BlogException {
        String title;
        String content;
        boolean allowComments;

        // Read data from JSON
        try {
            title = input.getString("title");
            content = input.getString("content");
            allowComments = input.getBoolean("allowComments");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Validate the data
        Post.validateTitle(title);
        Post.validateContent(content);

        // Retrieve the post
        Post post = retrievePost(input);

        // Apply edit to post
        post.setTitle(title);
        post.setContent(content);
        post.setLastModified(Utility.getCurrentTime());
        post.setAllowComments(allowComments);

        // Save post to database
        Database.save(post);
    }

    /**
     * Increments the upvote counter of the post in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post to upvote.
     *              }
     * @throws BlogException
     */
    public static void upvotePost(JSONObject input) throws BlogException {
        // Retrieve the comment
        Post post = retrievePost(input);

        // Apply upvote to post
        post.upvote();

        // Save post to database
        Database.save(post);
    }

    /**
     * Increments the downvote counter of the post in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post to downvote.
     *              }
     * @throws BlogException
     */
    public static void downvotePost(JSONObject input) throws BlogException {
        // Retrieve the post
        Post post = retrievePost(input);

        // Apply downvote to post
        post.downvote();

        // Save post to database
        Database.save(post);
    }

    /**
     * Increments the view counter of the post in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post to increment view counter.
     *              }
     * @throws BlogException
     */
    public static void viewPost(JSONObject input) throws BlogException {
        // Retrieve the post
        Post post = retrievePost(input);

        // Increment view counter of post
        post.view();

        // Save post to database
        Database.save(post);
    }

    /**
     * Retrieves a post from the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post to retrieve.
     *              }
     * @return The retrieved post.
     * @throws BlogException
     */
    private static Post retrievePost(JSONObject input) throws BlogException {
        int postID;

        // Read data from JSON
        try {
            postID = input.getInt("postID");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Return the retrieved post
        return new Post(postID);
    }

    /**
     * Validates whether the user has the necessary permissions to make a post.
     *
     * @param userID The user to validate.
     * @throws BlogException
     */
    private static void validatePermission(int userID) throws BlogException {
        // Retrieve the user
        User user = new User(userID);

        // Check whether the user has UserLevel of at least UserLevel.CONTRIBUTOR
        if (UserLevel.CONTRIBUTOR.compareTo(user.getUserLevel()) < 0) {
            throw new BlogException("User does not have the necessary permission to make a POST.");
        }
    }
}
