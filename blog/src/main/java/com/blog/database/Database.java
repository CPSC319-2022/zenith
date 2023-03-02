package com.blog.database;

import com.blog.model.Comment;
import com.blog.model.Post;
import com.blog.model.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This class handles all calls to the database related to the blog application.
 */
public class Database {
    /**
     * TODO
     *
     * @param comment
     */
    public static void retrieve(Comment comment) {
        int postID = comment.getPostID();
        int commentID = comment.getCommentID();

        throw new NotImplementedException();
    }

    /**
     * TODO
     *
     * @param post
     */
    public static void retrieve(Post post) {
        int postID = post.getPostID();

        throw new NotImplementedException();
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
        TODO: @Cheryl
              Use userID to select corresponding user record from database.
              Then use setter functions from User to fill out the private fields
         */
    }

    // TODO: corresponding static methods for updating a record
    //       maybe a range retrieve? e.g. retrieve next k posts starting from n
}
