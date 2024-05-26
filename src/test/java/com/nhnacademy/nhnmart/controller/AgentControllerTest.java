package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.*;
import com.nhnacademy.nhnmart.repository.AskRepository;
import com.nhnacademy.nhnmart.repository.UserRepository;
import com.nhnacademy.nhnmart.service.AskService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgentController.class)
class AgentControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private AskService askService;
    @MockBean
    private AskRepository askRepository;
    @MockBean
    private UserRepository userRepository;
    private User agent;
    @Autowired
    private HttpSession httpSession;
    MockHttpSession session = new MockHttpSession();
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AgentController(askService)).build();
        agent = new Agent("111","111","나야나");
        userRepository.init(new Customer("123","123","강하경"));
        userRepository.init(new Agent("111","111","나야나"));

        askRepository.save("123",new Ask("test1","칭찬해요","테스트 내용입니다만","2024-05-01 12:00"));
        askRepository.save("qwe",new Ask("test2","불만 접수","테스트 내용입니다만","2024-05-01 12:00"));
        askRepository.addAnswer("123","test1", new Answer("111","2024-05-01 12:00","나야나"));
        session.setAttribute("user", agent);
    }
    @Test
    @DisplayName("에이전트 메인페이지 이동")
    void dashboardView() throws Exception {
        mockMvc.perform(get("/cs/admin"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("asks", askService.getAsksForAgent()))
                .andExpect(view().name("agentDashboard"));

    }

    @Test
    @DisplayName("에이전트 답변폼 이동")
    void answerForm() throws Exception {
        Ask ask = new Ask("test1", "칭찬해요", "테스트 내용입니다만", "2024-05-01 12:00");
        when(askService.getAsk("123", "test1")).thenReturn(ask);
        mockMvc.perform(get("/cs/admin/answer/123/test1"))
                .andExpect(status().isOk())
                .andExpect(view().name("answerForm"))
                .andExpect(model().attribute("ask", ask))
                .andExpect(model().attribute("customerId", "123"));

        verify(askService, times(1)).getAsk("123", "test1");
    }

    @Test
    @DisplayName("답변 생성 요청 처리")
    void answerCreate() throws Exception {
        mockMvc.perform(post("/cs/admin/answerCreate")
                        .param("customerId", "123")
                        .param("title", "test1")
                        .param("content", "aa")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));

        verify(askService, times(1)).addAnswer("123", "test1", "aa", agent.getId());
    }

    @Test
    @DisplayName("validAnswerHandleException 처리")
    void validAnswerHandleException() throws Exception {
        mockMvc.perform(post("/cs/admin/answerCreate")
                        .param("customerId", "123")
                        .param("title", "")
                        .param("content", "testt")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("status", HttpStatus.NOT_FOUND));
    }
}