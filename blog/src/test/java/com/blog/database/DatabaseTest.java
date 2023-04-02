package com.blog.database;

import com.blog.exception.DoesNotExistException;
import com.blog.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseTest {
    @Test
    void testSaveUser() {
        // Create a user
        User user = new User("testID");
        user.setUsername("testname");
        user.setCreationDate("time1");
        user.setLastLogin("time2");
        user.setProfilePicture("link");
        user.setbio("testbio");
        user.setUserLevel(UserLevel.CONTRIBUTOR);
        user.setDeleted(false);
        
        // Create another user with same ID but with different info
        User update = new User("testID");
        update.setUsername("updatedname");
        update.setCreationDate("updatedtime1");
        update.setLastLogin("updatedtime2");
        update.setProfilePicture("updatedlink");
        update.setbio("updatedtestbio");
        update.setUserLevel(UserLevel.ADMIN);
        update.setDeleted(false);
        try {
            // Hard delete to make sure user not in db
            Database.hardDelete(user);
            Database.save(user);
            User result = new User("testID");
            Database.retrieve(result);
            assertEquals("testname", result.getUsername());
            assertEquals("time1", result.getCreationDate());
            assertEquals("time2", result.getLastLogin());
            assertEquals("link", result.getProfilePicture());
            assertEquals(UserLevel.CONTRIBUTOR, result.getUserLevel());
            assertEquals("testbio", result.getBio());
            assertEquals(false, result.isDeleted());

            // Update and check if info changes
            Database.save(update);
            User result2 = new User("testID");
            Database.retrieve(result2);
            assertEquals("updatedname", result2.getUsername());
            assertEquals("updatedtime1", result2.getCreationDate());
            assertEquals("updatedtime2", result2.getLastLogin());
            assertEquals("updatedlink", result2.getProfilePicture());
            assertEquals(UserLevel.ADMIN, result2.getUserLevel());
            assertEquals("updatedtestbio", result2.getBio());
            assertEquals(false, result2.isDeleted());

            Database.hardDelete(user);
        } catch (Exception e) {
            Database.hardDelete(user);
            fail(e.getMessage());
        }
    }

    @Test
    void testSavePost() {
        // Create a user as a foreign key of post
        User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
        Database.save(user);

        // Create a new post
        Post post = new Post(0);
        post.setAuthorID("testID");
        post.setTitle("title");
        post.setContent("content");
        post.setCreationDate("time1");
        post.setLastModified("time2");
        post.setUpvotes(10);
        post.setDownvotes(5);
        post.setDeleted(false);
        post.setViews(30);
        post.setAllowComments(true);
        post.setThumbnailURL("link");

        // Create another post for update
        Post update = new Post(0);
        update.setAuthorID("testID");
        update.setTitle("updatedtitle");
        update.setContent("updatedcontent");
        update.setCreationDate("updatedtime1");
        update.setLastModified("updatedtime2");
        update.setUpvotes(15);
        update.setDownvotes(10);
        update.setDeleted(true);
        update.setViews(40);
        update.setAllowComments(false);
        update.setThumbnailURL("updatedlink");
        try {
            // Test for insert
            Database.save(post);
            Post result = new Post(post.getPostID());
            Database.retrieve(result);
            assertEquals("testID", result.getAuthorID());
            assertEquals("title", result.getTitle());
            assertEquals("content", result.getContent());
            assertEquals("time1", result.getCreationDate());
            assertEquals("time2", result.getLastModified());
            assertEquals(10, result.getUpvotes());
            assertEquals(5, result.getDownvotes());
            assertEquals(false, result.isDeleted());
            assertEquals(30, result.getViews());
            assertEquals(true, result.isAllowComments());
            assertEquals("link", result.getThumbnailURL());

            // Test for update
            update.setPostID(post.getPostID());
            Database.save(update);
            Post result2 = new Post(post.getPostID());
            Database.retrieve(result2);
            assertEquals("testID", result2.getAuthorID());
            assertEquals("updatedtitle", result2.getTitle());
            assertEquals("updatedcontent", result2.getContent());
            assertEquals("updatedtime1", result2.getCreationDate());
            assertEquals("updatedtime2", result2.getLastModified());
            assertEquals(15, result2.getUpvotes());
            assertEquals(10, result2.getDownvotes());
            assertEquals(true, result2.isDeleted());
            assertEquals(40, result2.getViews());
            assertEquals(false, result2.isAllowComments());
            assertEquals("updatedlink", result.getThumbnailURL());

            Database.hardDelete(post);
        } catch (Exception e) {
            Database.hardDelete(post);
            System.out.println(e.getMessage());
        }
    }



}