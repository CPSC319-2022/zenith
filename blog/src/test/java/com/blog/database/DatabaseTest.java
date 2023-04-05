// package com.blog.database;

// import com.blog.exception.BlogException;
// import com.blog.exception.DoesNotExistException;
// import com.blog.exception.IsDeletedException;
// import com.blog.model.*;
// import org.junit.jupiter.api.Test;

// import java.util.ArrayList;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.fail;

// class DatabaseTest {
//     @Test
//     void testSaveUser() {
//         // Create a user
//         User user = new User("testID");
//         user.setUsername("testname");
//         user.setCreationDate("time1");
//         user.setLastLogin("time2");
//         user.setProfilePicture("link");
//         user.setBio("testbio");
//         user.setUserLevel(UserLevel.CONTRIBUTOR);
//         user.setDeleted(false);

//         // Create another user with same ID but with different info
//         User update = new User("testID");
//         update.setUsername("updatedname");
//         update.setCreationDate("updatedtime1");
//         update.setLastLogin("updatedtime2");
//         update.setProfilePicture("updatedlink");
//         update.setBio("updatedtestbio");
//         update.setUserLevel(UserLevel.ADMIN);
//         update.setDeleted(false);
//         try {
//             // Hard delete to make sure user not in db
//             Database.hardDelete(user);
//             Database.save(user);
//             User result = new User("testID");
//             Database.retrieve(result);
//             assertEquals("testname", result.getUsername());
//             assertEquals("time1", result.getCreationDate());
//             assertEquals("time2", result.getLastLogin());
//             assertEquals("link", result.getProfilePicture());
//             assertEquals(UserLevel.CONTRIBUTOR, result.getUserLevel());
//             assertEquals("testbio", result.getBio());
//             assertEquals(false, result.isDeleted());

//             // Update and check if info changes
//             Database.save(update);
//             User result2 = new User("testID");
//             Database.retrieve(result2);
//             assertEquals("updatedname", result2.getUsername());
//             assertEquals("updatedtime1", result2.getCreationDate());
//             assertEquals("updatedtime2", result2.getLastLogin());
//             assertEquals("updatedlink", result2.getProfilePicture());
//             assertEquals(UserLevel.ADMIN, result2.getUserLevel());
//             assertEquals("updatedtestbio", result2.getBio());
//             assertEquals(false, result2.isDeleted());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testSavePost() {
//         // Create a user as a foreign key of post
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);

//         // Create a new post
//         Post post = new Post(0);
//         post.setAuthorID("testID");
//         post.setTitle("title");
//         post.setContent("content");
//         post.setCreationDate("time1");
//         post.setLastModified("time2");
//         post.setUpvotes(10);
//         post.setDownvotes(5);
//         post.setDeleted(false);
//         post.setViews(30);
//         post.setAllowComments(true);
//         post.setThumbnailURL("link");

//         // Create another post for update
//         Post update = new Post(0);
//         update.setAuthorID("testID");
//         update.setTitle("updatedtitle");
//         update.setContent("updatedcontent");
//         update.setCreationDate("updatedtime1");
//         update.setLastModified("updatedtime2");
//         update.setUpvotes(15);
//         update.setDownvotes(10);
//         update.setDeleted(true);
//         update.setViews(40);
//         update.setAllowComments(false);
//         update.setThumbnailURL("updatedlink");
//         try {
//             // Test for insert
//             Database.save(user);
//             Database.save(post);
//             Post result = new Post(post.getPostID());
//             Database.retrieve(result);
//             assertEquals("testID", result.getAuthorID());
//             assertEquals("title", result.getTitle());
//             assertEquals("content", result.getContent());
//             assertEquals("time1", result.getCreationDate());
//             assertEquals("time2", result.getLastModified());
//             assertEquals(10, result.getUpvotes());
//             assertEquals(5, result.getDownvotes());
//             assertEquals(false, result.isDeleted());
//             assertEquals(30, result.getViews());
//             assertEquals(true, result.isAllowComments());
//             assertEquals("link", result.getThumbnailURL());

//             // Test for update
//             update.setPostID(post.getPostID());
//             Database.save(update);
//             Post result2 = new Post(update.getPostID());
//             Database.retrieve(result2);
//             assertEquals("testID", result2.getAuthorID());
//             assertEquals("updatedtitle", result2.getTitle());
//             assertEquals("updatedcontent", result2.getContent());
//             assertEquals("updatedtime1", result2.getCreationDate());
//             assertEquals("updatedtime2", result2.getLastModified());
//             assertEquals(15, result2.getUpvotes());
//             assertEquals(10, result2.getDownvotes());
//             assertEquals(true, result2.isDeleted());
//             assertEquals(40, result2.getViews());
//             assertEquals(false, result2.isAllowComments());
//             assertEquals("updatedlink", result2.getThumbnailURL());

//             // Delete user so that post will be deleted as well
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testSavePostInvalid() {
//         // Create a user as a foreign key of post
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         // Create a new post with an invaild ID
//         Post post = new Post(99999999);
//         post.setAuthorID("testID");
//         post.setTitle("title");
//         post.setContent("content");
//         post.setCreationDate("time1");
//         post.setLastModified("time2");
//         post.setUpvotes(10);
//         post.setDownvotes(5);
//         post.setDeleted(false);
//         post.setViews(30);
//         post.setAllowComments(true);
//         post.setThumbnailURL("link");
//         try {
//             Database.save(user);
//             Database.save(post);
//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (Error e) {
//             Database.hardDelete(user);
//             // Expected
//         }
//     }

//     @Test
//     void testSaveComment() {
//         // Create a user as a foreign key of comment
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);

//         // Create a user as a foreign key of comment
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");

//         // Create a new comment
//         Comment comment = new Comment(0, 0);
//         comment.setAuthorID("testID");
//         comment.setContent("content");
//         comment.setCreationDate("time1");
//         comment.setLastModified("time2");
//         comment.setUpvotes(10);
//         comment.setDownvotes(5);
//         comment.setDeleted(false);

//         // Create another comment
//         Comment update = new Comment(0, 0);
//         update.setAuthorID("testID");
//         update.setContent("updatedcontent");
//         update.setCreationDate("updatedtime1");
//         update.setLastModified("updatedtime2");
//         update.setUpvotes(15);
//         update.setDownvotes(10);
//         update.setDeleted(true);

//         try {
//             // Test for insert
//             Database.save(user);
//             Database.save(post);
//             comment.setPostID(post.getPostID());
//             Database.save(comment);
//             Comment result = new Comment(comment.getPostID(), comment.getCommentID());
//             Database.retrieve(result);
//             assertEquals("testID", result.getAuthorID());
//             assertEquals("content", result.getContent());
//             assertEquals("time1", result.getCreationDate());
//             assertEquals("time2", result.getLastModified());
//             assertEquals(10, result.getUpvotes());
//             assertEquals(5, result.getDownvotes());
//             assertEquals(false, result.isDeleted());

//             // Test for update
//             update.setPostID(comment.getPostID());
//             update.setCommentID(comment.getCommentID());
//             Database.save(update);
//             Comment result2 = new Comment(update.getPostID(), update.getCommentID());
//             Database.retrieve(result2);
//             assertEquals("testID", result2.getAuthorID());
//             assertEquals("updatedcontent", result2.getContent());
//             assertEquals("updatedtime1", result2.getCreationDate());
//             assertEquals("updatedtime2", result2.getLastModified());
//             assertEquals(15, result2.getUpvotes());
//             assertEquals(10, result2.getDownvotes());
//             assertEquals(true, result2.isDeleted());

//             // Delete user so that post and comment will be deleted as well
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     void testSaveCommentInvalid() {
//         // Create a user as a foreign key of comment
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);

//         // Create a user as a foreign key of comment
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");

//         // Create a new comment
//         Comment comment = new Comment(0, 99999999);
//         comment.setAuthorID("testID");
//         comment.setContent("content");
//         comment.setCreationDate("time1");
//         comment.setLastModified("time2");
//         comment.setUpvotes(10);
//         comment.setDownvotes(5);
//         comment.setDeleted(false);
//         try {
//             Database.save(user);
//             Database.save(post);
//             comment.setPostID(post.getPostID());
//             Database.save(comment);
//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (Error e) {
//             Database.hardDelete(user);
//             // Expected
//         }
//     }

//     @Test
//     void testDeleteUser() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         User result = new User("testID");
//         try {
//             Database.save(user);
//             Database.delete(user);
//             Database.retrieve(result);

//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (IsDeletedException e) {
//             // Expected
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testDeletePost() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");
//         try {
//             Database.save(user);
//             Database.save(post);
//             Database.delete(post);
//             Post result = new Post(post.getPostID());
//             Database.retrieve(result);
//             assertEquals(true, result.isDeleted());
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testDeleteComment() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");
//         Comment comment = new Comment(0, 0, "testID", "0", "0", "0", 0, 0, false);
//         try {
//             Database.save(user);
//             Database.save(post);
//             comment.setPostID(post.getPostID());
//             Database.save(comment);
//             Database.delete(comment);
//             Comment result = new Comment(comment.getPostID(), comment.getCommentID());
//             Database.retrieve(result);
//             assertEquals(true, result.isDeleted());
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testRetrieveUser() {
//         // Create a user
//         User user = new User("testID");
//         user.setUsername("testname");
//         user.setCreationDate("time1");
//         user.setLastLogin("time2");
//         user.setProfilePicture("link");
//         user.setBio("testbio");
//         user.setUserLevel(UserLevel.READER);
//         user.setDeleted(false);
//         try {
//             Database.save(user);
//             User result = new User("testID");
//             Database.retrieve(result);

//             assertEquals("testname", result.getUsername());
//             assertEquals("time1", result.getCreationDate());
//             assertEquals("time2", result.getLastLogin());
//             assertEquals("link", result.getProfilePicture());
//             assertEquals(UserLevel.READER, result.getUserLevel());
//             assertEquals("testbio", result.getBio());
//             assertEquals(false, result.isDeleted());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testRetrieveUserNotExist() {
//         try {
//             User user = new User("not_exist_ID");
//             Database.retrieve(user);
//             fail("Unexpected");
//         } catch (DoesNotExistException e) {
//             // Expected
//         } catch (Exception e) {
//             fail("Unexpected");
//         }
//     }

//     @Test
//     void testRetrieveUserIsDeleted() {
//         // Create a user
//         User user = new User("testID");
//         user.setUsername("testname");
//         user.setCreationDate("time1");
//         user.setLastLogin("time2");
//         user.setProfilePicture("link");
//         user.setBio("testbio");
//         user.setUserLevel(UserLevel.READER);
//         user.setDeleted(true);
//         User result = new User("testID");
//         try {
//             Database.save(user);
//             Database.retrieve(result);

//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (IsDeletedException e) {
//             // Expected
//             assertEquals("testname", result.getUsername());
//             assertEquals("time1", result.getCreationDate());
//             assertEquals("time2", result.getLastLogin());
//             assertEquals("link", result.getProfilePicture());
//             assertEquals(UserLevel.READER, result.getUserLevel());
//             assertEquals("testbio", result.getBio());
//             assertEquals(true, result.isDeleted());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail("Unexpected");
//         }
//     }

//     @Test
//     void testRetrievePostNotExist() {
//         try {
//             Post post = new Post(99999999);
//             Database.retrieve(post);
//             fail("Unexpected");
//         } catch (DoesNotExistException e) {
//             // Expected
//         } catch (Exception e) {
//             fail("Unexpected");
//         }
//     }

//     @Test
//     void testRetrieveCommentNotExist() {
//         try {
//             Comment comment = new Comment(99999999, 99999999);
//             Database.retrieve(comment);
//             fail("Unexpected");
//         } catch (DoesNotExistException e) {
//             // Expected
//         } catch (Exception e) {
//             fail("Unexpected");
//         }
//     }

//     @Test
//     void testRetrieveMultiplePost() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         ArrayList<Post> posts = new ArrayList<Post>();
//         try {
//             Database.save(user);
//             for (int i = 0; i < 5; i++) {
//                 Post post = new Post(0, "testID", "title " + i, "0", "0", "0", 0, 0, false, 0, true, "0");
//                 Database.save(post);
//                 posts.add(post);
//             }
//             ArrayList<Post> result = new ArrayList<Post>();
//             Database.retrieve(result, posts.get(0).getPostID(), 5, false);
//             assertEquals(5, result.size());
//             for (int i = 0; i < 5; i++) {
//                 assertEquals(posts.get(i).getTitle(), result.get(i).getTitle());
//             }
//             ArrayList<Post> result2 = new ArrayList<Post>();
//             Database.retrieve(result2, posts.get(4).getPostID(), 2, true);
//             assertEquals(2, result2.size());
//             assertEquals(posts.get(4).getTitle(), result2.get(0).getTitle());
//             assertEquals(posts.get(3).getTitle(), result2.get(1).getTitle());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testRetrieveMultipleComment() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");
//         ArrayList<Comment> comments = new ArrayList<Comment>();
//         try {
//             Database.save(user);
//             Database.save(post);
//             for (int i = 0; i < 5; i++) {
//                 Comment comment = new Comment(post.getPostID(), 0, "testID", "content" + i, "0", "0", 0, 0, false);
//                 Database.save(comment);
//                 comments.add(comment);
//             }
//             ArrayList<Comment> result = new ArrayList<Comment>();
//             Database.retrieve(result, post.getPostID(), comments.get(0).getCommentID(), 5, false);
//             assertEquals(5, result.size());
//             for (int i = 0; i < 5; i++) {
//                 assertEquals(comments.get(i).getContent(), result.get(i).getContent());
//             }
//             ArrayList<Comment> result2 = new ArrayList<Comment>();
//             Database.retrieve(result2, post.getPostID(), comments.get(4).getCommentID(), 2, true);
//             assertEquals(2, result2.size());
//             assertEquals(comments.get(4).getContent(), result2.get(0).getContent());
//             assertEquals(comments.get(3).getContent(), result2.get(1).getContent());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testRetrieveByUser() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         ArrayList<Post> posts = new ArrayList<Post>();
//         try {
//             Database.save(user);
//             for (int i = 0; i < 5; i++) {
//                 Post post = new Post(0, "testID", "title " + i, "0", "0" + i, "0", 0, 0, false, 0, true, "0");
//                 Database.save(post);
//                 posts.add(post);
//             }
//             ArrayList<Post> result = new ArrayList<Post>();
//             Database.retrieveByUser(result, "testID", 3);
//             assertEquals(3, result.size());
//             assertEquals(posts.get(4).getTitle(), result.get(0).getTitle());
//             assertEquals(posts.get(3).getTitle(), result.get(1).getTitle());
//             assertEquals(posts.get(2).getTitle(), result.get(2).getTitle());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testUpvoteDownvotePost() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");
//         try {
//             Database.save(user);
//             Database.save(post);
//             Database.upvote(user.getUserID(), post.getPostID());
//             Database.retrieve(post);
//             assertEquals(1, post.getUpvotes());
//             assertEquals(0, post.getDownvotes());

//             Database.downvote(user.getUserID(), post.getPostID());
//             Database.retrieve(post);
//             assertEquals(0, post.getUpvotes());
//             assertEquals(1, post.getDownvotes());

//             Database.downvote(user.getUserID(), post.getPostID());
//             Database.retrieve(post);

//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (BlogException e) {
//             // Expected
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testDownvoteUpvotePost() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");
//         try {
//             Database.save(user);
//             Database.save(post);
//             Database.downvote(user.getUserID(), post.getPostID());
//             Database.retrieve(post);
//             assertEquals(0, post.getUpvotes());
//             assertEquals(1, post.getDownvotes());

//             Database.upvote(user.getUserID(), post.getPostID());
//             Database.retrieve(post);
//             assertEquals(1, post.getUpvotes());
//             assertEquals(0, post.getDownvotes());

//             Database.upvote(user.getUserID(), post.getPostID());
//             Database.retrieve(post);

//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (BlogException e) {
//             // Expected
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testUpvoteDownvoteComment() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");
//         Comment comment = new Comment(0, 0, "testID", "0", "0", "0", 0, 0, false);
//         try {
//             Database.save(user);
//             Database.save(post);
//             comment.setPostID(post.getPostID());
//             Database.save(comment);
//             Database.upvote(user.getUserID(), comment.getPostID(), comment.getCommentID());
//             Database.retrieve(comment);
//             assertEquals(1, comment.getUpvotes());
//             assertEquals(0, comment.getDownvotes());

//             Database.downvote(user.getUserID(), comment.getPostID(), comment.getCommentID());
//             Database.retrieve(comment);
//             assertEquals(0, comment.getUpvotes());
//             assertEquals(1, comment.getDownvotes());

//             Database.downvote(user.getUserID(), comment.getPostID(), comment.getCommentID());
//             Database.retrieve(comment);

//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (BlogException e) {
//             // Expected
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testDownvoteUpvoteComment() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");
//         Comment comment = new Comment(0, 0, "testID", "0", "0", "0", 0, 0, false);
//         try {
//             Database.save(user);
//             Database.save(post);
//             comment.setPostID(post.getPostID());
//             Database.save(comment);
//             Database.downvote(user.getUserID(), comment.getPostID(), comment.getCommentID());
//             Database.retrieve(comment);
//             assertEquals(0, comment.getUpvotes());
//             assertEquals(1, comment.getDownvotes());

//             Database.upvote(user.getUserID(), comment.getPostID(), comment.getCommentID());
//             Database.retrieve(comment);
//             assertEquals(1, comment.getUpvotes());
//             assertEquals(0, comment.getDownvotes());

//             Database.upvote(user.getUserID(), comment.getPostID(), comment.getCommentID());
//             Database.retrieve(comment);

//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (BlogException e) {
//             // Expected
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testSearchNew() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         ArrayList<Post> posts = new ArrayList<Post>();
//         try {
//             Database.save(user);
//             for (int i = 0; i < 5; i++) {
//                 Post post = new Post(0, "testID", "testpostfortestingsearch " + i, "0", i + "", "0", 0, 0, false, 0,
//                         true, "0");
//                 Database.save(post);
//                 posts.add(post);
//             }
//             ArrayList<Post> result = new ArrayList<Post>();
//             Database.search(result, "testpostfortestingsearch", 2, 2, "new");
//             assertEquals(2, result.size());
//             assertEquals(posts.get(3).getTitle(), result.get(0).getTitle());
//             assertEquals(posts.get(2).getTitle(), result.get(1).getTitle());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testSearchOld() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         ArrayList<Post> posts = new ArrayList<Post>();
//         try {
//             Database.save(user);
//             for (int i = 0; i < 5; i++) {
//                 Post post = new Post(0, "testID", "testpostfortestingsearch " + i, "0", i + "", "0", 0, 0, false, 0,
//                         true, "0");
//                 Database.save(post);
//                 posts.add(post);
//             }
//             ArrayList<Post> result = new ArrayList<Post>();
//             Database.search(result, "testpostfortestingsearch", 2, 2, "old");
//             assertEquals(2, result.size());
//             assertEquals(posts.get(1).getTitle(), result.get(0).getTitle());
//             assertEquals(posts.get(2).getTitle(), result.get(1).getTitle());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testSearchTop() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         ArrayList<Post> posts = new ArrayList<Post>();
//         try {
//             Database.save(user);
//             for (int i = 0; i < 5; i++) {
//                 Post post = new Post(0, "testID", "testpostfortestingsearch " + i, "0", "0", "0", i, 0, false, 0, true,
//                         "0");
//                 Database.save(post);
//                 posts.add(post);
//             }
//             ArrayList<Post> result = new ArrayList<Post>();
//             Database.search(result, "testpostfortestingsearch", 2, 2, "top");
//             assertEquals(2, result.size());
//             assertEquals(posts.get(3).getTitle(), result.get(0).getTitle());
//             assertEquals(posts.get(2).getTitle(), result.get(1).getTitle());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testSearchView() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         ArrayList<Post> posts = new ArrayList<Post>();
//         try {
//             Database.save(user);
//             for (int i = 0; i < 5; i++) {
//                 Post post = new Post(0, "testID", "testpostfortestingsearch " + i, "0", "0", "0", 0, 0, false, i, true,
//                         "0");
//                 Database.save(post);
//                 posts.add(post);
//             }
//             ArrayList<Post> result = new ArrayList<Post>();
//             Database.search(result, "testpostfortestingsearch", 2, 2, "view");
//             assertEquals(2, result.size());
//             assertEquals(posts.get(3).getTitle(), result.get(0).getTitle());
//             assertEquals(posts.get(2).getTitle(), result.get(1).getTitle());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }
    
//     @Test
//     void testSavePromotionRequest() {
//         // Create a user as a foreign key of promotion request
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);

//         // Create a new promotion request
//         PromotionRequest request = new PromotionRequest(0);
//         request.setUserID("testID");
//         request.setRequestTime("time");
//         request.setTarget(UserLevel.CONTRIBUTOR);
//         request.setReason("reason");
//         request.setDeleted(false);

//         // Create another promotion request for update
//         PromotionRequest update = new PromotionRequest(0);
//         update.setUserID("testID");
//         update.setRequestTime("updatedtime");
//         update.setTarget(UserLevel.ADMIN);
//         update.setReason("updatedtimereason");
//         update.setDeleted(false);
//         try {
//             Database.save(user);
//             // Test for insert
//             Database.save(request);
//             PromotionRequest result = new PromotionRequest(request.getRequestID());
//             Database.retrieve(result);
//             assertEquals("testID", result.getUserID());
//             assertEquals("time", result.getRequestTime());
//             assertEquals(UserLevel.CONTRIBUTOR, result.getTarget());
//             assertEquals("reason", result.getReason());
//             assertEquals(false, result.isDeleted());

//             // Test for update
//             update.setRequestID(request.getRequestID());
//             Database.save(update);
//             PromotionRequest result2 = new PromotionRequest(update.getRequestID());
//             Database.retrieve(result2);
//             assertEquals("testID", result2.getUserID());
//             assertEquals("updatedtime", result2.getRequestTime());
//             assertEquals(UserLevel.ADMIN, result2.getTarget());
//             assertEquals("updatedtimereason", result2.getReason());
//             assertEquals(false, result2.isDeleted());

//             // Delete user so that request will be deleted as well
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail(e.getMessage());
//         }
//     }

//     @Test
//     void testSavePromotionRequestInvalid() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         try {
//             // Create a user as a foreign key of promotion request
//             Database.save(user);

//             // Create a new promotion request
//             PromotionRequest request = new PromotionRequest(99999999);
//             request.setUserID("testID");
//             request.setRequestTime("time");
//             request.setTarget(UserLevel.CONTRIBUTOR);
//             request.setReason("reason");
//             request.setDeleted(false);

//             Database.save(request);
//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (Error e) {
//             Database.hardDelete(user);
//             // Expected
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail("Unexpected");
//         }
//     }

//     @Test
//     void testPromote() {
//         User user = new User("testID", "0", UserLevel.READER, "0", "0", null, "0", "0", false);
//         PromotionRequest request = new PromotionRequest(0, "testID", UserLevel.CONTRIBUTOR, "0", "0", false);
//         try {
//             Database.save(user);
//             Database.save(request);
//             Database.promote(user.getUserID(), UserLevel.CONTRIBUTOR);

//             Database.retrieve(user);
//             assertEquals(UserLevel.CONTRIBUTOR, user.getUserLevel());
//             Database.retrieve(request);

//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (BlogException e) {
//             // Expected
//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail("Unexpected " + e.getMessage());
//         }
//     }

//     @Test
//     void testDeletePromotionRequest() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         PromotionRequest request = new PromotionRequest(0);
//         request.setUserID("testID");
//         request.setRequestTime("time");
//         request.setTarget(UserLevel.CONTRIBUTOR);
//         request.setReason("reason");
//         request.setDeleted(false);
//         try {
//             Database.save(user);
//             Database.save(request);
//             Database.delete(request);
//             Database.retrieve(request);

//             Database.hardDelete(user);
//             fail("Unexpected");
//         } catch (IsDeletedException e) {
//             Database.hardDelete(user);
//             // Expected
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail("Unexpected");
//         }
//     }

//     @Test
//     void testView() {
//         User user = new User("testID", "0", UserLevel.CONTRIBUTOR, "0", "0", null, "0", "0", false);
//         Post post = new Post(0, "testID", "0", "0", "0", "0", 0, 0, false, 0, true, "0");
//         try {
//             Database.save(user);
//             Database.save(post);
//             Database.view(post.getPostID());
//             Database.retrieve(post);
//             assertEquals(1, post.getViews());

//             Database.hardDelete(user);
//         } catch (Exception e) {
//             Database.hardDelete(user);
//             fail("Unexpected");
//         }
//     }

//     @Test
//     void testViewNotExist() {
//         try {
//             Database.view(99999999);
//             fail("Unexpected");
//         } catch (DoesNotExistException e) {
//             // Expected
//         } catch (Exception e) {
//             fail("Unexpected");
//         }
//     }

//     @Test
//     void testHighestPostID() {
//         int highest = 0;
//         try {
//             highest = Database.highestPostID();
//             if (highest != 0) {
//                 Post post = new Post(highest);
//                 Database.retrieve(post);
//             }
//         } catch (Exception e) {
//             fail("Unexpected");
//         }
//         try {
//             if (highest != 0) {
//                 Post post = new Post(highest + 1);
//                 Database.retrieve(post);
//             }
//             fail("Unexpected");
//         } catch (DoesNotExistException e) {
//             // Expected
//         } catch (Exception e) {
//             fail("Unexpected");
//         }
//     }
// }