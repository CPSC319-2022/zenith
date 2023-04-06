package com.blog.model;

import com.blog.exception.BlogException;
import com.blog.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static com.blog.model.UserLevel.*;
import static com.blog.model.UserStatus.OFFLINE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UnitTests {
    private User guest;
    private Comment comment;
    private Post post;
    PromotionRequest promotionRequest;

    @BeforeEach
    void beforeEach() {
        guest = new User(
                "",
                "Guest Username",
                VIEWER,
                "2023-03-15T06:00:00.861336Z",
                "2023-03-16T06:00:00.861336Z",
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
                true,
                "");
        promotionRequest = new PromotionRequest(
                9,
                "45",
                READER,
                "2023-03-16T16:30:00.861336Z",
                "I wanna be a part of the community!",
                false,
                "Guest");
    }

    /**
     * Tests for User + UserLevel + UserStatus
     */
    @Test
    void guestConstructorOne() {
        assertEquals(UserLevel.VIEWER, guest.getUserLevel());
    }

    @Test
    void guestConstructorTwo() {
        User user1 = new User("1");
        assertEquals("1", user1.getUserID());
    }


    @Test
    void userNameSet() {
        guest.setUsername("Guest");
        assertEquals("Guest", guest.getUsername());
    }

    @Test
    void userGetUserID() {
        assertEquals("", guest.getUserID());
    }

    @Test
    void userSetUserID() {
        assertEquals("", guest.getUserID());
        guest.setUserID("Admin");
        assertEquals("Admin", guest.getUserID());
    }


    @Test
    void guestToOtherLevels() {
        assertEquals(UserLevel.VIEWER, guest.getUserLevel());
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
    void userGetLastLogin() {
        assertEquals("2023-03-16T06:00:00.861336Z", guest.getLastLogin());
    }

    @Test
    void userLastLoginSet() {
        guest.setLastLogin("2023-03-15T06:00:00.861336Z");
        assertEquals("2023-03-15T06:00:00.861336Z", guest.getCreationDate());
    }

    @Test
    void userSetLastLoginNow() {
        Instant timeBefore = Instant.parse(Utility.getCurrentTime());
        guest.setLastLoginNow();
        Instant timeAfter = Instant.parse(Utility.getCurrentTime());
        assertEquals(true, timeBefore.isBefore(Instant.parse(guest.getLastLogin())));
        assertEquals(true, timeAfter.isAfter(Instant.parse(guest.getLastLogin())));
    }

    @Test
    void guestToOtherStatus() {
        guest.setUserStatus(UserStatus.AWAY);
        assertEquals(UserStatus.AWAY, guest.getUserStatus());
        guest.setUserStatus(UserStatus.BUSY);
        assertEquals(UserStatus.BUSY, guest.getUserStatus());
        guest.setUserStatus(OFFLINE);
        assertEquals(OFFLINE, guest.getUserStatus());
        guest.setUserStatus(UserStatus.ONLINE);
        assertEquals(UserStatus.ONLINE, guest.getUserStatus());
    }

    @Test
    void testUserLevelBelow() {
        assertFalse(guest.below(VIEWER));
        assertTrue(guest.below(READER));
        assertTrue(guest.below(CONTRIBUTOR));
        assertTrue(guest.below(ADMIN));
    }

    @Test
    void testUserLevelAbove() {
        User admin = new User(
                "",
                "Guest Username",
                ADMIN,
                "2023-03-15T06:00:00.861336Z",
                "2023-03-16T06:00:00.861336Z",
                UserStatus.ONLINE,
                "url to profile picture",
                "Guest bio",
                false
        );
        assertTrue(admin.above(VIEWER));
        assertTrue(admin.above(READER));
        assertTrue(admin.above(CONTRIBUTOR));
        assertFalse(admin.above(ADMIN));
    }

    @Test
    void testUserSetDeleted() {
        assertFalse(guest.isDeleted());
        guest.setDeleted(true);
        assertTrue(guest.isDeleted());
    }

    @Test
    void testUserIsUserTrue() {
        assertTrue(guest.is(guest));
    }

    @Test
    void testUserIsUserFalse() {
        User admin = new User(
                "98",
                "Admin",
                ADMIN,
                "2023-03-15T06:00:00.861336Z",
                "2023-03-16T06:00:00.861336Z",
                UserStatus.ONLINE,
                "url to profile picture",
                "Guest bio",
                false
        );
        assertFalse(guest.is(admin));
    }

    @Test
    void testUserIsUserLevelTrue() {
        assertTrue(guest.is(VIEWER));
    }

    @Test
    void testUserIsUserLevelFalse() {
        assertFalse(guest.is(ADMIN));
    }

    @Test
    void testUserBelow() {
        assertFalse(guest.below(VIEWER));
        assertTrue(guest.below(READER));
        assertTrue(guest.below(CONTRIBUTOR));
        assertTrue(guest.below(ADMIN));
    }

    @Test
    void testUserAbove() {
        User admin = new User(
                "",
                "Guest Username",
                ADMIN,
                "2023-03-15T06:00:00.861336Z",
                "2023-03-16T06:00:00.861336Z",
                UserStatus.ONLINE,
                "url to profile picture",
                "Guest bio",
                false
        );
        assertTrue(admin.above(VIEWER));
        assertTrue(admin.above(READER));
        assertTrue(admin.above(CONTRIBUTOR));
        assertFalse(admin.above(ADMIN));
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

    @Test
    void userValidateBio() {
        try {
            guest.validateBio("");
            fail();
        } catch (BlogException e){
            // success;
        }
    }

    @Test
    void userValidateUsername() {
        try {
            guest.validateUsername("");
            fail();
        } catch (BlogException e){
            // success;
        }
    }

    @Test
    void userAsJSONObject() throws JSONException {
        JSONObject expectedJson = new JSONObject()
                .put("userID", "99")
                .put("username", "Admin")
                .put("creationDate", "2021-12-09T06:00:00.861336Z")
                .put("lastLogin", "2023-03-19T19:00:30.861336Z")
                .put("userStatus", OFFLINE)
                .put("profilePicture", "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")
                .put("bio", "Admin is watching you");

        User admin = new User(
                "99",
                "Admin",
                ADMIN,
                "2021-12-09T06:00:00.861336Z",
                "2023-03-19T19:00:30.861336Z",
                OFFLINE,
                "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
                "Admin is watching you",
                false
        );
        JSONObject actualJson = admin.asJSONObject();

        try {
            JSONAssert.assertEquals(expectedJson, actualJson, false);
        } catch (JSONException je) {
            fail("JSON type is not the same.");
        }
    }

    @Test
    void userAsJSONString() throws JSONException {
        JSONObject expectedJson = new JSONObject()
                .put("userID", "99")
                .put("username", "Admin")
                .put("creationDate", "2021-12-09T06:00:00.861336Z")
                .put("lastLogin", "2023-03-19T19:00:30.861336Z")
                .put("userStatus", OFFLINE)
                .put("profilePicture", "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png")
                .put("bio", "Admin is watching you");
        String expectedOutput = expectedJson.toString();
        User admin = new User(
                "99",
                "Admin",
                ADMIN,
                "2021-12-09T06:00:00.861336Z",
                "2023-03-19T19:00:30.861336Z",
                OFFLINE,
                "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
                "Admin is watching you",
                false
        );
        String actualOutput = admin.asJSONString();
        try {
            JSONAssert.assertEquals(expectedOutput, actualOutput, false);
        } catch (Exception e) {
            fail("String does not match.");
        }
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
    void testCommentIsAuthoredBy() {
        User user1 = new User("2");
        assertTrue(comment.isAuthoredBy(user1));
    }

    @Test
    void commentPostIDCheck() {
        assertEquals(0, comment.getPostID());
    }

    @Test
    void commentPostIDSet() {
        assertEquals(0, comment.getPostID());
        comment.setPostID(1);
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
    void commentValidComment() {
        try {
            comment.validateContent("");
            fail("a blog exception should be thrown.");
        } catch (BlogException be) {
            // success
        }
    }

    @Test
    void commentValidCommentTwo() {
        try {
            comment.validateContent("Happy New Year!");
        } catch (BlogException be) {
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
        Instant timeBefore = Instant.parse(Utility.getCurrentTime());
        comment.lastModifiedNow();
        Instant timeAfter = Instant.parse(Utility.getCurrentTime());
        assertEquals(true, timeBefore.isBefore(Instant.parse(comment.getLastModified())));
        assertEquals(true, timeAfter.isAfter(Instant.parse(comment.getLastModified())));
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
    void commentAsJSONObject() throws JSONException{
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

    @Test
    void commentAsJSONString() throws JSONException {
        JSONObject expectedJson = new JSONObject()
                .put("postID", 10)
                .put("commentID", 11);
        String expectedOutput = expectedJson.toString();
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

        String actualOutput = comment1.asJSONString();
        try {
            JSONAssert.assertEquals(expectedOutput, actualOutput, false);
        } catch (Exception e) {
            fail("String does not match.");
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
    void postConstructorThree() {
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
                true,
                "google.ca");
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
        assertEquals("google.ca", post.getThumbnailURL());
    }

    @Test
    void testPostIsAuthoredBy() {
        User user1 = new User("1");
        assertTrue(post.isAuthoredBy(user1));
    }

    @Test
    void postPostIDCheck() {
        assertEquals(0, post.getPostID());
    }

    @Test
    void postPostIDSet() {
        assertEquals(0, post.getPostID());
        post.setPostID(2);
        assertEquals(2, post.getPostID());
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
        } catch (BlogException be) {
            // success
        }
    }

    @Test
    void postValidTitleTwo() {
        try {
            post.validateTitle("Happy New Year");
        } catch (BlogException be) {
            fail("a blog exception should not be thrown.");
        }
    }

    @Test
    void postValidContentOne() {
        try {
            post.validateContent("");
            fail("a blog exception should be thrown.");
        } catch (BlogException be) {
            // success
        }
    }

    @Test
    void postValidContentTwo() {
        try {
            post.validateContent("Happy New Year");
        } catch (BlogException be) {
            fail("a blog exception should not be thrown.");
        }
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
                false,
                ""
        );
        assertEquals(false, post1.isDisplayable());
    }

    @Test
    void postLastModified() {
        Instant timeBefore = Instant.parse(Utility.getCurrentTime());
        post.lastModifiedNow();
        Instant timeAfter = Instant.parse(Utility.getCurrentTime());
        assertEquals(true, timeBefore.isBefore(Instant.parse(post.getLastModified())));
        assertEquals(true, timeAfter.isAfter(Instant.parse(post.getLastModified())));
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
                false,
                ""
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
    void postAsJSONObject() throws JSONException{
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
                true,
                ""
        );
        JSONObject actualJson = post1.asJSONObject();

        try {
            JSONAssert.assertEquals(expectedJson, actualJson, false);
        } catch (JSONException je) {
            fail("JSON type is not the same.");
        }
    }

    @Test
    void postAsJSONString() throws JSONException{
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
                true,
                ""
        );

        String expectedResult = expectedJson.toString();
        String actualResult = post1.asJSONString();

        try {
            JSONAssert.assertEquals(expectedResult, actualResult, false);
        } catch (JSONException je) {
            fail("JSON type is not the same.");
        }
    }

    @Test
    void testGetThumbnailURL(){
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
                true,
                "google.ca");
        assertEquals("google.ca", post.getThumbnailURL());
    }

    @Test
    void testSetThumbnailURL(){
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
                true,
                "google.ca");
        assertEquals("google.ca", post.getThumbnailURL());
        post.setThumbnailURL("");
        assertEquals("https://storage.googleapis.com/zenith-blog-thumbnailurl/default_thumbnail.png", post.getThumbnailURL());
        post.setThumbnailURL("abc.ca");
        assertEquals("abc.ca", post.getThumbnailURL());
    }


    /**
     * Tests for PromotionRequest
     */
    @Test
    void testPromotionRequestConstructorOne() {
        PromotionRequest promotionRequest1 = new PromotionRequest(1);
        assertEquals(promotionRequest1.getRequestID(), 1);
    }

    @Test
    void testPromotionRequestConstructorTwo() {
        PromotionRequest promotionRequest1 = new PromotionRequest(
                9,
                "45",
                READER,
                "2023-03-16T16:30:00.861336Z",
                "I wanna be a part of the community!",
                false,
                "Guest");
        assertEquals(promotionRequest1.getRequestID(), 9);
        assertEquals(promotionRequest1.getUserID(), "45");
        assertEquals(promotionRequest1.getTarget(), READER);
        assertEquals(promotionRequest1.getRequestTime(), "2023-03-16T16:30:00.861336Z");
        assertEquals(promotionRequest1.getReason(), "I wanna be a part of the community!");
    }

    @Test
    void testPromotionRequestConstructorThree() {
        assertEquals(promotionRequest.getRequestID(), 9);
        assertEquals(promotionRequest.getUserID(), "45");
        assertEquals(promotionRequest.getTarget(), READER);
        assertEquals(promotionRequest.getRequestTime(), "2023-03-16T16:30:00.861336Z");
        assertEquals(promotionRequest.getReason(), "I wanna be a part of the community!");
        assertEquals(promotionRequest.getUsername(), "Guest");
    }

    @Test
    void testPromotionRequestGetRequestID() {
        assertEquals(promotionRequest.getRequestID(), 9);
    }

    @Test
    void testPromotionRequestSetRequestID() {
        assertEquals(promotionRequest.getRequestID(), 9);
        promotionRequest.setRequestID(8);
        assertEquals(promotionRequest.getRequestID(), 8);
    }

    @Test
    void testPromotionRequestGetUserID() {
        assertEquals(promotionRequest.getUserID(), "45");
    }

    @Test
    void testPromotionRequestSetUserID() {
        assertEquals(promotionRequest.getUserID(), "45");
        promotionRequest.setUserID("55");
        assertEquals(promotionRequest.getUserID(), "55");
    }

    @Test
    void testPromotionRequestGetTarget() {
        assertEquals(promotionRequest.getTarget(), READER);
    }

    @Test
    void testPromotionRequestSetTarget() {
        assertEquals(promotionRequest.getTarget(), READER);
        promotionRequest.setTarget(ADMIN);
        assertEquals(promotionRequest.getTarget(), ADMIN);
    }

    @Test
    void testPromotionRequestGetRequestTime() {
        assertEquals(promotionRequest.getRequestTime(), "2023-03-16T16:30:00.861336Z");
    }

    @Test
    void testPromotionRequestSetRequestTime() {
        assertEquals(promotionRequest.getRequestTime(), "2023-03-16T16:30:00.861336Z");
        promotionRequest.setRequestTime("2023-03-18T10:32:00.861336Z");
        assertEquals(promotionRequest.getRequestTime(), "2023-03-18T10:32:00.861336Z");
    }

    @Test
    void testPromotionRequestGetReason() {
        assertEquals(promotionRequest.getReason(), "I wanna be a part of the community!");
    }

    @Test
    void testPromotionRequestSetReason() {
        assertEquals(promotionRequest.getReason(), "I wanna be a part of the community!");
        promotionRequest.setReason(";)");
        assertEquals(promotionRequest.getReason(), ";)");
    }

    @Test
    void testPromotionRequestGetUserName() {
        assertEquals(promotionRequest.getUsername(), "Guest");
    }

    @Test
    void testPromotionRequestSetUserName() {
        assertEquals(promotionRequest.getUsername(), "Guest");
        promotionRequest.setUsername("Admin");
        assertEquals(promotionRequest.getUsername(), "Admin");
    }

    @Test
    void testPromotionRequestAsJSONObjectOne() throws JSONException{
        promotionRequest = new PromotionRequest(
                9,
                "45",
                READER,
                "2023-03-16T16:30:00.861336Z",
                "I wanna be a part of the community!",
                false);
        JSONObject expectedJson = new JSONObject()
                .put("requestID", 9)
                .put("userID", "45")
                .put("target", READER)
                .put("requestTime", "2023-03-16T16:30:00.861336Z")
                .put("reason", "I wanna be a part of the community!");

        JSONObject actualJson = promotionRequest.asJSONObject();

        try {
            JSONAssert.assertEquals(expectedJson, actualJson, false);
        } catch (JSONException je) {
            fail("JSON type is not the same.");
        }
    }

    @Test
    void testPromotionRequestAsJSONObjectTwo() throws JSONException{
        promotionRequest = new PromotionRequest(
                9,
                "45",
                READER,
                "2023-03-16T16:30:00.861336Z",
                "I wanna be a part of the community!",
                false,
                "Guest");
        JSONObject expectedJson = new JSONObject()
                .put("requestID", 9)
                .put("userID", "45")
                .put("target", READER)
                .put("requestTime", "2023-03-16T16:30:00.861336Z")
                .put("reason", "I wanna be a part of the community!")
                .put("username", "Guest");

        JSONObject actualJson = promotionRequest.asJSONObject();

        try {
            JSONAssert.assertEquals(expectedJson, actualJson, false);
        } catch (JSONException je) {
            fail("JSON type is not the same.");
        }
    }

    @Test
    void testPromotionRequestAsJSONString() throws JSONException {
        promotionRequest = new PromotionRequest(
                9,
                "45",
                READER,
                "2023-03-16T16:30:00.861336Z",
                "I wanna be a part of the community!",
                false,
                "Guest");
        JSONObject expectedOutput = new JSONObject()
                .put("requestID", 9)
                .put("userID", "45")
                .put("target", READER)
                .put("requestTime", "2023-03-16T16:30:00.861336Z")
                .put("reason", "I wanna be a part of the community!")
                .put("username", "Guest");

        String expectedResult = expectedOutput.toString();
        String actualResult = promotionRequest.asJSONString();

        try {
            JSONAssert.assertEquals(expectedResult, actualResult, false);
        } catch (JSONException je) {
            fail("JSON type is not the same.");
        }
    }

}