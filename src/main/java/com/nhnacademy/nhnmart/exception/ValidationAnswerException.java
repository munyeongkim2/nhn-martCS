package com.nhnacademy.nhnmart.exception;

public class ValidationAnswerException extends RuntimeException{
    public ValidationAnswerException() {
        super("내용은 최소 1자, 최대 40000만자 까지 쓸 수 있습니다.");
    }
}
