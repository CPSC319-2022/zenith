package com.blog.database;

import com.blog.exception.BlogException;
import com.blog.exception.DoesNotExistException;
import com.blog.exception.IsDeletedException;
import com.blog.model.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class handles all calls to the database related to the blog application.
 */

public class Database {
    private static Connection connection;

    static {
        // Create the connection to the database
        try {
            connection = DatabaseConfig.dataSource().getConnection();
        } catch (SQLException e) {
            connection = null;
        }
    }

    // TODO: before using this code, change table from cloud shell (necessary)

    private static JdbcTemplate jdbcTemplate;

    public static void createTemplate() {
        DataSource dataSource = DatabaseConfig.dataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * @param comment
     */
    public static void retrieve(Comment comment) throws DoesNotExistException {
        try {
            String sql = """
                    SELECT *
                    FROM Comment
                    WHERE post_ID = ? AND comment_number = ?
                    """;

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, comment.getPostID());
            ps.setInt(2, comment.getCommentID());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new DoesNotExistException(
                        "Comment with postID " + comment.getPostID() + " and commentID " + comment.getCommentID()
                                + " does not exist.");
            }
            comment.setAuthorID(rs.getString("user_ID"));
            comment.setContent(rs.getString("content"));
            comment.setCreationDate(rs.getString("creation_date"));
            comment.setLastModified(rs.getString("last_modified"));
            comment.setUpvotes(rs.getInt("upvotes"));
            comment.setDownvotes(rs.getInt("downvotes"));
            comment.setDeleted(rs.getBoolean("is_deleted"));
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * TODO
     *
     * @param post
     */
    public static void retrieve(Post post) throws DoesNotExistException {
        try {
            String sql = """
                    SELECT *
                    FROM   Post
                    WHERE  post_ID = ?
                    """;

            // Set up the prepared statement
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, post.getPostID());

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // Check that we got a result
            if (!rs.next()) {
                throw new DoesNotExistException("Post with postID " + post.getPostID() + " does not exist.");
            }

            // Retrieve the data from the result
            post.setAuthorID(rs.getString("user_ID"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setCreationDate(rs.getString("creation_date"));
            post.setLastModified(rs.getString("last_modified"));
            post.setUpvotes(rs.getInt("upvotes"));
            post.setDownvotes(rs.getInt("downvotes"));
            post.setDeleted(rs.getBoolean("is_deleted"));
            post.setViews(rs.getInt("views"));
            post.setAllowComments(rs.getBoolean("allow_comments"));
            post.setThumbnailURL(rs.getString("thumbnail_url"));

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Retrieves the user record from the database with corresponding
     * <code>userID</code> specified
     * by the given <code>User</code> object and updates its fields.
     *
     * @param user The <code>User</code> object to update. Contains the
     *             <code>userID</code>.
     * @throws DoesNotExistException
     * @throws IsDeletedException
     */
    public static void retrieve(User user) throws DoesNotExistException, IsDeletedException {
        try {
            String sql = """
                    SELECT *
                    FROM User
                    WHERE user_ID = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUserID());

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new DoesNotExistException("User with ID " + user.getUserID() + " does not exist.");
            }
            user.setUsername(rs.getString("username"));
            user.setCreationDate(rs.getString("creation_date"));
            user.setLastLogin(rs.getString("last_active"));
            user.setProfilePicture(rs.getString("profile_picture"));
            if (rs.getInt("user_level") == 1) {
                user.setUserLevel(UserLevel.READER);
            } else if (rs.getInt("user_level") == 2) {
                user.setUserLevel(UserLevel.CONTRIBUTOR);
            } else if (rs.getInt("user_level") == 3) {
                user.setUserLevel(UserLevel.ADMIN);
            }
            user.setDeleted(rs.getBoolean("is_deleted"));
            user.setBio(rs.getString("bio"));
            if (user.isDeleted()) {
                throw new IsDeletedException("User with ID " + user.getUserID() + " is deleted.");
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Retrieves the next <code>count</code> displayable comments starting from
     * <code>commentIDStart</code> from
     * the post with post ID <code>postID</code> into <code>comments</code>.
     *
     * @param comments
     * @param postID
     * @param commentIDStart
     * @param count
     */
    public static void retrieve(ArrayList<Comment> comments, int postID, int commentIDStart, int count,
            boolean reverse) {
        try {
            String sql;

            if (reverse) {
                sql = """
                        SELECT *
                        FROM Comment
                        WHERE post_ID = ? AND comment_number <= ? AND is_deleted = false
                        ORDER BY comment_number DESC
                        LIMIT ?
                        """;
            } else {
                sql = """
                        SELECT *
                        FROM Comment
                        WHERE post_ID = ? AND comment_number >= ? AND is_deleted = false
                        ORDER BY comment_number ASC
                        LIMIT ?
                        """;
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, postID);
            ps.setInt(2, commentIDStart);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comments.add(new Comment(
                        rs.getInt("post_ID"),
                        rs.getInt("comment_number"),
                        rs.getString("user_ID"),
                        rs.getString("content"),
                        rs.getString("creation_date"),
                        rs.getString("last_modified"),
                        rs.getInt("upvotes"),
                        rs.getInt("downvotes"),
                        rs.getBoolean("is_deleted")));
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Retrieves the next <code>count</code> displayable posts starting from
     * <code>postIDStart</code>
     * into <code>posts</code>.
     *
     * @param posts
     * @param postIDStart
     * @param count
     * @throws DoesNotExistException
     */
    public static void retrieve(ArrayList<Post> posts, int postIDStart, int count, boolean reverse) {
        try {
            String sql;
            if (reverse) {
                sql = """
                        SELECT *
                        FROM Post
                        WHERE post_ID <= ? AND is_deleted = false
                        ORDER BY post_ID DESC
                        LIMIT ?
                        """;
            } else {
                sql = """
                        SELECT *
                        FROM Post
                        WHERE post_ID >= ? AND is_deleted = false
                        ORDER BY post_ID ASC
                        LIMIT ?
                        """;
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, postIDStart);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("post_ID"),
                        rs.getString("user_ID"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("creation_date"),
                        rs.getString("last_modified"),
                        rs.getInt("upvotes"),
                        rs.getInt("downvotes"),
                        rs.getBoolean("is_deleted"),
                        rs.getInt("views"),
                        rs.getBoolean("allow_comments"),
                        rs.getString("thumbnail_url")));
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * @param comment
     */
    public static void save(Comment comment) {
        try {
            int commentID = comment.getCommentID();
            String sql = """
                    SELECT COALESCE(MAX(comment_number), 0) AS max
                    FROM Comment
                    WHERE post_ID = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, comment.getPostID());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int maxID = rs.getInt("max");
            if (maxID != 0 && commentID > maxID) {
                throw new Error("Invalid comment ID.");
            }
            if (commentID != 0) {
                int id = commentID;
                sql = """
                        UPDATE Comment
                        SET user_ID = ?, content = ?, creation_date = ?, last_modified = ?, upvotes = ?, downvotes = ?, is_deleted = ?
                        WHERE post_ID = ? AND comment_number = ?
                        """;
                ps = connection.prepareStatement(sql);
                ps.setString(1, comment.getAuthorID());
                ps.setString(2, comment.getContent());
                ps.setString(3, comment.getCreationDate());
                ps.setString(4, comment.getLastModified());
                ps.setInt(5, comment.getUpvotes());
                ps.setInt(6, comment.getDownvotes());
                ps.setBoolean(7, comment.isDeleted());
                ps.setInt(8, comment.getPostID());
                ps.setInt(9, id);
                ps.executeUpdate();
            } else {
                int id = maxID + 1;
                sql = "INSERT INTO Comment VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                ps = connection.prepareStatement(sql);
                ps.setInt(1, comment.getPostID());
                ps.setInt(2, id);
                ps.setString(3, comment.getAuthorID());
                ps.setString(4, comment.getContent());
                ps.setString(5, comment.getCreationDate());
                ps.setString(6, comment.getLastModified());
                ps.setInt(7, comment.getUpvotes());
                ps.setInt(8, comment.getDownvotes());
                ps.setBoolean(9, comment.isDeleted());
                ps.executeUpdate();
                comment.setCommentID(id);
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * @param post
     */
    public static void save(Post post) {
        try {
            int postID = post.getPostID();
            String sql = "SELECT COALESCE(MAX(post_id), 0) AS max FROM Post";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int maxID = rs.getInt("max");
            if (maxID != 0 && postID > maxID) {
                throw new Error("Invalid post ID.");
            }
            if (postID != 0) {
                int id = postID;
                sql = """
                        UPDATE Post
                        SET user_ID = ?, title = ?, content = ?, creation_date = ?, last_modified = ?, upvotes = ?, downvotes = ?, views = ?, is_deleted = ?, allow_comments = ?, thumbnail_url = ?
                        WHERE post_ID = ?
                        """;
                ps = connection.prepareStatement(sql);
                ps.setString(1, post.getAuthorID());
                ps.setString(2, post.getTitle());
                ps.setString(3, post.getContent());
                ps.setString(4, post.getCreationDate());
                ps.setString(5, post.getLastModified());
                ps.setInt(6, post.getUpvotes());
                ps.setInt(7, post.getDownvotes());
                ps.setInt(8, post.getViews());
                ps.setBoolean(9, post.isDeleted());
                ps.setBoolean(10, post.isAllowComments());
                ps.setString(11, post.getThumbnailURL());
                ps.setInt(12, id);
                ps.executeUpdate();
            } else {
                int id = maxID + 1;
                sql = "INSERT INTO Post VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                ps = connection.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, post.getAuthorID());
                ps.setString(3, post.getTitle());
                ps.setString(4, post.getContent());
                ps.setString(5, post.getCreationDate());
                ps.setString(6, post.getLastModified());
                ps.setInt(7, post.getUpvotes());
                ps.setInt(8, post.getDownvotes());
                ps.setInt(9, post.getViews());
                ps.setBoolean(10, post.isDeleted());
                ps.setBoolean(11, post.isAllowComments());
                ps.setString(12, post.getThumbnailURL());
                ps.executeUpdate();
                post.setPostID(id);
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Saves the given <code>User</code> object into the database.
     *
     * @param user The <code>User</code> object to save. Contains the
     *             <code>userID</code>.
     */
    public static void save(User user) {
        try {
            int level = 1;
            if (user.getUserLevel() == UserLevel.CONTRIBUTOR) {
                level = 2;
            } else if (user.getUserLevel() == UserLevel.ADMIN) {
                level = 3;
            }
            String sql = """
                    SELECT COUNT(*)
                    FROM User
                    WHERE user_ID = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUserID());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt("COUNT(*)");
            if (count == 1) {
                sql = """
                        UPDATE User
                        SET username = ?, creation_date = ?, last_active = ?, profile_picture = ?, bio = ?, user_level = ?, is_deleted = ?
                        WHERE user_ID = ?
                        """;
                ps = connection.prepareStatement(sql);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCreationDate());
                ps.setString(3, user.getLastLogin());
                if (user.getProfilePicture() != null) {
                    ps.setString(4, user.getProfilePicture());
                } else {
                    ps.setNull(4, java.sql.Types.VARCHAR);
                }
                if (user.getBio() != null) {
                    ps.setString(5, user.getBio());
                } else {
                    ps.setNull(5, java.sql.Types.VARCHAR);
                }
                ps.setInt(6, level);
                ps.setBoolean(7, user.isDeleted());
                ps.setString(8, user.getUserID());
                ps.executeUpdate();
            } else {
                sql = "INSERT INTO User VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, user.getUserID());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getCreationDate());
                ps.setString(4, user.getLastLogin());
                if (user.getProfilePicture() != null) {
                    ps.setString(5, user.getProfilePicture());
                } else {
                    ps.setNull(5, java.sql.Types.VARCHAR);
                }
                if (user.getBio() != null) {
                    ps.setString(6, user.getBio());
                } else {
                    ps.setNull(6, java.sql.Types.VARCHAR);
                }
                ps.setInt(7, level);
                ps.setBoolean(8, user.isDeleted());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * TODO
     *
     * @param comment
     */
    public static void delete(Comment comment) {
        try {
            String sql = """
                    UPDATE Comment
                    SET is_deleted = true
                    WHERE post_ID = ? AND comment_number = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, comment.getPostID());
            ps.setInt(2, comment.getCommentID());
            ps.executeUpdate();
            comment.setDeleted(true);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * @param post
     */
    public static void delete(Post post) {
        try {
            String sql = """
                    UPDATE Post
                    SET is_deleted = true
                    WHERE post_ID = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, post.getPostID());
            ps.executeUpdate();
            post.setDeleted(true);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Saves the given <code>User</code> object into the database.
     *
     * @param user The <code>User</code> object to save. Contains the
     *             <code>userID</code>.
     */

    public static void delete(User user) {
        try {
            String sql = """
                    UPDATE User
                    SET is_deleted = true
                    WHERE user_ID = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUserID());
            ps.executeUpdate();
            user.setDeleted(true);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * @param userID The user trying to upvote.
     * @param postID The post to upvote.
     * @throws BlogException If the user already upvoted this post.
     */
    public static void upvote(String userID, int postID) throws BlogException {
        try {
            String sql = """
                    SELECT COUNT(*)
                    FROM Vote_Post
                    WHERE post_ID = ? AND user_ID = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, postID);
            ps.setString(2, userID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt("COUNT(*)");
            if (count == 0) {
                sql = """
                        INSERT INTO Vote_Post
                        VALUES(?, ?, true)
                        """;
                ps = connection.prepareStatement(sql);
                ps.setInt(1, postID);
                ps.setString(2, userID);
                ps.executeUpdate();

                Post post = Post.retrieve(postID);
                post.setUpvotes(post.getUpvotes() + 1);
                save(post);
            } else {
                sql = """
                        SELECT is_upvoted
                        FROM Vote_Post
                        WHERE post_ID = ? AND user_ID = ?
                        """;
                ps = connection.prepareStatement(sql);
                ps.setInt(1, postID);
                ps.setString(2, userID);
                rs = ps.executeQuery();
                rs.next();
                if (Boolean.TRUE.equals(rs.getBoolean("is_upvoted"))) {
                    throw new BlogException("User already upvoted this post.");
                } else {
                    sql = """
                            UPDATE Vote_Post
                            SET is_upvoted = true
                            WHERE post_ID = ? AND user_ID = ?
                            """;
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, postID);
                    ps.setString(2, userID);
                    ps.executeUpdate();

                    Post post = Post.retrieve(postID);
                    post.setDownvotes(post.getDownvotes() - 1);
                    post.setUpvotes(post.getUpvotes() + 1);
                    save(post);
                }
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * @param userID The user trying to downvote.
     * @param postID The post to downvote.
     * @throws BlogException If the user already downvoted this post.
     */
    public static void downvote(String userID, int postID) throws BlogException {
        try {
            String sql = """
                    SELECT COUNT(*)
                    FROM Vote_Post
                    WHERE post_ID = ? AND user_ID = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, postID);
            ps.setString(2, userID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt("COUNT(*)");
            if (count == 0) {
                sql = """
                        INSERT INTO Vote_Post
                        VALUES(?, ?, false)
                        """;
                ps = connection.prepareStatement(sql);
                ps.setInt(1, postID);
                ps.setString(2, userID);
                ps.executeUpdate();

                Post post = Post.retrieve(postID);
                post.setDownvotes(post.getDownvotes() + 1);
                save(post);
            } else {
                sql = """
                        SELECT is_upvoted
                        FROM Vote_Post
                        WHERE post_ID = ? AND user_ID = ?
                        """;
                ps = connection.prepareStatement(sql);
                ps.setInt(1, postID);
                ps.setString(2, userID);
                rs = ps.executeQuery();
                rs.next();
                if (Boolean.TRUE.equals(rs.getBoolean("is_upvoted"))) {
                    sql = """
                            UPDATE Vote_Post
                            SET is_upvoted = false
                            WHERE post_ID = ? AND user_ID = ?
                            """;
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, postID);
                    ps.setString(2, userID);
                    ps.executeUpdate();

                    Post post = Post.retrieve(postID);
                    post.setUpvotes(post.getUpvotes() - 1);
                    post.setDownvotes(post.getDownvotes() + 1);
                    save(post);
                } else {
                    throw new BlogException("User already downvoted this post.");
                }
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Increments the view counter of the given post.
     *
     * @param postID The post.
     * @throws DoesNotExistException
     */
    public static void view(int postID) throws DoesNotExistException {
        try {
            String sql = "SELECT views FROM Post WHERE post_ID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new DoesNotExistException("Post does not exist.");
            }
            int view = rs.getInt("views");
            view++;
            sql = "UPDATE Post SET views = ? WHERE post_ID = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, view);
            ps.setInt(2, postID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * @param userID    The user trying to upvote.
     * @param postID    The post to upvote.
     * @param commentID The post to upvote.
     * @throws BlogException If the user already upvoted this post.
     */
    public static void upvote(String userID, int postID, int commentID) throws BlogException {
        /*
         * will also need new table with key (userID, postID, commentID)
         * also need column for whether they upvoted or downvoted (binary column)
         *
         * similar behaviour as upvote post but for comments
         */
        if (jdbcTemplate == null) {
            createTemplate();
        }
        String sql = "SELECT COUNT(*) FROM Vote_Comment WHERE user_ID = \"" + userID + "\" AND post_ID = " + postID
                + " AND comment_number = " + commentID;
        if (jdbcTemplate.queryForObject(sql, Integer.class) == 0) {
            sql = "INSERT INTO Vote_Comment VALUES(" + postID + ", " + commentID + ", \"" + userID + "\", true)";
            jdbcTemplate.update(sql);
            Comment comment = Comment.retrieve(postID, commentID);
            comment.setUpvotes(comment.getUpvotes() + 1);
            save(comment);
        } else {
            sql = "SELECT is_upvoted FROM Vote_Comment WHERE user_ID = \"" + userID + "\" AND post_ID = " + postID
                    + " AND comment_number = " + commentID;
            if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class))) {
                throw new BlogException("User already upvoted this comment.");
            } else {
                sql = "UPDATE Vote_Comment SET is_upvoted = true WHERE user_ID = \"" + userID + "\" AND post_ID = "
                        + postID + " AND comment_number = " + commentID;
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
        /*
         * should same table as upvote
         *
         * similar behaviour as upvote but flipped
         */
        if (jdbcTemplate == null) {
            createTemplate();
        }
        String sql = "SELECT COUNT(*) FROM Vote_Comment WHERE user_ID = \"" + userID + "\" AND post_ID = " + postID
                + " AND comment_number = " + commentID;
        if (jdbcTemplate.queryForObject(sql, Integer.class) == 0) {
            sql = "INSERT INTO Vote_Comment VALUES(" + postID + ", " + commentID + ", \"" + userID + "\", false)";
            jdbcTemplate.update(sql);
            Comment comment = Comment.retrieve(postID, commentID);
            comment.setDownvotes(comment.getDownvotes() + 1);
            save(comment);
        } else {
            sql = "SELECT is_upvoted FROM Vote_Comment WHERE user_ID = \"" + userID + "\" AND post_ID = " + postID
                    + " AND comment_number = " + commentID;
            if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class))) {
                sql = "UPDATE Vote_Comment SET is_upvoted = false WHERE user_ID = \"" + userID + "\" AND post_ID = "
                        + postID + " AND comment_number = " + commentID;
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
     * @throws DoesNotExistException If the user does not exist.
     */
    public static void save(PromotionRequest request) throws DoesNotExistException {
        try {
            int level = 1;
            if (request.getTarget() == UserLevel.CONTRIBUTOR) {
                level = 2;
            } else if (request.getTarget() == UserLevel.ADMIN) {
                level = 3;
            }
            String sql = """
                    SELECT COUNT(*)
                    FROM Promotion_Request
                    WHERE user_ID = ? AND is_deleted = false
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, request.getUserID());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt("COUNT(*)");
            if (count == 0) {
                sql = "SELECT COALESCE(MAX(request_ID), 0) AS max FROM Promotion_Request";
                ps = connection.prepareStatement(sql);
                rs = ps.executeQuery();
                rs.next();
                int id = rs.getInt("max") + 1;
                request.setRequestID(id);

                sql = "INSERT INTO Promotion_Request VALUES(?, ?, ?, ?, ?, ?)";
                ps = connection.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, request.getUserID());
                ps.setInt(3, level);
                ps.setString(4, request.getRequestTime());
                ps.setString(5, request.getReason());
                ps.setBoolean(6, request.isDeleted());
            } else {
                sql = """
                        SELECT request_ID
                        FROM Promotion_Request
                        WHERE user_ID = ? AND is_deleted = false
                        """;
                ps = connection.prepareStatement(sql);
                rs = ps.executeQuery();
                rs.next();
                int id = rs.getInt("request_ID");

                sql = """
                        UPDATE Promotion_Request
                        SET user_ID = ?, target_level = ?, request_time = ?, reason = ?, is_deleted = ?
                        WHERE request_ID = ?
                        """;
                ps.setString(1, request.getUserID());
                ps.setInt(2, level);
                ps.setString(3, request.getRequestTime());
                ps.setString(4, request.getReason());
                ps.setBoolean(5, request.isDeleted());
                ps.setInt(6, id);
                ps = connection.prepareStatement(sql);
                rs = ps.executeQuery();
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Retrieves the user record from the database with corresponding
     * <code>userID</code> specified
     * by the given <code>User</code> object and updates its fields.
     *
     * @param request The <code>PromotionRequest</code> object to update. Contains
     *                the <code>requestID</code>.
     * @throws DoesNotExistException
     * @throws IsDeletedException
     */
    public static void retrieve(PromotionRequest request) throws DoesNotExistException, IsDeletedException {

        try {
            String sql = """
                    SELECT *
                    FROM Promotion_Request
                    WHERE request_ID = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, request.getRequestID());

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new DoesNotExistException(
                        "Promotion request with ID " + request.getRequestID() + " does not exist.");
            }
            request.setUserID(rs.getString("user_ID"));
            request.setRequestTime(rs.getString("request_time"));
            if (rs.getInt("target_level") == 1) {
                request.setTarget(UserLevel.READER);
            } else if (rs.getInt("target_level") == 2) {
                request.setTarget(UserLevel.CONTRIBUTOR);
            } else if (rs.getInt("target_level") == 3) {
                request.setTarget(UserLevel.ADMIN);
            }
            request.setReason(rs.getString("reason"));
            request.setDeleted(rs.getBoolean("is_deleted"));

            if (Boolean.TRUE.equals(request.isDeleted())) {
                throw new IsDeletedException("Promotion request with ID " + request.getRequestID() + " is deleted.");
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Retrieves the next <code>count</code> displayable requests starting from
     * <code>requestIDStart</code>
     * into <code>requests</code>.
     *
     * @param requests
     * @param requestIDStart
     * @param count
     * @param reverse
     */
    public static void retrievePromotionRequests(ArrayList<PromotionRequest> requests, int requestIDStart, int count,
            boolean reverse) {
        try {
            String sql;
            if (reverse) {
                sql = """
                        SELECT *
                        FROM Promotion_Request
                        WHERE request_ID <= ? AND is_deleted = false
                        ORDER BY request_ID DESC
                        LIMIT ?
                        """;
            } else {
                sql = """
                        SELECT *
                        FROM Promotion_Request
                        WHERE request_ID >= ? AND is_deleted = false
                        ORDER BY request_ID ASC
                        LIMIT ?
                        """;
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, requestIDStart);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PromotionRequest request = new PromotionRequest(rs.getInt("request_ID"));
                request.setUserID(rs.getString("user_ID"));
                request.setRequestTime(rs.getString("request_time"));
                if (rs.getInt("target_level") == 1) {
                    request.setTarget(UserLevel.READER);
                } else if (rs.getInt("target_level") == 2) {
                    request.setTarget(UserLevel.CONTRIBUTOR);
                } else if (rs.getInt("target_level") == 3) {
                    request.setTarget(UserLevel.ADMIN);
                }
                request.setReason(rs.getString("reason"));
                request.setDeleted(rs.getBoolean("is_deleted"));
                requests.add(request);
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * @param request
     */
    public static void delete(PromotionRequest request) {
        try {
            String sql = """
                    UPDATE Promotion_Request
                    SET is_deleted = true
                    WHERE request_ID = ?
                    """;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, request.getRequestID());
            ps.executeUpdate();
            request.setDeleted(true);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
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
         * Change user level of userID to the target.
         * throw new DoesNotExistException("User does not exist.") if userID not in User
         * table
         * After changing user level, hard delete all promotion requests that
         * this user has that is not higher than their current new level.
         */
        try {
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
        String sql = "DELETE FROM Promotion_Request WHERE user_ID = \"" + userID + "\" AND target_level <= " + level;
        jdbcTemplate.update(sql);
    }

    /**
     * @param posts   The array that stores the posts.
     * @param pattern The string to search for.
     * @param start   The row to start returning after sorted.
     * @param count   The number of requested posts.
     * @param sortBy  The method of sorting:
     *                "new": newest post first
     *                "old": oldest post first
     *                "top": top-rated post first (upvote - downvote)
     *                "view": top-viewed post first
     * @throws BlogException
     */
    public static void search(ArrayList<Post> posts, String pattern, int start, int count, String sortBy)
            throws BlogException {
        try {
            String sql = switch (sortBy.toLowerCase()) {
                case "new" -> """
                        SELECT *
                        FROM Post
                        WHERE (title LIKE ? OR content LIKE ?) AND is_deleted = false
                        ORDER BY creation_date DESC
                        LIMIT ?, ?
                        """;
                case "old" -> """
                        SELECT *
                        FROM Post
                        WHERE (title LIKE ? OR content LIKE ?) AND is_deleted = false
                        ORDER BY creation_date ASC
                        LIMIT ?, ?
                        """;
                case "top" -> """
                        SELECT *, upvotes - downvotes as top
                        FROM Post
                        WHERE (title LIKE ? OR content LIKE ?) AND is_deleted = false
                        ORDER BY top DESC
                        LIMIT ?, ?
                        """;
                case "view" -> """
                        SELECT *
                        FROM Post
                        WHERE (title LIKE ? OR content LIKE ?) AND is_deleted = false
                        ORDER BY views DESC
                        LIMIT ?, ?
                        """;
                default -> throw new BlogException("Unexpected sortBy string.");
            };
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + pattern + "%");
            ps.setString(2, "%" + pattern + "%");
            ps.setInt(3, start - 1);
            ps.setInt(4, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("post_ID"),
                        rs.getString("user_ID"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("creation_date"),
                        rs.getString("last_modified"),
                        rs.getInt("upvotes"),
                        rs.getInt("downvotes"),
                        rs.getBoolean("is_deleted"),
                        rs.getInt("views"),
                        rs.getBoolean("allow_comments"),
                        rs.getString("thumbnail_url")));
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Returns the highest postID currently in the database.
     *
     * @return The highest postID.
     */
    public static int highestPostID() {
        String sql = "SELECT MAX(post_id) FROM Post";
        if (jdbcTemplate == null) {
            createTemplate();
        }
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
