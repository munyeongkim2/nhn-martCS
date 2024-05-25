package com.nhnacademy.nhnmart.exception;

import jakarta.validation.constraints.Size;

public class ValidationAskException extends RuntimeException {
    public ValidationAskException() {
        super("제목은 2~200자, 내용은 40000만자 까지 쓸 수 있습니다.");
    }
}
