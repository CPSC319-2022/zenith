package com.blog.model;

import java.time.Clock;

public class Post {

    private Integer post_ID;

    private Integer author_ID;

    private Integer views;

    private String title;

    private String content;

    private Clock creationDate;

    private Clock lastModified;

    private Boolean allowComment;

    private Integer commentsNum;

    //todo
    //private Tag tags;

}
