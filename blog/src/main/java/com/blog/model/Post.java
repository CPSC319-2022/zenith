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

    private final int postID;
    private String title;
    private int views;
    private boolean allowComments;
    // private Tag tags; TODO: allow tags for posts

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
                boolean allowComments) {
        super(authorID, content, creationDate, lastModified, upvotes, downvotes, isDeleted);
        this.postID = postID;
        this.title = title;
        this.views = views;
        this.allowComments = allowComments;
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
     * Returns the JSON string of this object
     *
     * @return String
     */
    public String asJSONString() {
        return asJSONObject().toString();
    }

    public void copy(Post p) {
        this.setAuthorID(p.getAuthorID());
        this.setTitle(p.getTitle());
        this.setContent(p.getContent());
        this.setCreationDate(p.getCreationDate());
        this.setLastModified(p.getLastModified());
        this.setUpvotes(p.getUpvotes());
        this.setDownvotes(p.getDownvotes());
        this.setDeleted(p.isDeleted());
        this.setAllowComments(p.isAllowComments());
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
