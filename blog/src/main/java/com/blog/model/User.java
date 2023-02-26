package com.blog.model;

import java.time.Clock;

public class User {
    private final int userID;  // TODO: reserve 0 for guest user?
    private String username;
    private UserLevel userLevel;
    private final Clock creationDate;
    private Clock lastLogin;
    private boolean isOnline;

    // Default guest user constructor
    public User() {
        this.userID = 0;
        this.userLevel = UserLevel.GUEST;
        creationDate = null;  // TODO: change to current time
    }

    //
    public User(int userID) {
        // TODO: pull data from database?
        this.userID = userID;
        creationDate = null;
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
