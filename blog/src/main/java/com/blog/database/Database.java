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
     * Retrieves the user record from the database with corresponding <code>userID</code> specified
     * by the given <code>User</code> object and updates its fields.
     *
     * @param user  The <code>User</code> object to update. Contains the <code>userID</code>.
     */
    public static void retrieveUser(User user) {
        int userID = user.getUserID();

        throw new NotImplementedException();
        /*
        TODO: @Cheryl
              Use userID to select corresponding user record from database.
              Then use setter functions from User to fill out the private fields
         */
    }

    public static void retrieveComment(Comment comment) {
        throw new NotImplementedException();
    }

    public static void retrievePost(Post post) {
        throw new NotImplementedException();
    }

    // TODO: corresponding static methods for updating a record
    //       maybe a range retrieve? e.g. retrieve next k posts starting from n
}
