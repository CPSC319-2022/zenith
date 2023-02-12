package com.blog.model;

import java.time.Clock;

public class Comment {
    private Integer commentID;

    private Integer authorID;

    private String title;

    private Clock creationDate;

    private boolean isParent;

    private Integer parentCommentID;
}
