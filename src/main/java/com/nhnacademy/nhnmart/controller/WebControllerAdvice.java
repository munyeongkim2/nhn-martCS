package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.exception.ValidationAnswerException;
import com.nhnacademy.nhnmart.exception.ValidationAskException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@ControllerAdvice()
public class WebControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.error("Exception occurred at request URI: {}", request.getRequestURI(), ex);

        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND);

        return "error";
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(Model model, HttpServletRequest request, HttpServletResponse response) {
        String errorMessage =  "첨부 파일의 크기가 너무 큽니다.";
        log.error("Exception occurred at request URI: {}", request.getRequestURI(),errorMessage);

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("status", HttpStatus.NOT_FOUND);

        return "error";
    }
}

