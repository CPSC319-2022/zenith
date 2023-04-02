package com.blog.model;

import com.blog.database.Database;
import com.blog.exception.BlogException;
import com.blog.exception.DoesNotExistException;
import org.json.JSONObject;

/**
 * Class that stores the details of a blog post.
 */
public class Post extends Content {
    public static final int NEW_POST_ID = 0;
    private static final int MIN_TITLE_LENGTH = 1;
    private static final String DEFAULT_IMAGE_URL = "https://storage.googleapis.com/zenith-blog-thumbnailurl/default_thumbnail.png";

    private int postID;
    private String title;
    private int views;
    private boolean allowComments;
    private String thumbnailURL;

    public Post(int postID) {
        this.postID = postID;
    }

    public Post(int postID,
                String authorID,
                String title,
                String content,
                String creationDate,
                String lastModified,
                int upvotes,
                int downvotes,
                boolean isDeleted,
                int views,
                boolean allowComments,
                String thumbnailURL) {
        super(authorID, content, creationDate, lastModified, upvotes, downvotes, isDeleted);
        this.postID = postID;
        this.title = title;
        this.views = views;
        this.allowComments = allowComments;
        if (thumbnailURL.isEmpty()) {
            this.thumbnailURL = DEFAULT_IMAGE_URL;
        } else {
            this.thumbnailURL = thumbnailURL;
        }
    }

    /**
     * Factory method to retrieve the post with the given postID.
     *
     * @param postID The post to retrieve.
     * @return The post with the given postID.
     */
    public static Post retrieve(int postID) throws DoesNotExistException {
        Post post = new Post(postID);
        Database.retrieve(post);
        return post;
    }

    /**
     * Validates the given title.
     *
     * @param title The title to validate.
     * @throws BlogException If the title is invalid.
     */
    public static void validateTitle(String title) throws BlogException {
        if (title.length() < MIN_TITLE_LENGTH) {
            throw new BlogException("Title is too short.");
        }
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
                .put("allowComments", allowComments)
                .put("thumbnailURL", thumbnailURL);
    }

    /**
     * Returns the JSON string of this object
     *
     * @return String
     */
    public String asJSONString() {
        return asJSONObject().toString();
    }

    /**
     * Increments the view counter.
     */
    public void view() {
        views++;
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

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        if (thumbnailURL.isEmpty()) {
            this.thumbnailURL = DEFAULT_IMAGE_URL;
        } else {
            this.thumbnailURL = thumbnailURL;
        }
    }
}
