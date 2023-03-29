package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.*;
import com.blog.model.Comment;
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

import static com.blog.model.Comment.NEW_COMMENT_ID;
import static com.blog.model.UserLevel.ADMIN;

@RestController
@RequestMapping("/comment")
public class CommentController {
    /**
     * Returns a JSON string containing the requested comment.
     *
     * @param postID    The post containing the requested comment.
     * @param commentID The requested comment.
     * @return The JSON string representing the comment using the following syntax:
     * {
     * "postID":       int,
     * "commentID":    int,
     * "authorID":     String,
     * "content":      String,
     * "creationDate": String,
     * "lastModified": String,
     * "upvotes":      int,
     * "downvotes":    int,
     * "isDeleted":    boolean
     * }
     * @throws DoesNotExistException If the requested comment does not exist.
     */
    private static String getComment(int postID, int commentID) throws DoesNotExistException {
        // Retrieve the comment
        Comment comment = Comment.retrieve(postID, commentID);

        // Return the JSON string
        return comment.asJSONString();
    }

    /**
     * Returns a JSON string containing the requested comments.
     *
     * @param postID         The post containing the requested comments.
     * @param commentIDStart The first requested post.
     * @param count          The number of requested posts.
     * @param reverse        Whether to get posts incrementally or decrementally.
     * @return The JSON string representing the comments using the following syntax:
     * [
     * {                          //
     * "postID":       int,       //
     * "commentID":    int,       //
     * "authorID":     String,    //
     * "content":      String,    //
     * "creationDate": String,    // <--- This represents one comment!
     * "lastModified": String,    //
     * "upvotes":      int,       //
     * "downvotes":    int,       //
     * "isDeleted":    boolean    //
     * },                         //
     * ...  // The JSON array will contain at most <code>count</code> number of comment representations.
     * ]
     */
    private static String getComments(int postID, int commentIDStart, int count, boolean reverse) {
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
        return response.toString();
    }

    /**
     * Creates a new comment in the database.
     *
     * @param accessToken The access token of the user.
     * @param input       A JSON containing the following key-value pairs:
     *                    {
     *                    "postID":      int,     // The post to create the comment in.
     *                    "content":     String   // The content of the comment.
     *                    }
     * @return The JSON string representing the created comment
     * @throws BlogException
     */
    private static String createComment(String accessToken, JSONObject input) throws BlogException {
        int postID;
        String content;

        // Read data from JSON
        try {
            postID = input.getInt("postID");
            content = input.getString("content");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Retrieve the author
        User author = User.retrieveByAccessToken(accessToken);

        // Check whether the author has permission to make a comment
        if (author.is(UserLevel.VIEWER)) {
            throw new InvalidPermissionException("User does not have the necessary permission to make a comment.");
        }

        // Validate the data
        Comment.validateContent(content);

        // Create new comment
        String currentTime = Utility.getCurrentTime();
        Comment comment = new Comment(
                postID,
                NEW_COMMENT_ID,
                author.getUserID(),
                content,
                currentTime,
                currentTime,
                0,
                0,
                false
        );

        // Save comment to database
        Database.save(comment);

        // Return the created comment
        return comment.asJSONString();
    }

    /**
     * Deletes a comment in the database.
     *
     * @param accessToken The access token of the user.
     * @param postID      The post containing the comment to delete.
     * @param commentID   The comment to delete.
     * @throws BlogException
     */
    private static void deleteComment(String accessToken, int postID, int commentID) throws BlogException {
        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Retrieve the comment
        Comment comment = Comment.retrieve(postID, commentID);

        // Check whether user has permission to delete comment
        if (!comment.isAuthoredBy(user) && !user.is(ADMIN)) {
            throw new InvalidPermissionException("User does not have the necessary permission to delete this comment.");
        }

        // Delete comment in database
        Database.delete(comment);
    }

    /**
     * Edits a comment in the database.
     *
     * @param accessToken The access token of the user.
     * @param input       A JSON containing the following key-value pairs:
     *                    {
     *                    "postID":      int,     // The post containing the comment to edit.
     *                    "commentID":   int,     // The comment to edit.
     *                    "content":     String,  // The new content of the comment.
     *                    }
     * @throws BlogException
     */
    private static void editComment(String accessToken, JSONObject input) throws BlogException {
        int postID;
        int commentID;
        String content;

        // Read data from JSON
        try {
            postID = input.getInt("postID");
            commentID = input.getInt("commentID");
            content = input.getString("content");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Validate the data
        Comment.validateContent(content);

        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Retrieve the comment
        Comment comment = Comment.retrieve(postID, commentID);

        // Check whether user has permission to edit comment
        if (!comment.isAuthoredBy(user)) {
            throw new InvalidPermissionException("User does not have the necessary permission to edit this comment.");
        }

        // Apply edit to comment
        comment.setContent(content);
        comment.lastModifiedNow();

        // Save comment to database
        Database.save(comment);
    }

    /**
     * Increments the upvote counter of a comment in the database.
     *
     * @param accessToken The access token of the user.
     * @param postID      The post containing the comment to upvote.
     * @param commentID   The comment to upvote.
     * @throws BlogException
     */
    private static void upvoteComment(String accessToken, int postID, int commentID) throws BlogException {
        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Upvote the comment
        Database.upvote(user.getUserID(), postID, commentID);
    }

    /**
     * Increments the downvote counter of a comment in the database.
     *
     * @param accessToken The access token of the user.
     * @param postID      The post containing the comment to downvote.
     * @param commentID   The comment to downvote.
     * @throws BlogException
     */
    private static void downvoteComment(String accessToken, int postID, int commentID) throws BlogException {
        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Downvote the comment
        Database.downvote(user.getUserID(), postID, commentID);
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<String> get(@RequestParam("postID") int postID,
                                      @RequestParam("commentID") int commentID) {
        try {
            return ResponseEntity.ok(getComment(postID, commentID));
        } catch (DoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/gets")
    @ResponseBody
    public ResponseEntity<String> gets(@RequestParam("postID") int postID,
                                       @RequestParam("commentIDStart") int commentIDStart,
                                       @RequestParam("count") int count,
                                       @RequestParam("reverse") boolean reverse) {
        try {
            return ResponseEntity.ok(getComments(postID, commentIDStart, count, reverse));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> create(@RequestHeader("Authorization") String accessToken,
                                         @RequestBody String body) {
        try {
            return new ResponseEntity<>(createComment(accessToken, new JSONObject(body)), HttpStatus.CREATED);
        } catch (IsDeletedException | InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InitializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String accessToken,
                                         @RequestParam("postID") int postID,
                                         @RequestParam("commentID") int commentID) {
        try {
            deleteComment(accessToken, postID, commentID);
            return ResponseEntity.noContent().build();
        } catch (IsDeletedException | InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InitializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit")
    @ResponseBody
    public ResponseEntity<String> edit(@RequestHeader("Authorization") String accessToken,
                                       @RequestBody String body) {
        try {
            editComment(accessToken, new JSONObject(body));
            return ResponseEntity.noContent().build();
        } catch (IsDeletedException | InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InitializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/upvote")
    @ResponseBody
    public ResponseEntity<String> upvote(@RequestHeader("Authorization") String accessToken,
                                         @RequestParam("postID") int postID,
                                         @RequestParam("commentID") int commentID) {
        try {
            upvoteComment(accessToken, postID, commentID);
            return ResponseEntity.noContent().build();
        } catch (IsDeletedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InitializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/downvote")
    @ResponseBody
    public ResponseEntity<String> downvote(@RequestHeader("Authorization") String accessToken,
                                           @RequestParam("postID") int postID,
                                           @RequestParam("commentID") int commentID) {
        try {
            downvoteComment(accessToken, postID, commentID);
            return ResponseEntity.noContent().build();
        } catch (IsDeletedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InitializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
