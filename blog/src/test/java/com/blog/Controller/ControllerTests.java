//package com.blog.Controller;
//
//import com.blog.exception.BlogException;
//import com.blog.model.Comment;
//import com.blog.model.UserLevel;
//import com.blog.utils.Utility;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static com.blog.controller.CommentController.retrieveComment;
//import static com.blog.model.UserLevel.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.fail;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.skyscreamer.jsonassert.JSONAssert;
//public class ControllerTests {
//    private Comment comment;
//    private JSONObject inputCommentJSON;
//
//    @BeforeEach
//    void beforeEach() {
//        comment = new Comment(
//                10,
//                11,
//                "12",
//                "Happy New Year!",
//                "2022-01-01T00:00:00.861336Z",
//                "2022-01-02T06:00:00.861336Z",
//                18,
//                1,
//                true
//        );
//        inputCommentJSON = new JSONObject()
//                .put("postID", 10)
//                .put("commentID", 11);
//    }
//
//    @Test
//    void testGetComment() throws BlogException {
//        // Create a test input JSON object
//        JSONObject input = new JSONObject();
//        input.put("commentId", 123);
//
//        // Call the method under test
//        String result = comment.getComment(input);
//
//        // Check the result against the expected output
//        assertEquals("{\"commentId\":123}", result);
//    }
//
//    @Test
//    void testRetrieveComment() throws BlogException {
//        // Create a test input JSON object
//        retrieveComment(inputCommentJSON);
//
//        // Call the method under test
//        String result = comment.getComment(input);
//
//        // Check the result against the expected output
//        assertEquals("{\"commentId\":123}", result);
//    }
//
//}
