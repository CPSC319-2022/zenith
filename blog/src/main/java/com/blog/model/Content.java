package com.blog.model;

import java.time.Clock;

/**
 * Abstract class Content to be extended by Post and Comment.
 *
 * Methods
 * ----------
 * boolean  isDisplayable()
 *
 * int      getAuthorID()
 * void     setAuthorID(int authorID)
 * String   getContent()
 * void     setContent(String content)
 * Clock    getCreationDate()
 * void     setCreationDate(Clock creationDate)
 * Clock    getLastModified()
 * void     setLastModified(Clock lastModified)
 * int      getUpvotes()
 * void     setUpvotes(int upvotes)
 * int      getDownvotes()
 * void     setDownvotes(int downvotes)
 * boolean  isDeleted()
 * void     setDeleted(boolean deleted)
 */
abstract class Content {
    private int authorID;
    private String content;
    private Clock creationDate;
    private Clock lastModified;
    private int upvotes;
    private int downvotes;
    private boolean isDeleted;

    public Content() {
    }

    public Content(int     authorID,
                   String  content,
                   Clock   creationDate,
                   Clock   lastModified,
                   int     upvotes,
                   int     downvotes,
                   boolean isDeleted) {
        this.authorID     = authorID;
        this.content      = content;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.upvotes      = upvotes;
        this.downvotes    = downvotes;
        this.isDeleted    = isDeleted;
    }

    /**
     * Returns whether this content is displayable. That is, it has content and is not deleted.
     *
     * @return boolean
     */
    public boolean isDisplayable() {
        return content != null && !isDeleted;
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

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
