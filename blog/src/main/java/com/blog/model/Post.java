package com.blog.model;

import com.blog.database.Database;

import java.time.Clock;
import java.util.Objects;

public class Post {
    private final int postID;
    private int authorID;
    private String title;
    private String content;
    private Clock creationDate;
    private Clock lastModified;
    private int views;
    private boolean allowComments;
    // private Tag tags; TODO: allow tags for posts
    // private int upvotes; TODO: allow upvoting/downvoting?

    public Post(int postID) {
        this.postID = postID;
        Database.retrievePost(this);
    }

    public int getPostID() {
        return postID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
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

    public void setCreationDate(Clock creationDate) {
        this.creationDate = creationDate;
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

    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }
}
