package com.blog.database;

import org.junit.jupiter.api.Test;

import com.blog.model.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class DatabaseTest {
    @Test
    void testSaveUser() {
        User guest = new User(2);
        guest.setUsername("testname");
        guest.setUserLevel(UserLevel.ADMIN);
        guest.setCreationDate("2023-03-01 01:02:03");
        guest.setLastLogin("2023-03-01 01:02:03");
        guest.setProfilePicture(null);
        guest.setDeleted(false);
        try {
            int num = Database.save(guest);
            assertEquals(2, num);
            assertEquals("2023-03-01 01:02:03", guest.getCreationDate());
            assertEquals("2023-03-01 01:02:03", guest.getLastLogin());
            assertEquals(UserLevel.ADMIN, guest.getUserLevel());
            assertEquals("testname", guest.getUsername());
            assertEquals(false, guest.isDeleted());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }  
    }

    @Test
    void testSavePost() {
        Post post = new Post(1);
        post.setAuthorID(1);
        post.setTitle("testtitle");
        post.setContent("testcontent");
        post.setCreationDate("2023-03-01 01:02:03");
        post.setLastModified("2023-03-01 01:02:03");
        post.setUpvotes(1);
        post.setDownvotes(2);
        post.setViews(3);
        post.setDeleted(false);
        post.setAllowComments(true);
        try {
            int num = Database.save(post);
            assertEquals(1, num);
            assertEquals("testtitle", post.getTitle());
            assertEquals("testcontent", post.getContent());
            assertEquals("2023-03-01 01:02:03", post.getCreationDate());
            assertEquals("2023-03-01 01:02:03", post.getLastModified());
            assertEquals(1, post.getUpvotes());
            assertEquals(2, post.getDownvotes());
            assertEquals(3, post.getViews());
            assertEquals(false, post.isDeleted());
            assertEquals(true, post.isAllowComments());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }  
    }

    @Test
    void testSaveComment() {
        Comment comment = new Comment(1, 1);
        comment.setAuthorID(1);
        comment.setContent("testcontent");
        comment.setCreationDate("2023-03-01 01:02:03");
        comment.setLastModified("2023-03-01 01:02:03");
        comment.setUpvotes(1);
        comment.setDownvotes(2);
        comment.setDeleted(false);
        try {
            int num = Database.save(comment);
            assertEquals(1, num);
            assertEquals(1, comment.getPostID());
            assertEquals("testcontent", comment.getContent());
            assertEquals("2023-03-01 01:02:03", comment.getCreationDate());
            assertEquals("2023-03-01 01:02:03", comment.getLastModified());
            assertEquals(1, comment.getUpvotes());
            assertEquals(2, comment.getDownvotes());
            assertEquals(false, comment.isDeleted());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }  
    }

    @Test
    void testRetrieveUser() {
        User guest = new User(1);
        try {
            Database.retrieve(guest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(1, guest.getUserID());
        assertEquals("2023-03-01 01:02:03", guest.getCreationDate());
        assertEquals("2023-03-01 01:02:03", guest.getLastLogin());
        assertEquals(UserLevel.READER, guest.getUserLevel());
        assertEquals("user1", guest.getUsername());
        assertEquals(false, guest.isDeleted());
    }

    @Test
    void testRetrievePost() {
        Post post = new Post(1);
        try {
            Database.retrieve(post);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(1, post.getPostID());
        assertEquals(1, post.getAuthorID());
        assertEquals("2023-03-01 01:02:03", post.getCreationDate());
        assertEquals("2023-03-01 01:02:03", post.getLastModified());
        assertEquals("title1", post.getTitle());
        assertEquals("content1", post.getContent());
        assertEquals(0, post.getUpvotes());
        assertEquals(0, post.getDownvotes());
        assertEquals(false, post.isDeleted());
        assertEquals(true, post.isAllowComments());
    }

    @Test
    void testRetrieveComment() {
        Comment comment = new Comment(1, 1);
        try {
            Database.retrieve(comment);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(1, comment.getPostID());
        assertEquals(1, comment.getCommentID());
        assertEquals(1, comment.getAuthorID());
        assertEquals("2023-03-01 01:02:03", comment.getCreationDate());
        assertEquals("2023-03-01 01:02:03", comment.getLastModified());
        assertEquals("content1", comment.getContent());
        assertEquals(0, comment.getUpvotes());
        assertEquals(0, comment.getDownvotes());
        assertEquals(false, comment.isDeleted());
    }

    @Test
    void testRetrieveMultiplePost() {
        ArrayList<Post> posts = new ArrayList<Post>();
        try {
            Database.retrieve(posts, 3, 2, false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals("testtitlec", posts.get(1).getTitle());
        //will fail if database reset, need to change later
    }

    @Test
    void testDeleteUser() {
        User guest = new User(2);
        try {
            Database.delete(guest);
            Database.retrieve(guest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(true, guest.isDeleted());
    }

    @Test
    void testDeletePost() {
        Post post = new Post(3);
        try {
            Database.delete(post);
            Database.retrieve(post);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(true, post.isDeleted());
    }

    @Test
    void testDeleteComment() {
        Comment comment = new Comment(1, 2);
        try {
            Database.delete(comment);
            Database.retrieve(comment);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(true, comment.isDeleted());
    }
}