package com.blog.model;

/**
 * Enumeration of all the user levels.
 *
 * User Levels
 * ----------
 * VIEWER:
 *     Not logged in. Only has read privileges.
 * READER:
 *     May make comments.
 * CONTRIBUTOR:
 *     May create posts.
 * ADMIN:
 *     May promote members to a higher type.
 */
public enum UserLevel {
    VIEWER,
    READER,
    CONTRIBUTOR,
    ADMIN;

    public boolean below(UserLevel userLevel) {
        return this.compareTo(userLevel) < 0;
    }

    public boolean above(UserLevel userLevel) {
        return this.compareTo(userLevel) > 0;
    }
}