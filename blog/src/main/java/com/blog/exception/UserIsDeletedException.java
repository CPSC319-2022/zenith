package com.blog.exception;

public class UserIsDeletedException extends Exception {
    public UserIsDeletedException(String e) {
        super(e);
    }

    @Override
    public String getMessage() {
        return "[[BLOG EXCEPTION]] \n" + super.getMessage();
    }   
}
