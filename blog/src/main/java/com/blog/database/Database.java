package com.blog.database;

import com.blog.exception.BlogException;
import com.blog.exception.DoesNotExistException;
import com.blog.exception.IsDeletedException;
import com.blog.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all calls to the database related to the blog application.
 */

public class Database {

    private static JdbcTemplate jdbcTemplate;

    public static void createTemplate() {
        DataSource dataSource = DatabaseConfig.dataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * @param comment
     */
    public static void retrieve(Comment comment) throws DoesNotExistException {
        int postID = comment.getPostID();
        int commentID = comment.getCommentID();
        String sql = "SELECT * FROM Comment WHERE post_ID = " + postID + " AND comment_number = " + commentID;
        if (jdbcTemplate == null) {
            createTemplate();
        }
        try {
            Comment temp = jdbcTemplate.queryForObject(sql, new CommentRowMapper());
            comment.copy(temp);
        } catch (EmptyResultDataAccessException e) {
            throw new DoesNotExistException("Comment with postID " + postID + " and commentID " + commentID + " does not exist.");
        }
    }

    /**
     * TODO
     *
     * @param post
     */
    public static void retrieve(Post post) throws DoesNotExistException {
        int postID = post.getPostID();
        String sql = "SELECT * FROM Post WHERE post_ID = " + postID;
        if (jdbcTemplate == null) {
            createTemplate();
        }
        try {
            Post temp = jdbcTemplate.queryForObject(sql, new PostRowMapper());
            post.copy(temp);
        } catch (EmptyResultDataAccessException e) {
            throw new DoesNotExistException("Post with postID " + postID + " does not exist.");
        }
    }

    /**
     * Retrieves the user record from the database with corresponding <code>userID</code> specified
     * by the given <code>User</code> object and updates its fields.
     *
     * @param user The <code>User</code> object to update. Contains the <code>userID</code>.
     * @throws DoesNotExistException
     * @throws IsDeletedException
     */
    public static void retrieve(User user) throws DoesNotExistException, IsDeletedException {
        String userID = user.getUserID();
        String sql = "SELECT * FROM User WHERE user_ID = \"" + userID + "\"";
        if (jdbcTemplate == null) {
            createTemplate();
        }
        try {
            User temp = jdbcTemplate.queryForObject(sql, new UserRowMapper());
            user.copy(temp);
            if (temp.isDeleted()) {
                throw new IsDeletedException("User with ID " + userID + " is deleted.");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new DoesNotExistException("User with ID " + userID + " does not exist.");
        }
	   
        /*
              Use userID to select corresponding user record from database.
              Then use setter functions from User to fill out the private fields
              Maybe throw an exception if user does not exist, e.g. throw new UserDoesNotExistException()
              If user is deleted maybe throw new UserIsDeletedException()
         */
    }

    /**
     * Retrieves the next <code>count</code> displayable comments starting from <code>commentIDStart</code> from
     * the post with post ID <code>postID</code> into <code>comments</code>.
     *
     * @param comments
     * @param postID
     * @param commentIDStart
     * @param count
     */
    public static void retrieve(ArrayList<Comment> comments, int postID, int commentIDStart, int count, boolean reverse) {
        String sql;
        if (reverse) {
            sql = "SELECT * FROM Comment WHERE post_ID = " + postID + " AND comment_number <= " + commentIDStart + " AND is_deleted = false ORDER BY comment_number DESC LIMIT " + count;
        } else {
            sql = "SELECT * FROM Comment WHERE post_ID = " + postID + " AND comment_number >= " + commentIDStart + " AND is_deleted = false ORDER BY comment_number ASC LIMIT " + count;
        }
        if (jdbcTemplate == null) {
            createTemplate();
        }
        List<Comment> temp = jdbcTemplate.query(sql, new CommentRowMapper());
        for (Comment c : temp) {
            comments.add(c);
        }
    }

    /**
     * Retrieves the next <code>count</code> displayable posts starting from <code>postIDStart</code>
     * into <code>posts</code>.
     *
     * @param posts
     * @param postIDStart
     * @param count
     */
    public static void retrieve(ArrayList<Post> posts, int postIDStart, int count, boolean reverse) {
        String sql;
        if (reverse) {
            sql = "SELECT * FROM Post WHERE post_ID <= " + postIDStart + " AND is_deleted = false ORDER BY post_ID DESC LIMIT " + count;
        } else {
            sql = "SELECT * FROM Post WHERE post_ID >= " + postIDStart + " AND is_deleted = false ORDER BY post_ID ASC LIMIT " + count;
        }
        if (jdbcTemplate == null) {
            createTemplate();
        }
        List<Post> temp = jdbcTemplate.query(sql, new PostRowMapper());
        for (Post p : temp) {
            posts.add(p);
        }
    }

    /**
     * @param comment
     * @return the commentID
     */
    public static int save(Comment comment) {
        int postID = comment.getPostID();
        int commentID = comment.getCommentID();
        String sql = "SELECT MAX(comment_number) FROM Comment";
        if (jdbcTemplate == null) {
            createTemplate();
        }
        int maxID;
        try {
            maxID = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            maxID = 0;
        }
        if (maxID != 0 && commentID > maxID) {
            throw new Error("Invalid comment ID.");
        }
        if (commentID != 0) {
            sql = formUpdate(comment, commentID);
        } else {
            commentID = maxID + 1;
            sql = formInsert(comment, commentID);
        }
        jdbcTemplate.update(sql);
        return postID;
        /*
              if keys already exist in database, update
              if postID does not exist throw error
              if commentID == 0, create new record with commentID being next smallest assignable ID
              if commentID anything else throw error
              Note: commentID == 0 reserved for creating new record
         */
    }

    /**
     * @param post
     * @return the postID
     */
    public static int save(Post post) {
        int postID = post.getPostID();
        String sql = "SELECT MAX(post_id) FROM Post";
        if (jdbcTemplate == null) {
            createTemplate();
        }
        int maxID;
        try {
            maxID = jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            maxID = 0;
        }
        if (maxID != 0 && postID > maxID) {
            throw new Error("Invalid post ID.");
        }
        if (postID != 0) {
            sql = formUpdate(post, postID);
        } else {
            postID = maxID + 1;
            sql = formInsert(post, postID);
        }
        jdbcTemplate.update(sql);
        return postID;

        /*
              if key already exist in database, update
              if postID == 0, create new record with postID being next smallest assignable ID
              if postID anything else throw error
              Note: postID == 0 reserved for creating new record
         */
    }

    /**
     * Saves the given <code>User</code> object into the database.
     *
     * @param user The <code>User</code> object to save. Contains the <code>userID</code>.
     * @return the userID
     */
    public static String save(User user) {
        String userID = user.getUserID();
        String sql = "SELECT COUNT(*) FROM User WHERE user_ID = " + userID;
        if (jdbcTemplate == null) {
            createTemplate();
        }
        if (jdbcTemplate.queryForObject(sql, Integer.class) == 1) {
            sql = formUpdate(user, userID);
        } else {
            sql = formInsert(user, userID);
        }
        jdbcTemplate.update(sql);
        return userID;
        // Note that since user ID is final, you will have to create a new user later for further use.
        /*
              if key already exist in database, update
              if userID == 0, create new record with userID being next smallest assignable ID
              if userID anything else throw error
              Note: userID == 0 reserved for creating new record
         */
    }

    /**
     * TODO
     *
     * @param comment
     */
    public static void delete(Comment comment) {
        int postID = comment.getPostID();
        int commentID = comment.getCommentID();
        String sql = "UPDATE Comment SET is_deleted = true WHERE post_ID = " + postID + " AND comment_number = " + commentID;
        if (jdbcTemplate == null) {
            createTemplate();
        }
        jdbcTemplate.update(sql);
        comment.setDeleted(true);
    }

    /**
     * @param post
     */
    public static void delete(Post post) {
        int postID = post.getPostID();
        String sql = "UPDATE Post SET is_deleted = true WHERE post_ID = " + postID;
        if (jdbcTemplate == null) {
            createTemplate();
        }
        jdbcTemplate.update(sql);
        post.setDeleted(true);
    }

    /**
     * Saves the given <code>User</code> object into the database.
     *
     * @param user The <code>User</code> object to save. Contains the <code>userID</code>.
     */

    //todo:need to change as the user id is different now
    public static void delete(User user) {
        String userID = user.getUserID();
        String sql = "UPDATE User SET is_deleted = true WHERE user_ID = \"" + userID + "\"";
        if (jdbcTemplate == null) {
            createTemplate();
        }
        jdbcTemplate.update(sql);
        user.setDeleted(true);

        // Note that there is no warning even if the user does not exist.
    }

    /**
     * @param userID The user trying to upvote.
     * @param postID The post to upvote.
     * @throws BlogException If the user already upvoted this post.
     */
    public static void upvote(String userID, int postID) throws BlogException {
        /*       will also need new table with key (userID, postID)
                 also need column for whether they upvoted or downvoted (binary column)

                 if user has neither upvoted nor downvoted
                     normal insert
                     increment upvote counter for the post
                 else if user already downvoted
                     change downvote to upvote
                     increment upvote counter for the post
                     decrement downvote counter for the post
                 else if user already upvoted
                     throw new BlogException("User already upvoted this post.")
                 else
                     unexpected
         */
        if (jdbcTemplate == null) {
            createTemplate();
        }
        String sql = "SELECT COUNT(*) FROM Vote_Post WHERE user_ID = " + userID + " AND post_ID = " + postID;
        if (jdbcTemplate.queryForObject(sql, Integer.class) == 0) {
            sql = "INSERT INTO Vote_Post VALUES(" + postID + ", " + userID + ", true)";
            jdbcTemplate.update(sql);
            Post post = Post.retrieve(postID);
            post.setUpvotes(post.getUpvotes() + 1);
            save(post);
        } else {
            sql = "SELECT is_upvoted FROM Vote_Post WHERE user_ID = " + userID + " AND post_ID = " + postID;
            if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class))) {
                throw new BlogException("User already upvoted this post.");
            } else {
                sql = "UPDATE Vote_Post SET is_deleted = true WHERE user_ID = " + userID + " AND post_ID = " + postID;
                jdbcTemplate.update(sql);
                Post post = Post.retrieve(postID);
                post.setDownvotes(post.getDownvotes() - 1);
                post.setUpvotes(post.getUpvotes() + 1);
                save(post);
            }
        }
    }

    /**
     * @param userID The user trying to downvote.
     * @param postID The post to downvote.
     * @throws BlogException If the user already downvoted this post.
     */
    public static void downvote(String userID, int postID) throws BlogException {
        /*       should same table as upvote
                 similar behaviour as upvote but flipped
         */
        if (jdbcTemplate == null) {
            createTemplate();
        }
        String sql = "SELECT COUNT(*) FROM Vote_Post WHERE user_ID = " + userID + " AND post_ID = " + postID;
        if (jdbcTemplate.queryForObject(sql, Integer.class) == 0) {
            sql = "INSERT INTO Vote_Post VALUES(" + postID + ", " + userID + ", false)";
            jdbcTemplate.update(sql);
            Post post = Post.retrieve(postID);
            post.setUpvotes(post.getDownvotes() + 1);
            save(post);
        } else {
            sql = "SELECT is_upvoted FROM Vote_Post WHERE user_ID = " + userID + " AND post_ID = " + postID;
            if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class))) {
                sql = "UPDATE Vote_Post SET is_deleted = false WHERE user_ID = " + userID + " AND post_ID = " + postID;
                jdbcTemplate.update(sql);
                Post post = Post.retrieve(postID);
                post.setUpvotes(post.getUpvotes() - 1);
                post.setDownvotes(post.getDownvotes() + 1);
                save(post);
            } else {
                throw new BlogException("User already downvoted this post.");
            }
        }
    }

    /**
     * Increments the view counter of the given post.
     *
     * @param postID The post.
     * @throws DoesNotExistException
     */
    public static void view(int postID) throws DoesNotExistException {
        if (jdbcTemplate == null) {
            createTemplate();
        }
        try {
            String sql = "SELECT views FROM Post WHERE post_ID = " + postID;
            int view = jdbcTemplate.queryForObject(sql, Integer.class);
            view++;
            sql = "UPDATE Post SET views = " + view + " WHERE post_ID = " + postID;
            jdbcTemplate.update(sql);
        } catch (Exception e) {
            throw new DoesNotExistException("Post does not exist.");
        }
    }

    /**
     * Increments the view counter of the given comment.
     *
     * @param postID    The post.
     * @param commentID The comment.
     */
    public static void view(int postID, int commentID) {
        // TODO
    }

    /**
     * @param userID    The user trying to upvote.
     * @param postID    The post to upvote.
     * @param commentID The post to upvote.
     * @throws BlogException If the user already upvoted this post.
     */
    public static void upvote(String userID, int postID, int commentID) throws BlogException {
        /*       will also need new table with key (userID, postID, commentID)
                 also need column for whether they upvoted or downvoted (binary column)

                 similar behaviour as upvote post but for comments
         */
        if (jdbcTemplate == null) {
            createTemplate();
        }
        String sql = "SELECT COUNT(*) FROM Vote_Comment WHERE user_ID = " + userID + " AND post_ID = " + postID + " AND comment_number = " + commentID;
        if (jdbcTemplate.queryForObject(sql, Integer.class) == 0) {
            sql = "INSERT INTO Vote_Comment VALUES(" + postID + ", " + userID + ", true)";
            jdbcTemplate.update(sql);
            Comment comment = Comment.retrieve(postID, commentID);
            comment.setUpvotes(comment.getUpvotes() + 1);
            save(comment);
        } else {
            sql = "SELECT is_upvoted FROM Vote_Comment WHERE user_ID = " + userID + " AND post_ID = " + postID + " AND comment_number = " + commentID;
            if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class))) {
                throw new BlogException("User already upvoted this comment.");
            } else {
                sql = "UPDATE Vote_Comment SET is_deleted = true WHERE user_ID = " + userID + " AND post_ID = " + postID + " AND comment_number = " + commentID;
                jdbcTemplate.update(sql);
                Comment comment = Comment.retrieve(postID, commentID);
                comment.setDownvotes(comment.getDownvotes() - 1);
                comment.setUpvotes(comment.getUpvotes() + 1);
                save(comment);
            }
        }
    }

    /**
     * @param userID The user trying to downvote.
     * @param postID The post to downvote.
     * @throws BlogException If the user already downvoted this post.
     */
    public static void downvote(String userID, int postID, int commentID) throws BlogException {
        /*       should same table as upvote

                 similar behaviour as upvote but flipped
         */
        if (jdbcTemplate == null) {
            createTemplate();
        }
        String sql = "SELECT COUNT(*) FROM Vote_Comment WHERE user_ID = " + userID + " AND post_ID = " + postID + " AND comment_number = " + commentID;
        if (jdbcTemplate.queryForObject(sql, Integer.class) == 0) {
            sql = "INSERT INTO Vote_Comment VALUES(" + postID + ", " + userID + ", false)";
            jdbcTemplate.update(sql);
            Comment comment = Comment.retrieve(postID, commentID);
            comment.setUpvotes(comment.getDownvotes() + 1);
            save(comment);
        } else {
            sql = "SELECT is_upvoted FROM Vote_Comment WHERE user_ID = " + userID + " AND post_ID = " + postID + " AND comment_number = " + commentID;
            if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class))) {
                sql = "UPDATE Vote_Comment SET is_deleted = false WHERE user_ID = " + userID + " AND post_ID = " + postID + " AND comment_number = " + commentID;
                jdbcTemplate.update(sql);
                Comment comment = Comment.retrieve(postID, commentID);
                comment.setUpvotes(comment.getUpvotes() - 1);
                comment.setDownvotes(comment.getDownvotes() + 1);
                save(comment);
            } else {
                throw new BlogException("User already downvoted this comment.");
            }
        }
    }

    /**
     * @param request The promotion request.
     * @return The requestID
     * @throws DoesNotExistException If the user does not exist.
     */
    public static int save(PromotionRequest request) throws DoesNotExistException {
        /*
              if user has active request (not deleted), update and return the existing requestID
              otherwise generate incrementing requestID then insert and return the generated requestID
              throw new DoesNotExistException("User does not exist.") if userID not in User table
         */
        if (jdbcTemplate == null) {
            createTemplate();
        }
        String userID = request.getUserID();
        int requestID = 0;
        String sql = "SELECT COUNT(*) FROM Promotion_Request WHERE user_ID = " + userID + " AND is_deleted = false";
        if (jdbcTemplate.queryForObject(sql, Integer.class) == 0) {
            int maxID;
            sql = "SELECT MAX(request_ID) FROM Promotion_Request";
            try {
                maxID = jdbcTemplate.queryForObject(sql, Integer.class);
            } catch (Exception e) {
                maxID = 0;
            }
            requestID = maxID + 1;
            sql = formInsert(request, requestID);
        } else {
            sql = "SELECT request_ID FROM Promotion_Request WHERE user_ID = " + userID + " AND is_deleted = false";
            requestID = jdbcTemplate.queryForObject(sql, Integer.class);
            sql = formUpdate(request, requestID);
        }
        jdbcTemplate.update(sql);
        return requestID;
    }

    /**
     * Retrieves the user record from the database with corresponding <code>userID</code> specified
     * by the given <code>User</code> object and updates its fields.
     *
     * @param request The <code>PromotionRequest</code> object to update. Contains the <code>requestID</code>.
     * @throws DoesNotExistException
     * @throws IsDeletedException
     */
    public static void retrieve(PromotionRequest request) throws DoesNotExistException, IsDeletedException {
        if (jdbcTemplate == null) {
            createTemplate();
        }
        int requestID = request.getRequestID();
        String sql = "SELECT * FROM Promotion_Request WHERE request_ID = " + requestID;
        try {
            PromotionRequest temp = jdbcTemplate.queryForObject(sql, new PromotionRequestRowMapper());
            request.copy(temp);
            if (Boolean.TRUE.equals(temp.isDeleted())) {
                throw new IsDeletedException("Promotion request with ID " + requestID + " is deleted.");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new DoesNotExistException("Promotion request with ID " + requestID +  " does not exist.");
        }
    }

    /**
     * Retrieves the next <code>count</code> displayable requests starting from <code>requestIDStart</code>
     * into <code>requests</code>.
     *
     * @param requests
     * @param requestIDStart
     * @param count
     * @param reverse
     */
    public static void retrievePromotionRequests(ArrayList<PromotionRequest> requests, int requestIDStart, int count, boolean reverse) {
        String sql;
        if (reverse) {
            sql = "SELECT * FROM Promotion_Request WHERE request_ID <= " + requestIDStart + " AND is_deleted = false ORDER BY request_ID DESC LIMIT " + count;
        } else {
            sql = "SELECT * FROM Promotion_Request WHERE request_ID >= " + requestIDStart + " AND is_deleted = false ORDER BY request_ID ASC LIMIT " + count;
        }
        if (jdbcTemplate == null) {
            createTemplate();
        }
        List<PromotionRequest> temp = jdbcTemplate.query(sql, new PromotionRequestRowMapper());
        for (PromotionRequest r : temp) {
            requests.add(r);
        }
    }

    /**
     * @param request
     */
    public static void delete(PromotionRequest request) {
        if (jdbcTemplate == null) {
            createTemplate();
        }
        int requestID = request.getRequestID();
        String sql = "UPDATE Promotion_Request SET is_deleted = true WHERE request_ID = " + requestID;
        jdbcTemplate.update(sql);
        request.setDeleted(true);
    }

    /**
     * Promotes or demotes the given user to the target user level.
     *
     * @param userID The user to promote or demote.
     * @param target The target user level.
     * @throws DoesNotExistException If the user does not exist.
     */
    public static void promote(String userID, UserLevel target) throws DoesNotExistException {
        /*
              Change user level of userID to the target.
              throw new DoesNotExistException("User does not exist.") if userID not in User table
              After changing user level, hard delete all promotion requests that
              this user has that is not higher than their current new level.
         */
        try{
            User user = User.retrieveByUserID(userID);
            user.setUserLevel(target);
            save(user);
        } catch (Exception e) {
            throw new DoesNotExistException("User does not exist.");
        }
        int level = 1;
        if (target == UserLevel.CONTRIBUTOR) {
            level = 2;
        } else if (target == UserLevel.ADMIN) {
            level = 3;
        }
        String sql = "DELETE FROM Promotion_Request WHERE user_ID = " + userID + "AND target_level <= " + level;
        jdbcTemplate.update(sql);
    }

    private static String formInsert(User user, String id) {
        String profile = "DEFAULT";
        if (user.getProfilePicture() != null) {
            profile = "\"" + user.getProfilePicture() + "\"";
        }
        int level = 1;
        if (user.getUserLevel() == UserLevel.CONTRIBUTOR) {
            level = 2;
        } else if (user.getUserLevel() == UserLevel.ADMIN) {
            level = 3;
        }
        int status = 0;
        if (user.getUserStatus() == UserStatus.AWAY) {
            status = 1;
        } else if (user.getUserStatus() == UserStatus.BUSY) {
            status = 2;
        } else if (user.getUserStatus() == UserStatus.OFFLINE) {
            status = 3;
        }
        String bio = user.getBio();
        String creationDate = user.getCreationDate();
        String lastLogin = user.getLastLogin();
        return "INSERT INTO User VALUES(\"" + id + "\", \"" + user.getUsername()
                + "\", \"" + creationDate + "\", \"" + lastLogin + "\", " + status + ", " + profile + ", \"" + bio + "\", " + level + ", " + user.isDeleted() + ")";
    }

    private static String formUpdate(User user, String id) {
        String profile = "NULL";
        if (user.getProfilePicture() != null) {
            profile = "\"" + user.getProfilePicture() + "\"";
        }
        String bio = "NULL";
        if (user.getBio() != null) {
            bio = "\"" + user.getBio() + "\"";
        }
        int level = 1;
        if (user.getUserLevel() == UserLevel.CONTRIBUTOR) {
            level = 2;
        } else if (user.getUserLevel() == UserLevel.ADMIN) {
            level = 3;
        }
        int status = 0;
        if (user.getUserStatus() == UserStatus.AWAY) {
            status = 1;
        } else if (user.getUserStatus() == UserStatus.BUSY) {
            status = 2;
        } else if (user.getUserStatus() == UserStatus.OFFLINE) {
            status = 3;
        }
        String creationDate = user.getCreationDate();
        String lastLogin = user.getLastLogin();

        return "UPDATE User SET username = \"" + user.getUsername() + "\", creation_date = \"" + creationDate + "\", last_login = \"" + lastLogin
                + "\", user_status = " + status + ", profile_picture = " + profile + ", bio = " + bio + ", user_level = " + level + ", is_deleted = " + user.isDeleted()
                + " WHERE user_ID = \"" + id + "\"";
    }

    private static String formInsert(Post post, int id) {
        String creationDate = post.getCreationDate();
        String lastModified = post.getLastModified();
        return "INSERT INTO Post VALUES(" + id + ", \"" + post.getAuthorID() + "\", \"" + post.getTitle() + "\", \"" + post.getContent() + "\", \""
                + creationDate + "\", \"" + lastModified + "\", " + post.getUpvotes() + ", " + post.getDownvotes() + ", "
                + post.getViews() + ", " + post.isDeleted() + ", " + post.isAllowComments() + ")";
    }

    private static String formUpdate(Post post, int id) {
        String creationDate = post.getCreationDate();
        String lastModified = post.getLastModified();
        return "UPDATE Post SET user_ID = \"" + post.getAuthorID() + "\", title = \"" + post.getTitle() + "\", content = \"" + post.getContent() + "\", creation_date = \"" + creationDate
                + "\", last_modified = \"" + lastModified + "\", upvotes = " + post.getUpvotes() + ", downvotes = " + post.getDownvotes() + ", views = " + post.getViews() + ", is_deleted = "
                + post.isDeleted() + ", allow_comments = " + post.isAllowComments() + " WHERE post_ID = " + id;
    }

    private static String formInsert(Comment comment, int id) {
        String creationDate = comment.getCreationDate();
        String lastModified = comment.getLastModified();
        return "INSERT INTO Comment VALUES(" + comment.getPostID() + ", " + id + ", \"" + comment.getAuthorID() + "\", \"" + comment.getContent() + "\", \""
                + creationDate + "\", \"" + lastModified + "\", " + comment.getUpvotes() + ", " + comment.getDownvotes() + ", "
                + comment.isDeleted() + ")";
    }

    private static String formUpdate(Comment comment, int id) {
        String creationDate = comment.getCreationDate();
        String lastModified = comment.getLastModified();
        return "UPDATE Comment SET user_ID = \"" + comment.getAuthorID() + "\", content = \"" + comment.getContent() + "\", creation_date = \"" + creationDate
                + "\", last_modified = \"" + lastModified + "\", upvotes = " + comment.getUpvotes() + ", downvotes = " + comment.getDownvotes() + ", is_deleted = "
                + comment.isDeleted() + " WHERE post_ID = " + comment.getPostID() + " AND comment_number = " + id;
    }

    private static String formInsert(PromotionRequest request, int id) {
        String requestTime = request.getRequestTime();
        int level = 1;
        if (request.getTarget() == UserLevel.CONTRIBUTOR) {
            level = 2;
        } else if (request.getTarget() == UserLevel.ADMIN) {
            level = 3;
        }
        return "INSERT INTO Promotion_Request VALUES(" + id + ", \"" + request.getUserID() + "\", " + level + ", \""
                + requestTime + "\",  \"" + request.getReason() + "\", " + request.isDeleted() + ")";
    }

    private static String formUpdate(PromotionRequest request, int id) {
        String requestTime = request.getRequestTime();
        int level = 1;
        if (request.getTarget() == UserLevel.CONTRIBUTOR) {
            level = 2;
        } else if (request.getTarget() == UserLevel.ADMIN) {
            level = 3;
        }
        return "UPDATE Promotion_Request SET user_ID = \"" + request.getUserID() + "\", target_level = " + level + ", request_time = \"" + requestTime
                + "\", reason = \"" + request.getReason() + "\", is_deleted = " + request.isDeleted() + " WHERE request_ID = " + id;
    }
}
