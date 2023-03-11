package com.blog.database;

import org.junit.jupiter.api.Test;

import com.blog.model.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
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
}