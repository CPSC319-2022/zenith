package com.blog.exception;

public class InvalidPermissionException extends BlogException {
    public InvalidPermissionException() {
    }

    public InvalidPermissionException(String e) {
        super(e);
    }
}
