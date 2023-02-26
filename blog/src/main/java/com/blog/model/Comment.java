package com.blog.model;

import java.time.Clock;

public class Comment {
    private final int commentID;
    private final int authorID;
    private final int postID;
    private String content;
    private final Clock creationDate;
    private Clock lastModified;
    // private int upvotes; TODO: allow upvoting/downvoting?

    public Comment(int commentID,
                   int authorID,
                   int postID,
                   String content,
                   Clock creationDate,
                   Clock lastModified) {
        this.commentID = commentID;
        this.authorID = authorID;
        this.postID = postID;
        this.content = content;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
    }

    public int getCommentID() {
        return commentID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public int getPostID() {
        return postID;
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

    public Clock getLastModified() {
        return lastModified;
    }

    public void setLastModified(Clock lastModified) {
        this.lastModified = lastModified;
    }
}
