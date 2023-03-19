package com.blog.model;

import com.blog.database.Database;
import com.blog.exception.DoesNotExistException;
import com.blog.exception.IsDeletedException;
import com.blog.utils.Utility;
import org.json.JSONObject;

/**
 * Class that stores the details of a user.
 * <p>
 * Methods
 * ----------
 * int         getUserID()
 * String      getUsername()
 * void        setUsername(String username)
 * UserLevel   getUserLevel()
 * void        setUserLevel(UserLevel userLevel)
 * Clock       getCreationDate()
 * void        setCreationDate(Clock creationDate)
 * Clock       getLastLogin()
 * void        setLastLogin(Clock lastLogin)
 * UserStatus  getUserStatus()
 * void        setUserStatus(UserStatus userStatus)
 * String      getProfilePicture()
 * void        setProfilePicture(String profilePicture)
 * <p>
 * Inherited Methods
 * ----------
 * boolean     isDeleted()
 * void        setDeleted(boolean deleted)
 */
public class User extends Record {
    private final String userID;
    private String username;
    private UserLevel userLevel;
    private String creationDate;
    private String lastLogin;
    private UserStatus userStatus;
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
        // this.userLevel = userLevel;
        this.creationDate = creationDate;
        this.lastLogin = lastLogin;
        //this.userStatus = userStatus;
        this.profilePicture = profilePicture;
        this.bio = bio;
    }

    /**
     * Factory method to retrieve the user with the given userID.
     *
     * @param userID The user to retrieve.
     * @return The user with the given userID.
     * @throws IsDeletedException If the user is deleted.
     * @throws DoesNotExistException If the user does not exist.
     */
    public static User retrieve(String userID) throws IsDeletedException, DoesNotExistException {
        User user = new User(userID);
        Database.retrieve(user);
        return user;
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
                // .put("userLevel", userLevel.ordinal())
                .put("creationDate", creationDate)
                .put("lastLogin", lastLogin)

                .put("userStatus", userStatus.ordinal())
                .put("profilePicture", profilePicture)
                .put("bio", bio);
    }

    /**
     * Returns the JSON string of this object
     * @return String
     */
    public String asJSONString() {
        return asJSONObject().toString();

    }

    public void copy(User u) {
        this.setUsername(u.getUsername());
        this.setCreationDate(u.getCreationDate());
        this.setLastLogin(u.getLastLogin());
        this.setProfilePicture(u.getProfilePicture());
        this.setUserLevel(u.getUserLevel());
        this.setDeleted(u.isDeleted());
        this.setBio(u.getBio());
    }

    public String getUserID() {
        return userID;
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