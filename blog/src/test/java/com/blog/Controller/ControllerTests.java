package com.blog.Controller;

import com.blog.exception.BlogException;
import com.blog.model.Comment;
import com.blog.model.UserLevel;
import com.blog.utils.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.blog.controller.CommentController.getComment;
import static com.blog.controller.CommentController.retrieveComment;
import static com.blog.model.UserLevel.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public class ControllerTests {
    private Comment comment;
    private JSONObject inputCommentJSON;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void beforeEach() {
        comment = new Comment(
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
        inputCommentJSON = new JSONObject()
                .put("postID", 10)
                .put("commentID", 11);
    }

    @Test
    void testGetCommentSuccess() {
        int postID = 0;
        int commentID = 0;

        ResponseEntity<String> response = getComment(postID, commentID);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONObject expected = new JSONObject()
                .put("postID", postID)
                .put("commentID", commentID);

        JSONAssert.assertEquals(expected.toString(), response.getBody(), JSONCompareMode.STRICT);
    }

    @Test
    public void testGetCommentDoesNotExist() {
        int postID = 789;
        int commentID = 101112;

        ResponseEntity<String> response = getComment(postID, commentID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getCommentBadRequestTest() {
        // TODO
        ResponseEntity<String> response = getComment("abc", 12);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRetrieveComment() throws BlogException {
        // Create a test input JSON object
        retrieveComment(inputCommentJSON);

        // Call the method under test
        String result = comment.getComment(input);

        // Check the result against the expected output
        assertEquals("{\"commentId\":123}", result);
    }

}
