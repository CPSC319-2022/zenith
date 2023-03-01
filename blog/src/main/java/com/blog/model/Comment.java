package com.blog.model;

import com.blog.database.Database;

import java.time.Clock;

public class Comment {
    private final int postID;
    private final int commentID;
    private int authorID;
    private String content;
    private Clock creationDate;
    private Clock lastModified;
    // private int upvotes; TODO: allow upvoting/downvoting?

    public Comment(int postID, int commentID) {
        this.postID = postID;
        this.commentID = commentID;
        Database.retrieveComment(this);
    }

    public int getPostID() {
        return postID;
    }

    public int getCommentID() {
        return commentID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Clock getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Clock creationDate) {
        this.creationDate = creationDate;
    }

    public Clock getLastModified() {
        return lastModified;
    }

    public void setLastModified(Clock lastModified) {
        this.lastModified = lastModified;
    }
}
