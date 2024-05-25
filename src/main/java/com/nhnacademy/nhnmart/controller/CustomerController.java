package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.Ask;
import com.nhnacademy.nhnmart.domain.AskFormRequest;
import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.ValidationAskException;
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

@Controller
@RequestMapping("/cs")
@AllArgsConstructor
@Slf4j
public class CustomerController {
    private final AskService askService;

    @ModelAttribute("id")
    public String addAttributes(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user.getId();
    }

    @GetMapping
    public String dashboardView(@ModelAttribute("id") String id, Model model) { //requirdParam

        List<Ask> asks = askService.getAsksForCustomer(id);
        model.addAttribute("asks", asks);
        model.addAttribute("categoryList", askService.getCategory());
        return "customerDashboard";
    }

    @GetMapping("/detail/{title}")
    public String detailAsk(@ModelAttribute("id") String id, @PathVariable String title, Model model) {

        Ask ask = askService.getAsk(id, title);
        model.addAttribute("ask", ask);
        return "askDetails";
    }

    @GetMapping("/ask")
    public String createAskFrom(Model model) {
        model.addAttribute("categoryList", askService.getCategory());
        return "askForm";
    }

    @GetMapping("/search")
    public String dashBoardSearch(@ModelAttribute("id") String id, @RequestParam("category") String category, Model model) {
        if (category.equals("all")) {
            return "redirect:/cs";
        }
        List<Ask> asks = askService.getAskForSearch(id, category);
        model.addAttribute("asks", asks);
        model.addAttribute("category", category);
        model.addAttribute("categoryList", askService.getCategory());
        return "customerDashboard";
    }
    @PostMapping("/ask")
    public String createAsk(@ModelAttribute("id") String id,
                            @RequestParam("category") String category,
                            @RequestParam(value = "file", required = false) List<MultipartFile> files,
                            @Valid @ModelAttribute() AskFormRequest askFormRequest,
                            BindingResult bindingResult
                            ) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new ValidationAskException();
        }

        if (files.size() == 1 && files.getFirst().getOriginalFilename().equals("")) {
            askService.addAsk(id, askFormRequest.getTitle(), category, askFormRequest.getContent());
        } else {
            askService.addAsk(id, askFormRequest.getTitle(), category, askFormRequest.getContent(), files);
        }

        return "redirect:/cs";
    }

    @ExceptionHandler(ValidationAskException.class)
    public String validAskHandleException(Exception ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        log.error("Exception occurred at request URI: {}", request.getRequestURI(), ex);

        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND);


        return "error";
    }

}
