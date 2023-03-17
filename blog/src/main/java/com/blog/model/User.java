package com.blog.model;

import com.blog.database.Database;
import com.blog.exception.UserDoesNotExistException;
import com.blog.exception.UserIsDeletedException;
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
    public static final int NEW_USER_ID = 0;
    
    private final int userID;  // TODO: reserve 0 for guest user? Maybe even up to n reserved for testing.
    private String username;
    private UserLevel userLevel;
    private String creationDate;
    private String lastLogin;
    private UserStatus userStatus;
    private String profilePicture;  // URL to the profile picture
    private String bio;

    /**
     * Default guest user constructor.
     */
    public User() {
        this.userID = 0;
        this.userLevel = UserLevel.GUEST;
        creationDate = null;  // TODO: change to current time
    }

    /**
     * Constructor for an existing user.
     *
     * @param userID The unique user identifier.
     */
    public User(int userID) {
        this.userID = userID;
        try {
            Database.retrieve(this);
        } catch (UserDoesNotExistException e) {
            // todo
        } catch (UserIsDeletedException e) {
            // todo
        }
    }

    public User(int userID,
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
     * Returns the JSON representation of this object.
     *
     * @return JSONObject
     */
    public JSONObject asJSONObject() {
        return super.asJSONObject()
                .put("userID", userID)
                .put("username", username)
                .put("userLevel", userLevel.ordinal())
                .put("creationDate", creationDate)
                .put("lastLogin", lastLogin)
                .put("userStatus", userStatus.ordinal())
                .put("profilePicture", profilePicture);
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


    /**
     * Updates the last login time to the current time.
     */
    public void lastLoginNow() {
        lastLogin = Utility.getCurrentTime();
    }

    public int getUserID() {
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

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return this.bio;
    }
}
