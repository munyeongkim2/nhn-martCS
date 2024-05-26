package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.Agent;
import com.nhnacademy.nhnmart.domain.Customer;
import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.UserNotFoundException;
import com.nhnacademy.nhnmart.exception.UserNotMatchesException;
import com.nhnacademy.nhnmart.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

@WebMvcTest(LoginController.class)
class LoginControllerTest {


    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;
    private User customer;
    private User agent;
    @Autowired
    private HttpSession httpSession;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(loginService)).build();
        customer = new Customer("123","123","강하경");
        agent = new Agent("111","111","빅희준");
    }


    @Test
    @DisplayName("로그인 화면 출력")
    void home() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", customer);

        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(view().name("logoutForm"));

        MockHttpSession session2 = new MockHttpSession();
        session2.setAttribute("user", null);

        mockMvc.perform(get("/").session(session2))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(view().name("loginForm"));
    }

    @Test
    @DisplayName("로그인 요청 처리")
    void login() throws Exception {

        when(loginService.login(anyString(), anyString())).thenReturn(customer);

        mockMvc.perform(post("/login")
                        .param("id", "123")
                        .param("password", "123"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/cs"))
                        .andExpect(request().sessionAttribute("user", customer));

        when(loginService.login(anyString(), anyString())).thenReturn(agent);
        mockMvc.perform(post("/login")
                        .param("id", "111")
                        .param("password", "111"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"))
                .andExpect(request().sessionAttribute("user", agent));

        when(loginService.login(anyString(), anyString())).thenReturn(null);
        mockMvc.perform(post("/login")
                        .param("id", "111")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(request().sessionAttributeDoesNotExist("user"));

    }

    @Test
    @DisplayName("로그아웃 요청 처리")
    void logout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", customer);

        mockMvc.perform(get("/logout").session(session))
                .andExpect(redirectedUrl("/"))
                .andExpect(request().sessionAttributeDoesNotExist("user"));

    }

    @Test
    @DisplayName("UserNotMatchesException 예외 처리")
    void loginUserNotMatchesException() throws Exception {
        when(loginService.login(anyString(), anyString())).thenThrow(new UserNotMatchesException());

        mockMvc.perform(post("/login")
                        .param("id", "wrong")
                        .param("password", "wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", "아이디와 비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("UserNotFoundException 예외 처리")
    void loginUserNotFoundException() throws Exception {
        when(loginService.login(anyString(), anyString())).thenThrow(new UserNotFoundException());

        mockMvc.perform(post("/login")
                        .param("id", "nonexistent")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("error", "아이디가 존재하지 않습니다."));
    }
}