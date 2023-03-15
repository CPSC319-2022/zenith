package com.blog.exception;

public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException(String e) {
        super(e);
    }

    @Override
    public String getMessage() {
        return "[[BLOG EXCEPTION]] \n" + super.getMessage();
    }
}
