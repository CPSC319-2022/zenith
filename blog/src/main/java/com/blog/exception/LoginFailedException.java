package com.blog.exception;

public class LoginFailedException extends BlogException{
    public LoginFailedException() {
    }

    public LoginFailedException(String e) {
        super(e);
    }
}
