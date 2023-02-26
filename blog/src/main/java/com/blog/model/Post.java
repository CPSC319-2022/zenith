package com.blog.model;

import java.time.Clock;
import java.util.Objects;

public class Post {
    private final int postID;
    private final int authorID;
    private String title;
    private String content;
    private final Clock creationDate;
    private Clock lastModified;
    private int views;
    private boolean allowComments;
    // private Tag tags; TODO: allow tags for posts
    // private int upvotes; TODO: allow upvoting/downvoting?

    public Post(int postID,
                int authorID,
                String title,
                String content,
                Clock creationDate,
                Clock lastModified,
                int views,
                boolean allowComments) {
        this.postID = postID;
        this.authorID = authorID;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.views = views;
        this.allowComments = allowComments;
    }

    public int getPostID() {
        return postID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isAllowComments() {
        return allowComments;
    }

    public void setAllowComment(boolean allowComments) {
        this.allowComments = allowComments;
    }
}
