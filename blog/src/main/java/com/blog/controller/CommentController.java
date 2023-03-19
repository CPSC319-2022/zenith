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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class CommentController {
    @GetMapping("/getComment")
    @ResponseBody
    public ResponseEntity<String> getComment(@RequestParam("postID") int postID, @RequestParam("commentID") int commentID) {
        try {
            JSONObject input = new JSONObject();
            input.put("postID", postID);
            input.put("commentID", commentID);
            return ResponseEntity.ok(getComment(input).toString());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getComments")
    @ResponseBody
    public ResponseEntity<String> getComments(@RequestParam("postID") int postID, @RequestParam("commentIDStart") int commentIDStart, @RequestParam("count") int count, @RequestParam("reverse") boolean reverse) {
        try {
            JSONObject input = new JSONObject();
            input.put("postID", postID);
            input.put("commentIDStart", commentIDStart);
            input.put("count", count);
            input.put("reverse", reverse);
            return ResponseEntity.ok(getComments(input).toString());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createComment")
    @ResponseBody
    public ResponseEntity<String> createComment(@RequestBody String input) {
        try {
            createComment(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteComment")
    @ResponseBody
    public ResponseEntity<String> deleteComment(@RequestBody String input) {
        try {
            deleteComment(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/editComment")
    @ResponseBody
    public ResponseEntity<String> editComment(@RequestBody String input) {
        try {
            editComment(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/upvoteComment")
    @ResponseBody
    public ResponseEntity<String> upvoteComment(@RequestBody String input) {
        try {
            upvoteComment(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/downvoteComment")
    @ResponseBody
    public ResponseEntity<String> downvoteComment(@RequestBody String input) {
        try {
            downvoteComment(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns a JSON containing the requested comment.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The post containing the requested comment.
     *              "commentID": int   // The requested comment.
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
    private static JSONObject getComment(JSONObject input) throws BlogException {
        // Retrieve the comment
        Comment comment = retrieveComment(input);

        // Return the JSON response
        return comment.asJSONObject();
    }

    /**
     * Returns a JSON containing the requested comments.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":         int,     // The post containing the requested comments.
     *              "commentIDStart": int,     // The first requested comment.
     *              "count":          int,     // The number of requested comments.
     *              "reverse":        boolean  // Whether to get comments incrementally or decrementally.
     *              }
     * @return The JSON representing the comments using the following syntax:
     * [
     * {                          //
     * "postID":       int,       //
     * "commentID":    int,       //
     * "authorID":     int,       //
     * "content":      String,    //
     * "creationDate": String,    // <--- This represents one comment!
     * "lastModified": String,    //
     * "upvotes":      int,       //
     * "downvotes":    int,       //
     * "isDeleted":    boolean    //
     * },                         //
     * ...  // The JSON array will contain at most <code>count</code> number of comment representations.
     * ]
     * @throws BlogException
     */
    private static JSONArray getComments(JSONObject input) throws BlogException {
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
    private static void createComment(JSONObject input) throws BlogException {
        int postID;
        int authorID;
        String content;

        // Check if input is null
    if (input == null) {
        throw new BlogException("JSON object received is null.");
    }

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

        // // Check whether author has permission to comment
        // validatePermission(authorID);

        // Validate the data
        Comment.validateContent(content);

        // Create new comment
        String currentTime = Utility.getCurrentTime();
        Comment comment = new Comment(
                postID,
                Comment.NEW_COMMENT_ID,
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
    private static void deleteComment(JSONObject input) throws BlogException {
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
    private static void editComment(JSONObject input) throws BlogException {
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
    private static void upvoteComment(JSONObject input) throws BlogException {
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
    private static void downvoteComment(JSONObject input) throws BlogException {
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
