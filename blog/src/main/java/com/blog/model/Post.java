package com.blog.model;

import com.blog.database.Database;
import com.blog.exception.BlogException;
import org.json.JSONObject;

/**
 * Class that stores the details of a blog post.
 * <p>
 * Constructors
 * ----------
 * Post(int postID)
 * <p>
 * Methods
 * ----------
 * int      getPostID()
 * String   getTitle()
 * void     setTitle(String title)
 * int      getViews()
 * void     setViews(int views)
 * boolean  isAllowComments()
 * void     setAllowComments(boolean allowComments)
 * <p>
 * Inherited Methods
 * ----------
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
public class Post extends Content {
    // Delete final for convenience to retrieve data, may change later
    public static final int NEW_POST_ID = 0;
    static final int MIN_TITLE_LENGTH = 1;

    private int postID;
    private String title;
    private int views;
    private boolean allowComments;
    // private Tag tags; TODO: allow tags for posts

    public Post(int postID) {
        this.postID = postID;
        Database.retrieve(this);
    }

    public Post(int postID,
                int authorID,
                String title,
                String content,
                String creationDate,
                String lastModified,
                int upvotes,
                int downvotes,
                boolean isDeleted,
                int views,
                boolean allowComments) {
        super(authorID, content, creationDate, lastModified, upvotes, downvotes, isDeleted);
        this.postID = postID;
        this.title = title;
        this.views = views;
        this.allowComments = allowComments;
    }

    /**
     * Returns the JSON representation of this object.
     *
     * @return JSONObject
     */
    public JSONObject asJSONObject() {
        return super.asJSONObject()
                .put("postID", postID)
                .put("title", title)
                .put("views", views)
                .put("allowComments", allowComments);
    }

    /**
     * Validates the length of the title field.
     *
     * @param title The title to validate.
     * @throws BlogException
     */
    public static void validateTitle(String title) throws BlogException {
        if (title.length() < MIN_TITLE_LENGTH) {
            throw new BlogException("Title length is too short.");
        }
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isAllowComments() {
        return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }
}
