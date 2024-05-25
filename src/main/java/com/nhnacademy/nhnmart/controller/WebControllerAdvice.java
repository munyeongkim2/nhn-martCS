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

@Slf4j
@ControllerAdvice()
public class WebControllerAdvice{

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.error("Exception occurred at request URI: {}", request.getRequestURI(), ex);

        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND);


        return "error";
    }




}
