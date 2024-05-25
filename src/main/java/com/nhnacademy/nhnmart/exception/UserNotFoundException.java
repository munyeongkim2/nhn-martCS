package com.nhnacademy.nhnmart.exception;

public class UserNotFoundException  extends RuntimeException {
    public UserNotFoundException() {
        super("user not found");
    }
}
