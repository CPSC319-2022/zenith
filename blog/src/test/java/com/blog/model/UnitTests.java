package com.blog.model;

import com.blog.exception.BlogException;
import com.blog.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.blog.model.UserLevel.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class UnitTests {
    private User guest;
    private Comment comment;
    private Post post;

    @BeforeEach
    void beforeEach() {
        guest = new User(
                "",
                "Guest Username",
                GUEST,
                Utility.getCurrentTime(),
                Utility.getCurrentTime(),
                UserStatus.ONLINE,
                "url to profile picture",
                "Guest bio",
                false
        );
        comment = new Comment(
                0,
                1,
                "2",
                "",
                "2023-03-15T06:00:00.861336Z",
                "2023-03-15T06:00:00.861336Z",
                0,
                0,
                false
        );
        post = new Post(
                0,
                "1",
                "",
                "",
                "2023-03-16T16:30:00.861336Z",
                "2023-03-16T16:30:00.861336Z",
                0,
                0,
                false,
                0,
                true);
    }

    /**
     * Tests for User + UserLevel + UserStatus
     */
    @Test
    void guestConstructor() {
        assertEquals(UserLevel.GUEST, guest.getUserLevel());
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
        assertEquals(UserLevel.READER, guest.getUserLevel());
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
    void userGetProfilePicture() {
        assertEquals("url to profile picture", guest.getProfilePicture());
    }

    @Test
    void userProfilePictureSetEmptyString() {
        guest.setProfilePicture("");
        assertEquals("", guest.getProfilePicture());
    }


    @Test
    void userProfilePictureSetTry() {
        guest.setProfilePicture("a1ns8");
        assertEquals("a1ns8", guest.getProfilePicture());
    }

    @Test
    void userGetBio() {
        assertEquals("Guest bio", guest.getBio());
    }

    @Test
    void userSetBioEmptyString() {
        guest.setBio("");
        assertEquals("", guest.getBio());
    }

    @Test
    void userSetBioTry() {
        guest.setBio("a1ns8");
        assertEquals("a1ns8", guest.getBio());
    }

    /**
     * Tests for Comment
     */

    @Test
    void commentConstructorTwo() {
        Comment comment1 = new Comment(
                0,
                1);
        assertEquals(0, comment1.getPostID());
        assertEquals(1, comment1.getCommentID());
    }

    @Test
    void commentConstructorThree() {
        Comment comment1 = new Comment(
                10,
                11,
                "12",
                "Happy New Year!",
                "2022-01-01T00:00:00.861336Z",
                "2022-01-02T06:00:00.861336Z",
                18,
                1,
                false
        );
        assertEquals(11, comment1.getCommentID());
        assertEquals(10, comment1.getPostID());
        assertEquals("12", comment1.getAuthorID());
        assertEquals("Happy New Year!", comment1.getContent());
        assertEquals("2022-01-01T00:00:00.861336Z", comment1.getCreationDate());
        assertEquals("2022-01-02T06:00:00.861336Z", comment1.getLastModified());
        assertEquals(18, comment1.getUpvotes());
        assertEquals(1, comment1.getDownvotes());
        assertEquals(false, comment1.isDeleted());
    }

    @Test
    void commentPostIDCheck() {
        assertEquals(0, comment.getPostID());
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
                "12",
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
        assertEquals(12, comment.getAuthorID());
        assertEquals("Happy New Year!", comment.getContent());
        assertEquals("2022-01-01T00:00:00.861336Z", comment.getCreationDate());
        assertEquals("2022-01-02T06:00:00.861336Z", comment.getLastModified());
        assertEquals(18, comment.getUpvotes());
        assertEquals(1, comment.getDownvotes());
        assertEquals(false, comment.isDeleted());
    }

    @Test
    void commentValidComment() {
        try {
            comment.validateContent("");
            fail("a blog exception should be thrown.");
        } catch (BlogException be){
            // success
        }
    }
    @Test
    void commentValidCommentTwo() {
        try {
            comment.validateContent("Happy New Year!");
        } catch (BlogException be){
            fail("a blog exception should not be thrown.");
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
                "12",
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
    void commentLastModified() {
        String timeNow = Utility.getCurrentTime();
        comment.lastModifiedNow();
        assertEquals(timeNow, comment.getLastModified());
    }

    @Test
    void commentGetUpvotes() {
        assertEquals(0, comment.getUpvotes());
    }
    @Test
    void commentUpvote() {
        assertEquals(0, comment.getUpvotes());
        comment.upvote();
        assertEquals(1, comment.getUpvotes());
    }

    @Test
    void commentSetUpvote() {
        assertEquals(0, comment.getUpvotes());
        comment.setUpvotes(5);
        assertEquals(5, comment.getUpvotes());
    }

    @Test
    void commentGetDownvotes() {
        assertEquals(0, comment.getUpvotes());
    }

    @Test
    void commentDownvote() {
        assertEquals(0, comment.getDownvotes());
        comment.downvote();
        assertEquals(1, comment.getDownvotes());
    }

    @Test
    void commentSetDownvote() {
        assertEquals(0, comment.getDownvotes());
        comment.setDownvotes(4);
        assertEquals(4, comment.getDownvotes());
    }

    @Test
    void commentSetAuthorID() {
        assertEquals("2", comment.getAuthorID());
        comment.setAuthorID("1");
        assertEquals("1", comment.getAuthorID());
    }

    @Test
    void commentGetContent() {
        Comment comment1 = new Comment(
                10,
                11,
                "12",
                "Happy New Year!",
                "2022-01-01T00:00:00.861336Z",
                "2022-01-02T06:00:00.861336Z",
                18,
                1,
                true
        );
        assertEquals("Happy New Year!", comment1.getContent());
    }

    @Test
    void commentSetContent() {
        comment.setContent("Happy New Year!");
        assertEquals("Happy New Year!", comment.getContent());
    }

    @Test
    void commentGetCreationDate() {
        assertEquals("2023-03-15T06:00:00.861336Z", comment.getCreationDate());
    }

    @Test
    void commentSetCreationDate() {
        comment.setCreationDate("2023-03-16T16:30:00.861336Z");
        assertEquals("2023-03-16T16:30:00.861336Z", comment.getCreationDate());
    }

    @Test
    void commentGetLastModified() {
        assertEquals("2023-03-15T06:00:00.861336Z", comment.getLastModified());
    }

    @Test
    void commentSetLastModified() {
        comment.setLastModified("2023-03-16T16:30:00.861336Z");
        assertEquals("2023-03-16T16:30:00.861336Z", comment.getLastModified());
    }

    @Test
    void commentAsJSONObject(){
        JSONObject expectedJson = new JSONObject()
                .put("postID", 10)
                .put("commentID", 11);

        Comment comment1 = new Comment(
                10,
                11,
                "12",
                "Happy New Year!",
                "2022-01-01T00:00:00.861336Z",
                "2022-01-02T06:00:00.861336Z",
                18,
                1,
                true
        );
        JSONObject actualJson = comment1.asJSONObject();

        try {
            JSONAssert.assertEquals(expectedJson, actualJson, false);
        } catch (JSONException je) {
            fail("JSON type is not the same.");
        }
    }

    /**
     * Tests for Post
     */
    @Test
    void postConstructorOne() {
        Post post1 = new Post(1);
        assertEquals(1, post1.getPostID());
    }

    @Test
    void postConstructorTwo() {
        assertEquals(0, post.getPostID());
        assertEquals("1", post.getAuthorID());
        assertEquals("", post.getTitle());
        assertEquals("", post.getContent());
        assertEquals("2023-03-16T16:30:00.861336Z", post.getCreationDate());
        assertEquals("2023-03-16T16:30:00.861336Z", post.getLastModified());
        assertEquals(0, post.getUpvotes());
        assertEquals(0, post.getDownvotes());
        assertEquals(false, post.isDeleted());
        assertEquals(0, post.getViews());
        assertEquals(true, post.isAllowComments());
    }

    @Test
    void postPostIDCheck() {
        assertEquals(0, post.getPostID());
    }

    @Test
    void postAuthorIDCheck() {
        assertEquals("1", post.getAuthorID());
    }

    @Test
    void postGetViews() {
        assertEquals(0, post.getViews());
    }

    @Test
    void postUpdateViews() {
        assertEquals(0, post.getViews());
        post.view();
        assertEquals(1, post.getViews());
    }

    @Test
    void postSetViews() {
        assertEquals(0, post.getViews());
        post.setViews(6);
        assertEquals(6, post.getViews());
    }

    @Test
    void postGetTitle() {
        assertEquals("", post.getTitle());
    }

    @Test
    void postSetTitle() {
        assertEquals("", post.getTitle());
        post.setTitle("Happy Holiday");
        assertEquals("Happy Holiday", post.getTitle());
    }

    @Test
    void postAllowComments() {
        assertEquals(true, post.isAllowComments());
    }

    @Test
    void postSetAllowComments() {
        assertEquals(true, post.isAllowComments());
        post.setAllowComments(false);
        assertEquals(false, post.isAllowComments());
    }

    @Test
    void postValidTitleOne() {
        try {
            post.validateTitle("");
            fail("a blog exception should be thrown.");
        } catch (BlogException be){
            // success
        }
    }

    @Test
    void postValidTitleTwo() {
        try {
            post.validateTitle("Happy New Year");
        } catch (BlogException be){
            fail("a blog exception should not be thrown.");
        }
    }

    @Test
    void postValidContentOne() {
        try {
            post.validateContent("");
            fail("a blog exception should be thrown.");
        } catch (BlogException be){
            // success
        }
    }

    @Test
    void postValidContentTwo() {
        try {
            post.validateContent("Happy New Year");
        } catch (BlogException be){
            fail("a blog exception should not be thrown.");
        }
    }

    @Test
    void postCopy() {
        Post post1 = new Post(
                1874,
                "9934",
                "Happy New Year",
                "I wish everyone is having fun in Christmas and wish all of us a happy new year!",
                "2023-01-01T00:00:01.861336Z",
                "2023-01-01T00:21:57.861336Z",
                98,
                0,
                false,
                201,
                true);

        assertEquals(0, post.getPostID());
        assertEquals("1", post.getAuthorID());
        post.copy(post1);
        assertEquals(1874, post.getPostID());
        assertEquals("9934", post.getAuthorID());
        assertEquals("Happy New Year", post.getTitle());
        assertEquals("I wish everyone is having fun in Christmas and wish all of us a happy new year!", post.getContent());
        assertEquals("2023-01-01T00:00:01.861336Z", post.getCreationDate());
        assertEquals("2023-01-01T00:21:57.861336Z", post.getLastModified());
        assertEquals(98, post.getUpvotes());
        assertEquals(0, post.getDownvotes());
        assertEquals(false, post.isDeleted());
        assertEquals(201, post.getViews());
        assertEquals(true, post.isAllowComments());
    }

    @Test
    void postDisplayableSuccess() {
        assertEquals(true, post.isDisplayable());
    }

    @Test
    void postDisplayableFailOne() {
        Post post1 = new Post(
                10,
                "11",
                "Happy New Year!",
                "I wish everyone is having fun in Christmas and wish all of us a happy new year!",
                "2022-01-01T00:00:00.861336Z",
                "2022-01-02T06:00:00.861336Z",
                18,
                1,
                true,
                200,
                false
        );
        assertEquals(false, post1.isDisplayable());
    }

    @Test
    void postLastModified() {
        String timeNow = Utility.getCurrentTime();
        post.lastModifiedNow();
        assertEquals(timeNow, post.getLastModified());
    }

    @Test
    void postGetUpvotes() {
        assertEquals(0, post.getUpvotes());
    }
    @Test
    void postUpvote() {
        assertEquals(0, post.getUpvotes());
        post.upvote();
        assertEquals(1, post.getUpvotes());
    }

    @Test
    void postSetUpvote() {
        assertEquals(0, post.getUpvotes());
        post.setUpvotes(5);
        assertEquals(5, post.getUpvotes());
    }

    @Test
    void postGetDownvotes() {
        assertEquals(0, post.getUpvotes());
    }

    @Test
    void postDownvote() {
        assertEquals(0, post.getDownvotes());
        post.downvote();
        assertEquals(1, post.getDownvotes());
    }

    @Test
    void postSetDownvote() {
        assertEquals(0, post.getDownvotes());
        post.setDownvotes(4);
        assertEquals(4, post.getDownvotes());
    }


    @Test
    void postSetAuthorID() {
        assertEquals("1", post.getAuthorID());
        post.setAuthorID("12");
        assertEquals("12", post.getAuthorID());
    }

    @Test
    void postGetContent() {
        Post post1 = new Post(
                10,
                "11",
                "Happy New Year!",
                "I wish everyone is having fun in Christmas and wish all of us a happy new year!",
                "2022-01-01T00:00:00.861336Z",
                "2022-01-02T06:00:00.861336Z",
                18,
                1,
                true,
                200,
                false
        );
        assertEquals("I wish everyone is having fun in Christmas and wish all of us a happy new year!", post1.getContent());
    }

    @Test
    void postSetContent() {
        post.setContent("Happy New Year!");
        assertEquals("Happy New Year!", post.getContent());
    }

    @Test
    void postGetCreationDate() {
        assertEquals("2023-03-16T16:30:00.861336Z", post.getCreationDate());
    }

    @Test
    void postSetCreationDate() {
        post.setCreationDate("2023-03-16T16:30:00.861336Z");
        assertEquals("2023-03-16T16:30:00.861336Z", post.getCreationDate());
    }

    @Test
    void postGetLastModified() {
        assertEquals("2023-03-16T16:30:00.861336Z", post.getLastModified());
    }

    @Test
    void postSetLastModified() {
        post.setLastModified("2023-03-17T19:30:00.861336Z");
        assertEquals("2023-03-17T19:30:00.861336Z", post.getLastModified());
    }

    @Test
    void postAsJSONObject(){
        JSONObject expectedJson = new JSONObject()
                .put("postID", 10)
                .put("title", "Happy New Year!")
                .put("views", 201)
                .put("allowComments", true);

        Post post1 = new Post(
                10,
                "11",
                "Happy New Year!",
                "I wish everyone is having fun in Christmas and wish all of us a happy new year!",
                "2022-01-01T00:00:00.861336Z",
                "2022-01-02T06:00:00.861336Z",
                18,
                1,
                true,
                201,
                true
        );
        JSONObject actualJson = post1.asJSONObject();

        try {
            JSONAssert.assertEquals(expectedJson, actualJson, false);
        } catch (JSONException je) {
            fail("JSON type is not the same.");
        }
    }
}