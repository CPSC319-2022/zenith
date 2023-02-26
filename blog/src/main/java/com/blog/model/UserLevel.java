package com.blog.model;

/**
 * Enumeration of all the user levels.
 *
 * User Levels
 * ----------
 * ADMIN:
 *     May promote members to a higher type.
 * CONTRIBUTOR:
 *     May create posts.
 * READER:
 *     May make comments.
 * GUEST:
 *     Not logged in. Only has read privileges.
 */
enum UserLevel {
    ADMIN,
    CONTRIBUTOR,
    READER,
    GUEST
}