package com.blog.controller;

import com.blog.database.Database;
import com.blog.exception.*;
import com.blog.model.PromotionRequest;
import com.blog.model.User;
import com.blog.model.UserLevel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static com.blog.model.UserLevel.ADMIN;

@RestController
@RequestMapping("/admin")
public class AdminController {
    /**
     * Returns a JSON string containing the requested promotion requests.
     *
     * @param accessToken    The access token of the user attempting to get promotion requests.
     * @param requestIDStart The first requested promotion request.
     * @param count          The number of requested promotion requests.
     * @param reverse        Whether to get promotion requests incrementally or decrementally.
     * @return The JSON string representing the promotion requests using the following syntax:
     * [
     * {                         //
     * "requestID":   int,       //
     * "userID":      String,    //
     * "target":      String,    // <--- This represents one promotion request!
     * "requestTime": String,    //
     * "reason":      String     //
     * },                        //
     * ...  // The JSON array will contain at most <code>count</code> number of promotion request representations.
     * ]
     * @throws BlogException
     */
    private static String getPromotionRequests(String accessToken, int requestIDStart, int count, boolean reverse)
            throws BlogException {
        // Validate the admin
        validateAdmin(accessToken);

        // Create array to store retrieved promotion requests
        ArrayList<PromotionRequest> requests = new ArrayList<>();

        // Retrieve the promotion requests
        Database.retrievePromotionRequests(requests, requestIDStart, count, reverse);

        // Build the JSON response
        JSONArray response = new JSONArray();
        for (PromotionRequest request : requests) {
            response.put(request.asJSONObject());
        }

        // Return the JSON response
        return response.toString();
    }

    /**
     * Promotes a user.
     *
     * @param accessToken The access token of the user attempting to promote.
     * @param input       A JSON string containing the following key-value pairs:
     *                    {
     *                    "userID": String,  // The user to promote.
     *                    "target": String   // The user level to promote to.
     *                    }
     * @throws BlogException
     */
    private static void promote(String accessToken, JSONObject input) throws BlogException {
        String userID;
        UserLevel target;

        // Validate the admin
        validateAdmin(accessToken);

        // Read data from JSON
        try {
            userID = input.getString("userID");
            target = UserLevel.valueOf(input.getString("target").toUpperCase());
        } catch (JSONException e) {
            throw new BlogException("Failed to read data from JSON. \n" + e.getMessage());
        } catch (NullPointerException e) {
            throw new BlogException("JSON object received is null. \n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BlogException("Target user level is invalid. \n" + e.getMessage());
        }

        // Retrieve the user
        User user = User.retrieveByUserID(userID);

        // Check whether user can be promoted
        if (user.is(ADMIN)) {
            throw new InvalidPermissionException("Admin cannot change the user level of an admin.");
        }

        // Save the change to database
        Database.promote(userID, target);
    }

    /**
     * Deletes a promotion request in the database.
     *
     * @param accessToken The access token of the user.
     * @param requestID   The promotion request to delete.
     * @throws BlogException
     */
    private static void deletePromotionRequest(String accessToken, int requestID) throws BlogException {
        // Validate the admin
        validateAdmin(accessToken);

        // Retrieve the promotion request
        PromotionRequest request = PromotionRequest.retrieve(requestID);

        // Delete promotion request in database
        Database.delete(request);
    }

    /**
     * Validates that the access token is of a user that is an admin.
     *
     * @param accessToken The access token of the user.
     * @throws LoginFailedException       Invalid access token.
     * @throws IsDeletedException         If the user is deleted.
     * @throws InitializationException    Unable to create GoogleIDTokenVerifier.
     * @throws InvalidPermissionException User is not an admin.
     */
    private static void validateAdmin(String accessToken) throws LoginFailedException, IsDeletedException, InitializationException, InvalidPermissionException {
        // Retrieve the user
        User user = User.retrieveByAccessToken(accessToken);

        // Check whether user is an admin
        if (!user.is(ADMIN)) {
            throw new InvalidPermissionException("User must be an admin to perform this action.");
        }
    }

    @GetMapping("/gets")
    @ResponseBody
    public ResponseEntity<String> gets(@RequestHeader("Authorization") String accessToken,
                                       @RequestParam("requestIDStart") int requestIDStart,
                                       @RequestParam("count") int count,
                                       @RequestParam("reverse") boolean reverse) {
        try {
            return ResponseEntity.ok(getPromotionRequests(accessToken, requestIDStart, count, reverse));
        } catch (IsDeletedException | InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/promote")
    @ResponseBody
    public ResponseEntity<String> promote(@RequestHeader("Authorization") String accessToken,
                                          @RequestBody String body) {
        try {
            promote(accessToken, new JSONObject(body));
            return ResponseEntity.ok().build();
        } catch (IsDeletedException | InvalidPermissionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InitializationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BlogException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String accessToken,
                                         @RequestParam("requestID") int requestID) {
        try {
            deletePromotionRequest(accessToken, requestID);
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
}
