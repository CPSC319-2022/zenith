package com.blog.model;

import com.blog.database.Database;

import java.time.Clock;

/**
 * Class that stores the details of a user.
 *
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
 *
 * Inherited Methods
 * ----------
 * boolean     isDeleted()
 * void        setDeleted(boolean deleted)
 */
public class User extends Record {
    private final int userID;  // TODO: reserve 0 for guest user? Maybe even up to n reserved for testing.
    private String username;
    private UserLevel userLevel;
    private Clock creationDate;
    private Clock lastLogin;
    private UserStatus userStatus;
    private String profilePicture;  // URL to the profile picture

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
     * @param userID  The unique user identifier.
     */
    public User(int userID) {
        this.userID = userID;
        Database.retrieve(this);
    }

    public User(int        userID,
                String     username,
                UserLevel  userLevel,
                Clock      creationDate,
                Clock      lastLogin,
                UserStatus userStatus,
                String     profilePicture,
                boolean    isDeleted) {
        super(isDeleted);
        this.userID         = userID;
        this.username       = username;
        this.userLevel      = userLevel;
        this.creationDate   = creationDate;
        this.lastLogin      = lastLogin;
        this.userStatus     = userStatus;
        this.profilePicture = profilePicture;
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

    public Clock getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Clock creationDate) {
        this.creationDate = creationDate;
    }

    public Clock getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Clock lastLogin) {
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
}
