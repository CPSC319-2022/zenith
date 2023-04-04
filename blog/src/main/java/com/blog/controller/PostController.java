package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.*;
import com.blog.model.Post;
import com.blog.model.User;
import com.blog.storage.GoogleCloudStorage;
import com.blog.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import static com.blog.model.Post.NEW_POST_ID;
import static com.blog.model.UserLevel.ADMIN;
import static com.blog.model.UserLevel.CONTRIBUTOR;

@RestController
@RequestMapping("/post")
public class PostController {
    /**
     * Returns a JSON string containing the requested post.
     *
     * @param postID The requested post.
     * @return The JSON string representing the post using the following syntax:
     * {
     * "postID":        int,
     * "authorID":      String,
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
     * @throws DoesNotExistException If the requested post does not exist.
     */
    private static String getPost(int postID) throws DoesNotExistException {
        // Retrieve the post
        Post post = Post.retrieve(postID);

        // Return the JSON string
        return post.asJSONString();
    }

    /**
     * Returns a JSON string containing the requested posts.
     *
     * @param postIDStart The first requested post.
     * @param count       The number of requested posts.
     * @param reverse     Whether to get posts incrementally or decrementally.
     * @return The JSON string representing the posts using the following syntax:
     * [
     * {                            //
     * "postID":        int,        //
     * "authorID":      String,     //
     * "title":         String,     //
     * "content":       String,     //
     * "creationDate":  String,     //
     * "lastModified":  String,     // <--- This represents one post!
     * "upvotes":       int,        //
     * "downvotes":     int,        //
     * "isDeleted":     boolean,    //
     * "views":         int,        //
     * "allowComments": boolean     //
     * },                           //
     * ...  // The JSON array will contain at most <code>count</code> number of post representations.
     * ]
     */
    private static String getPosts(int postIDStart, int count, boolean reverse) {
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
        return response.toString();
    }

    /**
     * Creates a new post in the database.
     *
     * @param accessToken The access token of the user.
     * @param input       A JSON containing the following key-value pairs:
     *                    {
     *                    "title":         String,  // The title of the post.
     *                    "content":       String,  // The content of the post.
     *                    "allowComments": boolean  // Whether to allow comments
     *                    }
     * @param file        The thumbnail of the post. Can be null to indicate using default image.
     * @return The JSON string representing the created post.
     * @throws BlogException
     */
    private static String createPost(String accessToken, JSONObject input, MultipartFile file) throws BlogException {
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

        // Retrieve the author
        User author = User.retrieveByAccessToken(accessToken);

        // Check whether the author has permission to make a post
        if (author.below(CONTRIBUTOR)) {
            throw new InvalidPermissionException("User does not have the necessary permission to make a post.");
        }

        // Validate the data
        Post.validateTitle(title);
        Post.validateContent(content);

        // Get the URL for the thumbnail
        String thumbnailURL = GoogleCloudStorage.uploadImage(file);

        // Create new post
        String currentTime = Utility.getCurrentTime();
        Post post = new Post(
                NEW_POST_ID,
                author.getUserID(),
                title,
                content,
                currentTime,
                currentTime,
                0,
                0,
                false,
                0,
                allowComments,
                thumbnailURL
        );

        // Save post to database
        Database.save(post);

        // Return the created post
        return post.asJSONString();
    }

    /**
     * Deletes a post in the database.
     *
     * @param accessToken The access token of the user.
     * @param postID      The post to delete.
     * @throws BlogException
     */
    private static void deletePost(String accessToken, int postID) throws BlogException {
        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Retrieve the post
        Post post = Post.retrieve(postID);

        // Check whether user has permission to delete post
        if (!post.isAuthoredBy(user) && !user.is(ADMIN)) {
            throw new InvalidPermissionException("User does not have the necessary permission to delete this post.");
        }

        // Delete post in database
        Database.delete(post);
    }

    /**
     * Edits a post in the database.
     *
     * @param accessToken The access token of the user.
     * @param input       A JSON containing the following key-value pairs:
     *                    {
     *                    "postID":        int,     // The post to edit.
     *                    "title":         String   // The new title of the post.
     *                    "content":       String   // The new content of the post.
     *                    "allowComments": boolean  // Whether to allow comments.
     *                    }
     * @throws BlogException
     */
    private static void editPost(String accessToken, JSONObject input) throws BlogException {
        int postID;
        String title;
        String content;
        boolean allowComments;

        // Read data from JSON
        try {
            postID = input.getInt("postID");
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

        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Retrieve the post
        Post post = Post.retrieve(postID);

        // Check whether user has permission to edit post
        if (!post.isAuthoredBy(user)) {
            throw new InvalidPermissionException("User does not have the necessary permission to edit this post.");
        }

        // Apply edit to post
        post.setTitle(title);
        post.setContent(content);
        post.lastModifiedNow();
        post.setAllowComments(allowComments);

        // Save post to database
        Database.save(post);
    }

    /**
     * Increments the upvote counter of a post in the database.
     *
     * @param accessToken The access token of the user.
     * @param postID      The post to upvote.
     * @throws BlogException
     */
    private static void upvotePost(String accessToken, int postID) throws BlogException {
        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Upvote the post
        Database.upvote(user.getUserID(), postID);
    }

    /**
     * Increments the downvote counter of a post in the database.
     *
     * @param accessToken The access token of the user.
     * @param postID      The post to upvote.
     * @throws BlogException
     */
    private static void downvotePost(String accessToken, int postID) throws BlogException {
        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Downvote the post
        Database.downvote(user.getUserID(), postID);
    }

    /**
     * Increments the view counter of the post in the database.
     *
     * @param postID The post to upvote.
     * @throws BlogException
     */
    private static void viewPost(int postID) throws BlogException {
        // View the post
        Database.view(postID);
    }

    /**
     * Returns a JSON string containing the requested posts.
     *
     * @param pattern The string to search for.
     * @param start   The row to start returning after sorted.
     * @param count   The number of requested posts.
     * @param sortBy  The method of sorting:
     *                "new":  newest post first
     *                "old":  oldest post first
     *                "top":  top-rated post first (upvote - downvote)
     *                "view": top-viewed post first
     * @return The JSON string representing the posts using the following syntax:
     * [
     * {                            //
     * "postID":        int,        //
     * "authorID":      String,     //
     * "title":         String,     //
     * "content":       String,     //
     * "creationDate":  String,     //
     * "lastModified":  String,     // <--- This represents one post!
     * "upvotes":       int,        //
     * "downvotes":     int,        //
     * "isDeleted":     boolean,    //
     * "views":         int,        //
     * "allowComments": boolean     //
     * },                           //
     * ...  // The JSON array will contain at most <code>count</code> number of post representations.
     * ]
     * @throws BlogException
     */
    private static String searchPosts(String pattern, int start, int count, String sortBy) throws BlogException {
        // Create array to store retrieved posts
        ArrayList<Post> posts = new ArrayList<>();

        // Retrieve the posts
        Database.search(posts, pattern, start, count, sortBy);

        // Build the JSON response
        JSONArray response = new JSONArray();
        for (Post post : posts) {
            response.put(post.asJSONObject());
        }

        // Return the JSON response
        return response.toString();
    }

    /**
     * Returns a JSON string containing the requested posts.
     *
     * @param userID The author of the posts to search for.
     * @param count   The number of requested posts.
     * @return The JSON string representing the posts using the following syntax:
     * [
     * {                            //
     * "postID":        int,        //
     * "authorID":      String,     //
     * "title":         String,     //
     * "content":       String,     //
     * "creationDate":  String,     //
     * "lastModified":  String,     // <--- This represents one post!
     * "upvotes":       int,        //
     * "downvotes":     int,        //
     * "isDeleted":     boolean,    //
     * "views":         int,        //
     * "allowComments": boolean     //
     * },                           //
     * ...  // The JSON array will contain at most <code>count</code> number of post representations.
     * ]
     */
    private static String byUserPosts(String userID, int count) {
        // Create array to store retrieved posts
        ArrayList<Post> posts = new ArrayList<>();

        // Retrieve the posts
        Database.retrieveByUser(posts, userID, count);

        // Build the JSON response
        JSONArray response = new JSONArray();
        for (Post post : posts) {
            response.put(post.asJSONObject());
        }

        // Return the JSON response
        return response.toString();
    }

    /**
     * Returns the highest postID currently in the database.
     *
     * @return The highest postID.
     */
    private static String highestPostID() {
        return String.valueOf(Database.highestPostID());
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<String> get(@RequestParam("postID") int postID) {
        try {
            return ResponseEntity.ok(getPost(postID));
        } catch (DoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/gets")
    @ResponseBody
    public ResponseEntity<String> gets(@RequestParam("postIDStart") int postIDStart,
                                       @RequestParam("count") int count,
                                       @RequestParam("reverse") boolean reverse) {
        try {
            return ResponseEntity.ok(getPosts(postIDStart, count, reverse));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> create(@RequestHeader("Authorization") String accessToken,
                                         @RequestParam("jsonData") String body,
                                         @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            return new ResponseEntity<>(createPost(accessToken, new JSONObject(body), file), HttpStatus.CREATED);
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
                                         @RequestParam("postID") int postID) {
        try {
            deletePost(accessToken, postID);
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
            editPost(accessToken, new JSONObject(body));
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
                                         @RequestParam("postID") int postID) {
        try {
            upvotePost(accessToken, postID);
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
                                           @RequestParam("postID") int postID) {
        try {
            downvotePost(accessToken, postID);
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

    @PutMapping("/view")
    @ResponseBody
    public ResponseEntity<String> view(@RequestParam("postID") int postID) {
        try {
            viewPost(postID);
            return ResponseEntity.noContent().build();
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<String> search(@RequestParam(value = "pattern", defaultValue = "") String pattern,
                                         @RequestParam(value = "start", defaultValue = "1") int start,
                                         @RequestParam(value = "count", defaultValue = "100") int count,
                                         @RequestParam(value = "sortBy", defaultValue = "new") String sortBy) {
        try {
            return ResponseEntity.ok(searchPosts(pattern, start, count, sortBy));
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byUser")
    @ResponseBody
    public ResponseEntity<String> byUser(@RequestParam("userID") String userID,
                                         @RequestParam(value = "count", defaultValue = "10") int count) {
        try {
            return ResponseEntity.ok(byUserPosts(userID, count));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/highest")
    @ResponseBody
    public ResponseEntity<String> highest() {
        try {
            return ResponseEntity.ok(highestPostID());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
