package com.blog.model;

import com.blog.utils.Utility;
import org.json.JSONObject;

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
 *
 * Inherited Methods
 * ----------
 * boolean  isDeleted()
 * void     setDeleted(boolean deleted)
 */
abstract class Content extends Record {
    private int authorID;
    private String content;
    private String creationDate;
    private String lastModified;
    private int upvotes;
    private int downvotes;

    public Content() {
    }

    public Content(int     authorID,
                   String  content,
                   String  creationDate,
                   String  lastModified,
                   int     upvotes,
                   int     downvotes,
                   boolean isDeleted) {
        super(isDeleted);
        this.authorID     = authorID;
        this.content      = content;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.upvotes      = upvotes;
        this.downvotes    = downvotes;
    }

    /**
     * Returns the JSON representation of this object.
     *
     * @return JSONObject
     */
    public JSONObject asJSONObject() {
        JSONObject json = super.asJSONObject();

        json.put("authorID", authorID);
        json.put("content", content);
        json.put("creationDate", creationDate);
        json.put("lastModified", lastModified);
        json.put("upvotes", upvotes);
        json.put("downvotes", downvotes);

        return json;
    }

    /**
     * Returns whether this content is displayable. That is, it has content and is not deleted.
     *
     * @return boolean
     */
    public boolean isDisplayable() {
        return content != null && !isDeleted();
    }

    /**
     * Updates the last modified time to the current time.
     */
    public void lastModifiedNow() {
        lastModified = Utility.getCurrentTime();
    }

    /**
     * Increments the upvote counter.
     */
    public void upvote() {
        upvotes++;
    }

    /**
     * Increments the downvote counter.
     */
    public void downvote() {
        downvotes++;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
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
}
