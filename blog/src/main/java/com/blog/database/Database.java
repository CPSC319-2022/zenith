package com.blog.database;

import com.blog.model.Comment;
import com.blog.model.Post;
import com.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.blog.database.*;
import com.blog.exception.UserDoesNotExistException;
import com.blog.exception.UserIsDeletedException;

/**
 * This class handles all calls to the database related to the blog application.
 */

public class Database {
    /*
     * TODO: Need some static fields to store the connection to the database.
     *       Need static method to make the connection (maybe also close)
     */

	private static JdbcTemplate jdbcTemplate;

	public static void createTemplate() {
          DataSource dataSource = DatabaseConfig.dataSource();
          jdbcTemplate = new JdbcTemplate(dataSource);
     }
     

    /**
     * TODO
     *
     * @param comment
     */
    public static void retrieve(Comment comment) {
        int postID = comment.getPostID();
        int commentID = comment.getCommentID();
        String sql = "SELECT * FROM Comment WHERE post_ID = " + postID + " AND comment_number = " + commentID;
        if (jdbcTemplate == null) {
          createTemplate();
        }
        try {
          Comment temp =  jdbcTemplate.queryForObject(sql, new CommentRowMapper());
          comment.copy(temp);
          // TODO: what if the comment is deleted?
        } catch (EmptyResultDataAccessException e) {
          comment.setContent(null);
        }

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
        String sql = "SELECT * FROM Post WHERE post_ID = " + postID;
        if (jdbcTemplate == null) {
          createTemplate();
        }
        try {
          Post temp = jdbcTemplate.queryForObject(sql, new PostRowMapper());
          post.copy(temp);
          // TODO: what if the post is deleted?
        } catch (EmptyResultDataAccessException e) {
          post.setContent(null);
        }
        /*
        TODO: if post does not exist, return without error. but make sure that you do post.setContent(null) first.
         */
    }

    /**
     * Retrieves the user record from the database with corresponding <code>userID</code> specified
     * by the given <code>User</code> object and updates its fields.
     *
     * @param user  The <code>User</code> object to update. Contains the <code>userID</code>.
     * @throws UserDoesNotExistException
     * @throws UserIsDeletedException
     */
    public static void retrieve(User user) throws UserDoesNotExistException, UserIsDeletedException {
        int userID = user.getUserID();
        String sql = "SELECT * FROM User WHERE user_ID = " + userID;
        if (jdbcTemplate == null) {
          createTemplate();
        }
        try {
          User temp = jdbcTemplate.queryForObject(sql, new UserRowMapper());
          user.copy(temp);
          if (temp.isDeleted()) {
               throw new UserIsDeletedException("User with ID " + userID + " is deleted.");
          }
        } catch (EmptyResultDataAccessException e) {
          throw new UserDoesNotExistException("User with ID " + userID + " does not exist.");
        }
	   
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
    public static void retrieve(ArrayList<Comment> comments, int postID, int commentIDStart, int count, boolean reverse) {
        
        /*
        SELECT *
        FROM   comments
        WHERE  postID = postID
               AND
               commentID >= commentIDStart  -- <= if reverse, otherwise >=
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
    public static void retrieve(ArrayList<Post> posts, int postIDStart, int count, boolean reverse) {
        
        /*
        SELECT *
        FROM   posts
        WHERE  postID <= postIDStart  -- <= if reverse, otherwise >=
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

        return 0;
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

        return 0;
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
        // TODO: Need to add user status to database.
        int userID = user.getUserID();
        String sql = "SELECT MAX(user_id) FROM User";
        if (jdbcTemplate == null) {
          createTemplate();
        }
        int maxID = 0;
        maxID = jdbcTemplate.queryForObject(sql, Integer.class);
        if (userID > maxID) {
          throw new Error("Invalid user ID.");
        }
        if (userID != 0) {
          sql = "DELETE FROM User WHERE user_ID = " + userID;
          jdbcTemplate.update(sql);
        } else {
          userID = maxID + 1;
        }
        sql = user.formSQL(userID);
        jdbcTemplate.update(sql);
        return userID;
        // Note that since user ID is final, you will have to create a new user later for further use.
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
        String sql = "UPDATE Comment SET is_deleted = true WHERE post_ID = " + postID + " AND comment_number = " + commentID;
        if (jdbcTemplate == null) {
          createTemplate();
        }
        jdbcTemplate.update(sql);
        comment.setDeleted(true);

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
        String sql = "UPDATE Post SET is_deleted = true WHERE post_ID = " + postID;
        if (jdbcTemplate == null) {
          createTemplate();
        }
        jdbcTemplate.update(sql);
        post.setDeleted(true);
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
        String sql = "UPDATE User SET is_deleted = true WHERE user_ID = " + userID;
        if (jdbcTemplate == null) {
          createTemplate();
        }
        jdbcTemplate.update(sql);
        user.setDeleted(true);

        // Note that there is no warning even if the user does not exist.
        
        /*
        TODO: don't hard delete the record
              instead, we'll soft delete by setting a deleted flag
         */
    }

}
