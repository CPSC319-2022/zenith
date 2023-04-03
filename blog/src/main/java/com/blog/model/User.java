package com.blog.model;

import com.blog.database.Database;
import com.blog.exception.*;
import com.blog.utils.Utility;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Class that stores the details of a user.
 */
public class User extends Record {
    private static final int MIN_USERNAME_LENGTH = 1;
    private static final int MIN_BIO_LENGTH = 1;

    private static GoogleIdTokenVerifier VERIFIER;

    static {
        // Create the GoogleIDTokenVerifier
        try {
            VERIFIER = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("987709227897-c7nj5pgs1t4e68q26slqf5prtabhhe7n.apps.googleusercontent.com"))  // TODO: hide this
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            VERIFIER = null;
        }
    }

    private String userID;
    private String username;
    private UserLevel userLevel;
    private String creationDate;
    private String lastLogin;       // TODO: change to lastActivity?
    private UserStatus userStatus;  // TODO: deprecate
    private String profilePicture;  // URL to the profile picture
    private String bio;

    public User(String userID) {
        this.userID = userID;
    }

    public User(String userID,
                String username,
                UserLevel userLevel,
                String creationDate,
                String lastLogin,
                UserStatus userStatus,
                String profilePicture,
                String bio,
                boolean isDeleted) {
        super(isDeleted);
        this.userID = userID;
        this.username = username;
        this.userLevel = userLevel;
        this.creationDate = creationDate;
        this.lastLogin = lastLogin;
        this.userStatus = userStatus;
        this.profilePicture = profilePicture;
        this.bio = bio;
    }

    /**
     * Factory method to retrieve the user with the given userID.
     *
     * @param userID The user to retrieve.
     * @return The user with the given userID.
     * @throws IsDeletedException    If the user is deleted.
     * @throws DoesNotExistException If the user does not exist.
     */
    public static User retrieveByUserID(String userID) throws IsDeletedException, DoesNotExistException {
        User user = new User(userID);
        Database.retrieve(user);
        return user;
    }

    /**
     * Factory method to retrieve the user with the given Google access token.
     * Will create the user if it does not exist.
     *
     * @param accessToken The user to retrieve.
     * @return The user with the given access token.
     * @throws IsDeletedException      If the user is deleted.
     * @throws LoginFailedException    Invalid access token.
     * @throws InitializationException Unable to create GoogleIDTokenVerifier.
     */
    public static User retrieveByAccessToken(String accessToken) throws IsDeletedException, LoginFailedException, InitializationException {
        GoogleIdToken.Payload payload = getPayload(accessToken);

        String userID = payload.getSubject();

        User user = new User(userID);
        try {
            Database.retrieve(user);
        } catch (DoesNotExistException e) {
            String currentTime = Utility.getCurrentTime();

            user.setUsername((String) payload.get("name"));
            user.setUserLevel(UserLevel.READER);
            user.setCreationDate(currentTime);
            user.setLastLogin(currentTime);
            user.setUserStatus(UserStatus.ONLINE);
            user.setProfilePicture((String) payload.get("picture"));
            user.setBio("");
            user.setDeleted(false);

            Database.save(user);
        }

        return user;
    }

    /**
     * Validates the given username.
     *
     * @param username The username to validate.
     * @throws BlogException If the username is invalid.
     */
    public static void validateUsername(String username) throws BlogException {
        if (username.length() < MIN_USERNAME_LENGTH) {
            throw new BlogException("Username is too short.");
        }
    }

    /**
     * Validates the given bio.
     *
     * @param bio The bio to validate.
     * @throws BlogException If the bio is invalid.
     */
    public static void validateBio(String bio) throws BlogException {
        if (bio.length() < MIN_BIO_LENGTH) {
            throw new BlogException("Bio is too short.");
        }
    }

    /**
     * Gets the payload from the given Google access token.
     *
     * @param accessToken The Google access token.
     * @return The payload.
     * @throws LoginFailedException    Invalid access token.
     * @throws InitializationException Unable to create GoogleIDTokenVerifier.
     */
    private static GoogleIdToken.Payload getPayload(String accessToken) throws LoginFailedException, InitializationException {
        if (accessToken == null) {
            throw new LoginFailedException("The access token provided is null.");
        }

        // Remove the "Bearer " prefix from the access token
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring("Bearer ".length());
        }

        // Verify the access token
        GoogleIdToken idToken;
        try {
            idToken = VERIFIER.verify(accessToken);
        } catch (GeneralSecurityException | IOException e) {
            throw new LoginFailedException("Invalid access token.");
        } catch (NullPointerException e) {
            throw new InitializationException("Unable to create GoogleIDTokenVerifier.");
        }

        // Check if verification succeeded
        if (idToken == null) {
            throw new LoginFailedException("Failed to verify access token.");
        }

        // Return the userID
        return idToken.getPayload();
    }

    /**
     * Returns the JSON representation of this object.
     *
     * @return JSONObject
     */
    public JSONObject asJSONObject() {
        return super.asJSONObject()
                .put("userID", userID)
                .put("username", username)
                .put("userLevel", userLevel)
                .put("creationDate", creationDate)
                .put("lastLogin", lastLogin)
                .put("userStatus", userStatus)
                .put("profilePicture", profilePicture)
                .put("bio", bio);
    }

    /**
     * Returns the JSON string of this object
     *
     * @return String
     */
    public String asJSONString() {
        return asJSONObject().toString();
    }

    /**
     * @param user The user to check against.
     * @return Whether this user is the same user (same userID).
     */
    public boolean is(User user) {
        return getUserID().equals(user.getUserID());
    }

    /**
     * @param userLevel The user level to check against.
     * @return Whether this user is of the given user level.
     */
    public boolean is(UserLevel userLevel) {
        return getUserLevel() == userLevel;
    }

    /**
     * @param userLevel The user level to check against.
     * @return Whether this user is below the given user level.
     */
    public boolean below(UserLevel userLevel) {
        return getUserLevel().below(userLevel);
    }

    /**
     * @param userLevel The user level to check against.
     * @return Whether this user is above the given user level.
     */
    public boolean above(UserLevel userLevel) {
        return getUserLevel().above(userLevel);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(UserLevel userLevel) {
        this.userLevel = userLevel;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Updates the last login time to the current time.
     */
    public void setLastLoginNow() {
        lastLogin = Utility.getCurrentTime();
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}