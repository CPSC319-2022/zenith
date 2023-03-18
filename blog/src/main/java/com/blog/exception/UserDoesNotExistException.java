package com.blog.exception;

public class UserDoesNotExistException extends BlogException {
    public UserDoesNotExistException(String e) {
        super(e);
    }

}
