package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.BlogException;
import com.blog.model.Comment;
import com.blog.model.Content;
import com.blog.model.User;
import com.blog.model.UserLevel;
import com.blog.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentController {
    /**
     * Return a JSON containing the comment requested to be viewed.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post containing the comment to view.
     *              "commentID": int   // The comment to view.
     *              }
     * @return The JSON representing the comment using the following syntax:
     * {
     * "postID":       int,
     * "commentID":    int,
     * "authorID":     int,
     * "content":      String,
     * "creationDate": String,
     * "lastModified": String,
     * "upvotes":      int,
     * "downvotes":    int,
     * "isDeleted":    boolean
     * }
     * @throws BlogException
     */
    public static JSONObject viewComment(JSONObject input) throws BlogException {
        // Retrieve the comment
        Comment comment = retrieveComment(input);

        // Return the JSON response
        return comment.asJSONObject();
    }

    /**
     * Return a JSON containing the comments requested to be viewed.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":         int,     // The post containing the comments to view.
     *              "commentIDStart": int,     // The first comment to view.
     *              "count":          int,     // The number of comments to view.
     *              "reverse":        boolean  // Whether to view comments incrementally or decrementally.
     *              }
     * @return The JSON representing the comments using the following syntax:
     * [
     * {                          //
     * "postID":       int,       //
     * "commentID":    int,       //
     * "authorID":     int,       //
     * "content":      String,    //
     * "creationDate": String,    // This represents one comment!
     * "lastModified": String,    //
     * "upvotes":      int,       //
     * "downvotes":    int,       //
     * "isDeleted":    boolean    //
     * },                         //
     * ...  // The JSON array will contain <code>count</code> number of comment representations.
     * ]
     * @throws BlogException
     */
    public static JSONArray viewComments(JSONObject input) throws BlogException {
        int postID;
        int commentIDStart;
        int count;
        boolean reverse;

        // Read data from JSON
        try {
            postID = input.getInt("postID");
            commentIDStart = input.getInt("commentIDStart");
            count = input.getInt("count");
            reverse = input.getBoolean("reverse");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Create array to store retrieved comments
        ArrayList<Comment> comments = new ArrayList<>();

        // Retrieve the comments
        Database.retrieve(comments, postID, commentIDStart, count, reverse);

        // Build the JSON response
        JSONArray response = new JSONArray();
        for (Comment comment : comments) {
            response.put(comment.asJSONObject());
        }

        // Return the JSON response
        return response;
    }

    /**
     * Creates a new comment in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":   int,    // The post to create the comment in.
     *              "authorID": int,    // The author of the comment.
     *              "content":  String  // The content of the comment.
     *              }
     * @throws BlogException
     */
    public static void createComment(JSONObject input) throws BlogException {
        int postID;
        int authorID;
        String content;

        // Read data from JSON
        try {
            postID = input.getInt("postID");
            authorID = input.getInt("authorID");
            content = input.getString("content");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Check whether author has permission to comment
        validatePermission(authorID);

        // Validate the data
        Comment.validateContent(content);

        // Create new comment
        String currentTime = Utility.getCurrentTime();
        Comment comment = new Comment(
                postID,
                0,
                authorID,
                content,
                currentTime,
                currentTime,
                0,
                0,
                false
        );

        // Save comment to database
        Database.save(comment);
    }

    /**
     * Deletes a comment in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post containing the comment to delete.
     *              "commentID": int   // The comment to delete.
     *              }
     * @throws BlogException
     */
    public static void deleteComment(JSONObject input) throws BlogException {
        // Retrieve the comment
        Comment comment = retrieveComment(input);

        // Delete comment in database
        Database.delete(comment);
    }

    /**
     * Edits a comment in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,    // The post containing the comment to edit.
     *              "commentID": int,    // The comment to edit.
     *              "content":   String  // The new content of the comment.
     *              }
     * @throws BlogException
     */
    public static void editComment(JSONObject input) throws BlogException {
        String content;

        // Read data from JSON
        try {
            content = input.getString("content");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Validate the data
        Comment.validateContent(content);

        // Retrieve the comment
        Comment comment = retrieveComment(input);

        // Apply edit to comment
        comment.setContent(content);
        comment.setLastModified(Utility.getCurrentTime());

        // Save comment to database
        Database.save(comment);
    }

    /**
     * Increments the upvote counter of the comment in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post containing the comment to upvote.
     *              "commentID": int   // The comment to upvote.
     *              }
     * @throws BlogException
     */
    public static void upvoteComment(JSONObject input) throws BlogException {
        // Retrieve the comment
        Comment comment = retrieveComment(input);

        // Apply upvote to comment
        comment.upvote();

        // Save comment to database
        Database.save(comment);
    }

    /**
     * Increments the downvote counter of the comment in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post containing the comment to downvote.
     *              "commentID": int   // The post to downvote.
     *              }
     * @throws BlogException
     */
    public static void downvoteComment(JSONObject input) throws BlogException {
        // Retrieve the comment
        Comment comment = retrieveComment(input);

        // Apply downvote to comment
        comment.downvote();

        // Save comment to database
        Database.save(comment);
    }

    /**
     * Retrieves a comment from the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post containing the comment to retrieve.
     *              "commentID": int   // The comment to retrieve.
     *              }
     * @return The retrieved comment.
     * @throws BlogException
     */
    private static Comment retrieveComment(JSONObject input) throws BlogException {
        int postID;
        int commentID;

        // Read data from JSON
        try {
            postID = input.getInt("postID");
            commentID = input.getInt("commentID");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Return the retrieved comment
        return new Comment(postID, commentID);
    }

    /**
     * Validates whether the user has the necessary permissions to make a comment.
     *
     * @param userID The user to validate.
     * @throws BlogException
     */
    private static void validatePermission(int userID) throws BlogException {
        // Retrieve the user
        User user = new User(userID);

        // Check whether the user has UserLevel of at least UserLevel.READER
        if (UserLevel.READER.compareTo(user.getUserLevel()) < 0) {
            throw new BlogException("User does not have the necessary permission to make a comment.");
        }
    }
}
