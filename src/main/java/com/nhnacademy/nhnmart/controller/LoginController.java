package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.Agent;
import com.nhnacademy.nhnmart.domain.Ask;
import com.nhnacademy.nhnmart.domain.Customer;
import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.UserNotFoundException;
import com.nhnacademy.nhnmart.exception.UserNotMatchesException;
import com.nhnacademy.nhnmart.service.AskService;
import com.nhnacademy.nhnmart.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
//@RequestMapping("/cs/login")
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if(model.getAttribute("error") != null) {
            model.addAttribute("error", model.getAttribute("error"));
        }
        if (user == null) {
            return "loginForm";
        }else {
            return "logoutForm";
        }
    }
    @PostMapping("/login")
    public String login(@RequestParam("id") String id, @RequestParam("password") String password,HttpSession session,RedirectAttributes redirectAttributes) {

        User user = loginService.login(id, password);

        if(user instanceof Customer){
            session.setAttribute("user", user);
            return "redirect:/cs";
        }
        else if(user instanceof Agent){
            session.setAttribute("user", user);
            return "redirect:/cs/admin";
        }

        return "redirect:/";

    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @ExceptionHandler(UserNotMatchesException.class)
    public String noMaths(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "아이디와 비밀번호가 일치하지 않습니다.");
        return "redirect:/";
    }
    @ExceptionHandler(UserNotFoundException.class)
    public String notFound(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "아이디가 존재하지 않습니다.");
        return "redirect:/";
    }
}
