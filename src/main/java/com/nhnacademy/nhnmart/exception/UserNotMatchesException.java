package com.nhnacademy.nhnmart.exception;

public class UserNotMatchesException extends RuntimeException {
    public UserNotMatchesException() {
        super("login failed");
    }
}
