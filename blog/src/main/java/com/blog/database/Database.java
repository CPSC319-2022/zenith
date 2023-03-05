package com.blog.database;

import com.blog.model.Comment;
import com.blog.model.Post;
import com.blog.model.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This class handles all calls to the database related to the blog application.
 */
public class Database {
    /*
     * TODO: Need some static fields to store the connection to the database.
     *       Need static method to make the connection (maybe also close)
     */

    /**
     * TODO
     *
     * @param comment
     */
    public static void retrieve(Comment comment) {
        int postID = comment.getPostID();
        int commentID = comment.getCommentID();

        throw new NotImplementedException();
        /*
        TODO: if comment does not exist, return without error. but make sure that you do comment.setContent(null) first.
         */
    }

    /**
     * TODO
     *
     * @param post
     */
    public static void retrieve(Post post) {
        int postID = post.getPostID();

        throw new NotImplementedException();
        /*
        TODO: if post does not exist, return without error. but make sure that you do post.setContent(null) first.
         */
    }

    /**
     * Retrieves the user record from the database with corresponding <code>userID</code> specified
     * by the given <code>User</code> object and updates its fields.
     *
     * @param user  The <code>User</code> object to update. Contains the <code>userID</code>.
     */
    public static void retrieve(User user) {
        int userID = user.getUserID();

        throw new NotImplementedException();
        /*
        TODO: Use userID to select corresponding user record from database.
              Then use setter functions from User to fill out the private fields
              Maybe throw an exception if user does not exist, e.g. throw new UserDoesNotExistException()
              If user is deleted maybe throw new UserIsDeletedException()
         */
    }

    /**
     * TODO
     *
     * @param comment
     */
    public static void save(Comment comment) {
        int postID = comment.getPostID();
        int commentID = comment.getCommentID();

        throw new NotImplementedException();
        /*
        TODO: if keys already exist in database, update
              otherwise, create new record with the keys
         */
    }

    /**
     * TODO
     *
     * @param post
     */
    public static void save(Post post) {
        int postID = post.getPostID();

        throw new NotImplementedException();
        /*
        TODO: if keys already exist in database, update
              otherwise, create new record with the keys
         */
    }

    /**
     * Saves the given <code>User</code> object into the database.
     *
     * @param user  The <code>User</code> object to save. Contains the <code>userID</code>.
     */
    public static void save(User user) {
        int userID = user.getUserID();

        throw new NotImplementedException();
        /*
        TODO: if keys already exist in database, update
              otherwise, create new record with the keys
         */
    }

    /**
     * TODO
     *
     * @param comment
     */
    public static void delete(Comment comment) {
        int postID = comment.getPostID();
        int commentID = comment.getCommentID();

        throw new NotImplementedException();
        /*
        TODO: don't hard delete the record
              instead, we'll soft delete by setting a deleted flag
         */
    }

    /**
     * TODO
     *
     * @param post
     */
    public static void delete(Post post) {
        int postID = post.getPostID();

        throw new NotImplementedException();
        /*
        TODO: don't hard delete the record
              instead, we'll soft delete by setting a deleted flag
         */
    }

    /**
     * Saves the given <code>User</code> object into the database.
     *
     * @param user  The <code>User</code> object to save. Contains the <code>userID</code>.
     */
    public static void delete(User user) {
        int userID = user.getUserID();

        throw new NotImplementedException();
        /*
        TODO: don't hard delete the record
              instead, we'll soft delete by setting a deleted flag
         */
    }
}
