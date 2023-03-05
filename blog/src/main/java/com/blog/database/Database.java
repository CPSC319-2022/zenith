package com.blog.database;

import com.blog.model.Comment;
import com.blog.model.Post;
import com.blog.model.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

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
     * Retrieves the next <code>count</code> displayable comments starting from <code>commentIDStart</code> from
     * the post with post ID <code>postID</code> into <code>comments</code>.
     *
     * @param comments
     * @param postID
     * @param commentIDStart
     * @param count
     */
    public static void retrieve(ArrayList<Comment> comments, int postID, int commentIDStart, int count) {
        throw new NotImplementedException();
        /*
        SELECT *
        FROM   comments
        WHERE  postID = postID
               AND
               commentID >= commentIDStart
               AND
               !isDeleted
        LIMIT  count

        for each record:
            comments.add(new Comment(...))
         */
    }

    /**
     * Retrieves the next <code>count</code> displayable posts starting from <code>postIDStart</code>
     * into <code>posts</code>.
     *
     * @param posts
     * @param postIDStart
     * @param count
     */
    public static void retrieve(ArrayList<Post> posts, int postIDStart, int count) {
        throw new NotImplementedException();
        /*
        SELECT *
        FROM   posts
        WHERE  postID >= postIDStart
               AND
               !isDeleted
        LIMIT  count

        for each record:
            post.add(new Post(...))
         */
    }

    /**
     * TODO
     *
     * @param comment
     *
     * @return the commentID
     */
    public static int save(Comment comment) {
        int postID = comment.getPostID();
        int commentID = comment.getCommentID();

        throw new NotImplementedException();
        /*
        TODO: if keys already exist in database, update
              if postID does not exist throw error
              if commentID == 0, create new record with commentID being next smallest assignable ID
              if commentID anything else throw error
              Note: commentID == 0 reserved for creating new record
         */
    }

    /**
     * TODO
     *
     * @param post
     *
     * @return the postID
     */
    public static int save(Post post) {
        int postID = post.getPostID();

        throw new NotImplementedException();
        /*
        TODO: if key already exist in database, update
              if postID == 0, create new record with postID being next smallest assignable ID
              if postID anything else throw error
              Note: postID == 0 reserved for creating new record
         */
    }

    /**
     * Saves the given <code>User</code> object into the database.
     *
     * @param user  The <code>User</code> object to save. Contains the <code>userID</code>.
     *
     * @return the userID
     */
    public static int save(User user) {
        int userID = user.getUserID();

        throw new NotImplementedException();
        /*
        TODO: if key already exist in database, update
              if userID == 0, create new record with userID being next smallest assignable ID
              if userID anything else throw error
              Note: userID == 0 reserved for creating new record
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
