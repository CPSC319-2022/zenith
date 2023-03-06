package com.blog.model;

import com.blog.database.Database;

import java.time.Clock;
import java.util.Objects;

/**
 * Class that stores the details of a blog post.
 *
 * Constructors
 * ----------
 * Post(int postID)
 *
 * Methods
 * ----------
 * int      getPostID()
 * String   getTitle()
 * void     setTitle(String title)
 * int      getViews()
 * void     setViews(int views)
 * boolean  isAllowComments()
 * void     setAllowComments(boolean allowComments)
 *
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
    private final int postID;
    private String title;
    private int views;
    private boolean allowComments;
    // private Tag tags; TODO: allow tags for posts

    public Post(int postID) {
        this.postID = postID;
        Database.retrieve(this);
    }

    public Post(int     postID,
                int     authorID,
                String  title,
                String  content,
                Clock   creationDate,
                Clock   lastModified,
                int     upvotes,
                int     downvotes,
                boolean isDeleted,
                int     views,
                boolean allowComments) {
        super(authorID, content, creationDate, lastModified, upvotes, downvotes, isDeleted);
        this.postID = postID;
        this.title = title;
        this.views = views;
        this.allowComments = allowComments;
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
