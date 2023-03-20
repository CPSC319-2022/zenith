package com.blog.model;

import com.blog.database.Database;
import com.blog.exception.DoesNotExistException;
import org.json.JSONObject;

/**
 * Class that stores the details of a comment.
 * <p>
 * Constructors
 * ----------
 * Comment(int postID, int commentID)
 * <p>
 * Methods
 * ----------
 * int  getPostID()
 * int  getCommentID()
 * <p>
 * Inherited Methods
 * ----------
 * int     getAuthorID()
 * void    setAuthorID(int authorID)
 * String  getContent()
 * void    setContent(String content)
 * Clock   getCreationDate()
 * void    setCreationDate(Clock creationDate)
 * Clock   getLastModified()
 * void    setLastModified(Clock lastModified)
 * int     getUpvotes()
 * void    setUpvotes(int upvotes)
 * int     getDownvotes()
 * void    setDownvotes(int downvotes)
 */
public class Comment extends Content {
    public static final int NEW_COMMENT_ID = 0;

    private final int postID;
    private int commentID;

    public Comment(int postID, int commentID) {
        this.postID = postID;
        this.commentID = commentID;
    }

    public Comment(int postID,
                   int commentID,
                   String authorID,
                   String content,
                   String creationDate,
                   String lastModified,
                   int upvotes,
                   int downvotes,
                   boolean isDeleted) {
        super(authorID, content, creationDate, lastModified, upvotes, downvotes, isDeleted);
        this.postID = postID;
        this.commentID = commentID;
    }

    /**
     * Factory method to retrieve the comment with the given postID and commentID.
     *
     * @param postID    The post to retrieve the comment from.
     * @param commentID The comment to retrieve.
     * @return The comment with the given postID and commentID.
     */
    public static Comment retrieve(int postID, int commentID) throws DoesNotExistException {
        Comment comment = new Comment(postID, commentID);
        Database.retrieve(comment);
        return comment;
    }

    /**
     * Returns the JSON representation of this object.
     *
     * @return JSONObject
     */
    public JSONObject asJSONObject() {
        return super.asJSONObject()
                .put("postID", postID)
                .put("commentID", commentID);
    }

    /**
     * Returns the JSON string of this object
     *
     * @return String
     */
    public String asJSONString() {
        return asJSONObject().toString();
    }

    public void copy(Comment c) {
        this.setAuthorID(c.getAuthorID());
        this.setContent(c.getContent());
        this.setCreationDate(c.getCreationDate());
        this.setLastModified(c.getLastModified());
        this.setUpvotes(c.getUpvotes());
        this.setDownvotes(c.getDownvotes());
        this.setDeleted(c.isDeleted());
    }

    public int getPostID() {
        return postID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }


}
