package com.blog.model;

import com.blog.database.Database;

import java.time.Clock;

/**
 * Class that stores the details of a comment.
 *
 * Constructors
 * ----------
 * Comment(int postID, int commentID)
 *
 * Methods
 * ----------
 * int  getPostID()
 * int  getCommentID()
 *
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
    // Delete final for convenience to retrieve data, may change later
    private int postID;
    private int commentID;

    public Comment(int postID, int commentID) {
        this.postID = postID;
        this.commentID = commentID;
        Database.retrieve(this);
    }

    public Comment(int     postID,
                   int     commentID,
                   int     authorID,
                   String  content,
                   Clock   creationDate,
                   Clock   lastModified,
                   int     upvotes,
                   int     downvotes,
                   boolean isDeleted) {
        super(authorID, content, creationDate, lastModified, upvotes, downvotes, isDeleted);
        this.postID = postID;
        this.commentID = commentID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }


}
