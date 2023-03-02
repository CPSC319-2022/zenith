package com.blog.model;

import com.blog.database.Database;

import java.time.Clock;

public class User {
    private final int userID;  // TODO: reserve 0 for guest user? Maybe even up to n reserved for testing.
    private String username;
    private UserLevel userLevel;
    private Clock creationDate;
    private Clock lastLogin;
    private boolean isOnline;  // TODO: change to enum for online/offline/busy/away?
    // TODO: discuss additional fields

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

    public Clock getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Clock lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
