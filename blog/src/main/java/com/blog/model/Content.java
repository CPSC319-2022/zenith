package com.blog.model;

import com.blog.exception.BlogException;
import com.blog.utils.Utility;
import org.json.JSONObject;

/**
 * Abstract class Content to be extended by Post and Comment.
 */
public abstract class Content extends Record {
    private static final int MIN_CONTENT_LENGTH = 1;

    private String authorID;
    private String content;
    private String creationDate;
    private String lastModified;
    private int upvotes;
    private int downvotes;

    public Content() {
    }

    public Content(String authorID,
                   String content,
                   String creationDate,
                   String lastModified,
                   int upvotes,
                   int downvotes,
                   boolean isDeleted) {
        super(isDeleted);
        this.authorID = authorID;
        this.content = content;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    /**
     * Validates the given content.
     *
     * @param content The content to validate.
     * @throws BlogException If the content is invalid.
     */
    public static void validateContent(String content) throws BlogException {
        if (content.length() < MIN_CONTENT_LENGTH) {
            throw new BlogException("Content is too short.");
        }
    }

    /**
     * Returns the JSON representation of this object.
     *
     * @return JSONObject
     */
    public JSONObject asJSONObject() {
        return super.asJSONObject()
                .put("authorID", authorID)
                .put("content", content)
                .put("creationDate", creationDate)
                .put("lastModified", lastModified)
                .put("upvotes", upvotes)
                .put("downvotes", downvotes);
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
     * @param user The user to check authorship.
     * @return Whether the user is the author of this content.
     */
    public boolean isAuthoredBy(User user) {
        return authorID.equals(user.getUserID());
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

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
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
