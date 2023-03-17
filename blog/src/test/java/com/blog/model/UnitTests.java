package com.blog.model;

import com.blog.exception.BlogException;
import com.blog.utils.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.blog.model.UserLevel.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class UnitTests {
    private User guest;
    private Comment comment;

    @BeforeEach
    void beforeEach() {
        guest = new User(1);
        comment = new Comment(
                0,
                1,
                2,
                "",
                "2023-03-15T06:00:00.861336Z",
                "2023-03-15T06:00:00.861336Z",
                0,
                0,
                false
        );
    }

    // Tests for User + UserLevel + UserStatus
    @Test
    void guestConstructor() {
        assertEquals(0, guest.getUserID());
        assertEquals(UserLevel.GUEST, guest.getUserLevel());
    }

    @Test
    void userIDSet() {
        assertEquals(0, guest.getUserID());
        // guest.setUserID(1);
        assertEquals(1, guest.getUserID());
    }

    @Test
    void userNameSet() {
        guest.setUsername("Guest");
        assertEquals("Guest", guest.getUsername());
    }

    @Test
    void guestToOtherLevels() {
        assertEquals(UserLevel.GUEST, guest.getUserLevel());
        guest.setUserLevel(READER);
        assertEquals(UserLevel.GUEST, guest.getUserLevel());
        guest.setUserLevel(CONTRIBUTOR);
        assertEquals(UserLevel.CONTRIBUTOR, guest.getUserLevel());
        guest.setUserLevel(ADMIN);
        assertEquals(UserLevel.ADMIN, guest.getUserLevel());
    }

    @Test
    void userCreateionDateSet() {
         guest.setCreationDate("2023-03-15T06:00:00.861336Z");
         assertEquals("2023-03-15T06:00:00.861336Z", guest.getCreationDate());
    }

    @Test
    void userLastLoginSet() {
         guest.setCreationDate("2023-03-15T06:00:00.861336Z");
         assertEquals("2023-03-15T06:00:00.861336Z", guest.getCreationDate());
    }

    @Test
    void guestToOtherStatus() {
        guest.setUserStatus(UserStatus.AWAY);
        assertEquals(UserStatus.AWAY, guest.getUserStatus());
        guest.setUserStatus(UserStatus.BUSY);
        assertEquals(UserStatus.BUSY, guest.getUserStatus());
        guest.setUserStatus(UserStatus.OFFLINE);
        assertEquals(UserStatus.OFFLINE, guest.getUserStatus());
        guest.setUserStatus(UserStatus.ONLINE);
        assertEquals(UserStatus.ONLINE, guest.getUserStatus());
    }

    @Test
    void userProfilePictureSet() {
        guest.setProfilePicture("");
        assertEquals("", guest.getProfilePicture());
    }

    // Tests for Comment
    @Test
    void commentConstructorOne() {
        Comment comment1 = new Comment();
        assertEquals(0, comment1.getPostID());
        assertEquals(0, comment.getCommentID());
    }

    @Test
    void commentConstructorTwo() {
        Comment comment1 = new Comment(
                0,
                1);
        assertEquals(0, comment1.getPostID());
        assertEquals(1, comment.getCommentID());
    }

    @Test
    void commentPostIDCheck() {
        assertEquals(1, comment.getPostID());
    }

    @Test
    void commentCommentIDCheck() {
        assertEquals(1, comment.getCommentID());
    }

    @Test
    void commentCommentIDUpdate() {
        assertEquals(1, comment.getCommentID());
        comment.setCommentID(5);
        assertEquals(5, comment.getCommentID());
    }

    @Test
    void commentCopy() {
        Comment comment1 = new Comment(
                10,
                11,
                12,
                "Happy New Year!",
                "2022-01-01T00:00:00.861336Z",
                "2022-01-02T06:00:00.861336Z",
                18,
                1,
                false
        );
        assertEquals(1, comment.getCommentID());
        assertEquals(0, comment.getPostID());
        comment.copy(comment1);
        assertEquals(11, comment.getCommentID());
        assertEquals(10, comment.getPostID());
        // TODO: add other assertions from content
    }

    @Test
    void commentValidComment() {
        try {
            comment.validateContent("comment");
            fail("a blog exception should be thrown.");
        } catch (BlogException be){
            // success
        }
    }
    @Test
    void commentValidCommentTwo() {
        try {
            Comment comment1 = new Comment(
                    10,
                    11,
                    12,
                    "Happy New Year!",
                    "2022-01-01T00:00:00.861336Z",
                    "2022-01-02T06:00:00.861336Z",
                    18,
                    1,
                    false
            );
            comment.validateContent("comment1");
        } catch (BlogException be){
            fail("a blog exception should be thrown.");
        }
    }

    @Test
    void commentDisplayableSuccess() {
        assertEquals(true, comment.isDisplayable());
    }

    @Test
    void commentDisplayableFailOne() {
        Comment comment1 = new Comment(
                10,
                11,
                12,
                "Happy New Year!",
                "2022-01-01T00:00:00.861336Z",
                "2022-01-02T06:00:00.861336Z",
                18,
                1,
                true
        );
        assertEquals(false, comment1.isDisplayable());
    }

    @Test
    void commentDisplayableFailTwo() {
        Comment comment1 = null;
        assertEquals(false, comment1.isDisplayable());
    }

    @Test
    void commentLastModified() {
        String timeNow = Utility.getCurrentTime();
        comment.lastModifiedNow();
        assertEquals(timeNow, comment.getLastModified());
    }

    @Test
    void commentUpVote() {
        assertEquals(0, comment.getUpvotes());
        comment.upvote();
        assertEquals(1, comment.getUpvotes());
    }

    @Test
    void commentDownVote() {
        assertEquals(0, comment.getDownvotes());
        comment.downvote();
        assertEquals(1, comment.getDownvotes());
    }

    @Test
    void commentSetAuthorID() {
        assertEquals(0, comment.getAuthorID());
        comment.setAuthorID(1);
        assertEquals(1, comment.getAuthorID());
    }
}