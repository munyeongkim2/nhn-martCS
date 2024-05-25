package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.*;
import com.nhnacademy.nhnmart.exception.UserNotFoundException;
import com.nhnacademy.nhnmart.exception.ValidationAnswerException;
import com.nhnacademy.nhnmart.service.AskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.IOException;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
@Slf4j
@Controller
@RequestMapping("/cs/admin")
@AllArgsConstructor
public class AgentController {
    private final AskService askService;
//    @ModelAttribute("id")
//    public String addAttributes(HttpSession session) {
//        User user = (User) session.getAttribute("user");
//        String id = user.getId();
//        if (id == null || id.isEmpty()) {
//            throw new UserNotFoundException();
//        }
//        return id;
//    }

    @GetMapping
    public String dashboardView(Model model) {
        Map<String, List<Ask>> asks = askService.getAsksForAgent();
        model.addAttribute("asks", asks);
        return "agentDashboard";
    }

    @GetMapping("/answer/{customerId}/{title}")
    public String answerForm(@PathVariable("customerId") String customerId, @PathVariable("title") String title, Model model) {
        Ask ask = askService.getAsk(customerId, title);
        model.addAttribute("ask", ask);
        model.addAttribute("customerId", customerId);
        return "answerForm";
    }

    @PostMapping("/answerCreate")
    public String answerCreate(
            @RequestParam("customerId") String customerId,
            @Valid @ModelAttribute() AnswerFormRequest answerFormRequest,
            BindingResult bindingResult,
            HttpSession session) {
        if(bindingResult.hasErrors()) {
            throw new ValidationAnswerException();
        }

        User user = (User) session.getAttribute("user");
        String id = user.getId();
        askService.addAnswer(customerId, answerFormRequest.getTitle(), answerFormRequest.getContent(), id);
        return "redirect:/cs/admin";
    }


    @ExceptionHandler(ValidationAnswerException.class)
    public String validAnswerHandleException(Exception ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.error("Exception occurred at request URI: {}", request.getRequestURI(), ex);

        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND);


        return "error";
    }
}
