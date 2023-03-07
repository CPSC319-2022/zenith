package com.blog.exception;

public class BlogException extends Exception {
    public BlogException(String e) {
        super(e);
    }

    @Override
    public String getMessage() {
        return "[[BLOG EXCEPTION]] \n" + super.getMessage();
    }
}
