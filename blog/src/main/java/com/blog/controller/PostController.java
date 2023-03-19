package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.BlogException;
import com.blog.exception.DoesNotExistException;
import com.blog.exception.InvalidPermissionException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blog.exception.LoginFailedException;

import com.blog.model.Post;
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
public class PostController {

  // Replace the getPost method
@GetMapping("/getPost")
@ResponseBody
public ResponseEntity<String> getPost(@RequestParam("postID") int postID) {
    try {
        JSONObject input = new JSONObject();
        input.put("postID", postID);
        return ResponseEntity.ok(getPost(input).toString());
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}

// Replace the getPosts method
@GetMapping("/getPosts")
@ResponseBody
public ResponseEntity<String> getPosts(@RequestParam("postIDStart") int postIDStart, @RequestParam("count") int count, @RequestParam("reverse") boolean reverse) {
    try {
        JSONObject input = new JSONObject();
        input.put("postIDStart", postIDStart);
        input.put("count", count);
        input.put("reverse", reverse);
        return ResponseEntity.ok(getPosts(input).toString());
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
// @PostMapping("/testMessage")
// public String testMessage(@RequestBody String content, @AuthenticationPrincipal OAuth2User principal) {
//     String userName = principal.getAttribute("name");
//     return content + " " + userName;
// }

@PostMapping("/createPost")
@ResponseBody
public ResponseEntity<String> createPost(@RequestBody String input, @RequestHeader("Authorization") String accessToken) {
    try {
        // Remove the "Bearer " prefix from the access token
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring("Bearer ".length());
        }
        //System.out.println(accessToken);

        // Verify the Google OAuth access token and extract the authorID
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory)
                .setAudience(Collections.singletonList("137046975675-86mneph4bv1sfafa1788famgv2ot695r.apps.googleusercontent.com")) // NEED TO HIDE IT
                .build();

        GoogleIdToken idToken = verifier.verify(accessToken);
        //System.out.println(idToken);
        if (idToken == null) {
            System.out.println("Invalid access token");
            return new ResponseEntity<>("Invalid access token", HttpStatus.UNAUTHORIZED);
        }
        //System.out.println(idToken.getPayload());
        String subject = idToken.getPayload().get("sub").toString();
        //System.out.println("Subject: " + subject);
       
        // int authorID = Integer.parseInt(idToken.getPayload().getSubject());
        // System.out.println(idToken.getPayload());

        JSONObject inputJson = new JSONObject(input);
        // int authorID = Integer.parseInt(subject);
        // System.out.println(authorID);
        inputJson.put("authorID", subject);
       System.out.println(inputJson);
      
        createPost(inputJson);
        return ResponseEntity.ok().build();
    } catch (InvalidPermissionException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

    @DeleteMapping("/deletePost")
    @ResponseBody
    public ResponseEntity<String> deletePost(@RequestBody String input) {
        try {
            deletePost(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/editPost")
    @ResponseBody
    public ResponseEntity<String> editPost(@RequestBody String input) {
        try {
            editPost(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/upvotePost")
    @ResponseBody
    public ResponseEntity<String> upvotePost(@RequestBody String input) {
        try {
            upvotePost(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/downvotePost")
    @ResponseBody
    public ResponseEntity<String> downvotePost(@RequestBody String input) {
        try {
            downvotePost(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/viewPost")
    @ResponseBody
    public ResponseEntity<String> viewPost(@RequestBody String input) {
        try {
            viewPost(new JSONObject(input));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Returns a JSON containing the requested post.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID":    int,  // The requested post.
     *              }
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
     * @throws BlogException
     */
    private static String getPost(JSONObject input) throws BlogException {
        // Retrieve the post
        Post post = retrievePost(input);

        // Return the JSON string
        return post.asJSONString();
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
     * @return The JSON string representing the posts using the following syntax:
     * [
     * {                           //
     * "postID":        int,       //
     * "authorID":      String,    //
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
    private static String getPosts(JSONObject input) throws BlogException {
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
        return response.toString();
    }

    /**
     * Creates a new post in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "authorID":      String,   // The author of the post.
     *              "title":         String,   // The title of the post.
     *              "content":       String,   // The content of the post.
     *              "allowComments": boolean,  // Whether to allow comments
     *              }
     * @return The JSON string representing the created post
     * @throws BlogException
     */
    private static String createPost(JSONObject input) throws BlogException {
        String authorID;
        String title;
        String content;
        boolean allowComments;

        // Read data from JSON
        try {

            authorID = input.getString("authorID");
            title = input.getString("title");
            content = input.getString("content");
            allowComments = input.getBoolean("allowComments");
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        }

        // Retrieve the user
        User user = User.retrieve(authorID);

        // Check whether the user has permission to make a post.
        if (user.getUserLevel().compareTo(UserLevel.GUEST) == 0) {
            throw new InvalidPermissionException("User does not have the necessary permission to make a post.");
        }

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
        int postID = Database.save(post);

        // Indicate that the user is a contributor
        userIsContributor(user);

        // Return the created post
        return Post.retrieve(postID).asJSONString();
    }

    /**
     * Deletes a post in the database.
     *
     * @param input A JSON containing the following key-value pairs:
     *              {
     *              "postID": int,     // The post to delete.
     *              "userID": String,  // The user attempting to delete.
     *              }
     * @throws BlogException
     */
    private static void deletePost(JSONObject input) throws BlogException {
        // Retrieve the post
        Post post = retrievePost(input);

        // Retrieve the user
        User user = UserController.retrieveUser(input);

        // Check whether user has permission to delete post
        if (post.getAuthorID().equals(user.getUserID()) && UserLevel.ADMIN.compareTo(user.getUserLevel()) < 0) {
            throw new InvalidPermissionException("User does not have the necessary permission to delete this post.");
        }

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
     *              "allowComments": boolean  // Whether to allow comments.
     *              "userID":        String,  // The user attempting to edit.
     *              }
     * @throws BlogException
     */
    private static void editPost(JSONObject input) throws BlogException {
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

        // Retrieve the user
        User user = UserController.retrieveUser(input);

        // Check whether user has permission to edit post
        if (post.getAuthorID().equals(user.getUserID()) && UserLevel.ADMIN.compareTo(user.getUserLevel()) < 0) {
            throw new InvalidPermissionException("User does not have the necessary permission to edit this post.");
        }

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
     *              "postID": int,  // The post to upvote.
     *              }
     * @throws BlogException
     */
    private static void upvotePost(JSONObject input) throws BlogException {
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
     *              "postID": int,  // The post to downvote.
     *              }
     * @throws BlogException
     */
    private static void downvotePost(JSONObject input) throws BlogException {
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
     *              "postID": int,  // The post to increment view counter.
     *              }
     * @throws BlogException
     */
    private static void viewPost(JSONObject input) throws BlogException {
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
        return Post.retrieve(postID);
    }

    /**
     * Sets the given user as a contributor.
     *
     * @param user The user to set as a contributor.
     */
    private static void userIsContributor(User user) {
        // Check if user is already a contributor or higher
        if (user.getUserLevel().compareTo(UserLevel.CONTRIBUTOR) >= 0) {
            return;
        }

        // Set the user as a contributor
        user.setUserLevel(UserLevel.CONTRIBUTOR);

        // Save this change to the database
        Database.save(user);
    }

    @GetMapping("/getPost")
    @ResponseBody
    public ResponseEntity<String> getPost(@RequestParam("postID") int postID) {
        try {
            JSONObject input = new JSONObject()
                    .put("postID", postID);
            return ResponseEntity.ok(getPost(input));
        } catch (DoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPosts")
    @ResponseBody
    public ResponseEntity<String> getPosts(@RequestParam("postIDStart") int postIDStart,
                                           @RequestParam("count") int count,
                                           @RequestParam("reverse") boolean reverse) {
        try {
            JSONObject input = new JSONObject()
                    .put("postIDStart", postIDStart)
                    .put("count", count)
                    .put("reverse", reverse);
            return ResponseEntity.ok(getPosts(input));
        } catch (DoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createPost")
    @ResponseBody
    public ResponseEntity<String> createPost(@RequestHeader("Authorization") String accessToken,
                                             @RequestBody String body) {
        try {
            JSONObject input = new JSONObject(body)
                    .put("authorID", LoginController.getUserID(accessToken));
            return ResponseEntity.ok(createPost(input));
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletePost")
    @ResponseBody
    public ResponseEntity<String> deletePost(@RequestHeader("Authorization") String accessToken,
                                             @RequestBody String body) {
        try {
            JSONObject input = new JSONObject(body)
                    .put("userID", LoginController.getUserID(accessToken));
            deletePost(input);
            return ResponseEntity.ok().build();
        } catch (InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/editPost")
    @ResponseBody
    public ResponseEntity<String> editPost(@RequestHeader("Authorization") String accessToken,
                                           @RequestBody String body) {
        try {
            JSONObject input = new JSONObject(body)
                    .put("userID", LoginController.getUserID(accessToken));
            editPost(input);
            return ResponseEntity.ok().build();
        } catch (InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/upvotePost")
    @ResponseBody
    public ResponseEntity<String> upvotePost(@RequestBody String body) {
        try {
            upvotePost(new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/downvotePost")
    @ResponseBody
    public ResponseEntity<String> downvotePost(@RequestBody String body) {
        try {
            downvotePost(new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/viewPost")
    @ResponseBody
    public ResponseEntity<String> viewPost(@RequestBody String body) {
        try {
            viewPost(new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
