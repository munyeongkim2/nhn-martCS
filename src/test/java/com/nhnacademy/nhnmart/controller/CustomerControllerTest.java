package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.Agent;
import com.nhnacademy.nhnmart.domain.Ask;
import com.nhnacademy.nhnmart.domain.Customer;
import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.ValidationAskException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private AskService askService;
    @MockBean
    private AskRepository askRepository;
    @MockBean
    private UserRepository userRepository;
    private User customer;
    @Autowired
    private HttpSession httpSession;
    MockHttpSession session = new MockHttpSession();
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController(askService)).build();
        customer = new Customer("123","123","강하경");
        userRepository.init(new Customer("123","123","강하경"));
        userRepository.init(new Customer("qwe","123","빅희준"));
        userRepository.init(new Customer("asd","123","채상희"));
        userRepository.init(new Customer("zxc","123","이재이"));
        userRepository.init(new Agent("111","111","나야나"));

        askRepository.save("123",new Ask("test1","칭찬해요","테스트 내용입니다만","2024-05-01 12:00"));
        askRepository.save("qwe",new Ask("test2","불만 접수","테스트 내용입니다만","2024-05-01 12:00"));
        askRepository.save("asd",new Ask("test3","기타 문의","테스트 내용입니다만","2024-05-01 12:00"));
        askRepository.save("zxc",new Ask("test3","환불/교환","테스트 내용입니다만","2024-05-01 12:00"));
        askRepository.save("zxc",new Ask("test3","제안","테스트 내용입니다만","2024-05-01 12:00"));

        session.setAttribute("user", customer);
    }

    @Test
    @DisplayName("고객 메인페이지 이동")
    void dashboardView() throws Exception {

        mockMvc.perform(get("/cs").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", customer.getId()))
                .andExpect(view().name("customerDashboard"));
    }

    @Test
    @DisplayName("고객 문의 상세 이동")
    void detailAsk() throws Exception {
        mockMvc.perform(get("/cs/detail/{title}","123").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", customer.getId()))
                .andExpect(model().attribute("ask",askService.getAsk("123","test1")))
                .andExpect(view().name("askDetails"));

        verify(askService, times(1)).getAsk("123", "test1");
    }

    @Test
    @DisplayName("고객 문의글 생성폼")
    void createAskFrom() throws Exception {
        mockMvc.perform(get("/cs/ask","123").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", customer.getId()))
                .andExpect(model().attribute("categoryList",askService.getCategory()))
                .andExpect(view().name("askForm"));

    }

    @Test
    @DisplayName("고객 메인 카테고리 검색")
    void dashBoardSearch() throws Exception {
        mockMvc.perform(get("/cs/search","123").session(session)
                        .param("id", "123")
                        .param("category", "칭찬해요"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("id", customer.getId()))
                .andExpect(model().attribute("asks", askService.getAskForSearchService(customer.getId(), "칭찬해요")))
                .andExpect(model().attribute("category","칭찬해요"))
                .andExpect(model().attribute("categoryList",askService.getCategory()))
                .andExpect(view().name("customerDashboard"));

        mockMvc.perform(get("/cs/search","123").session(session)
                        .param("id", "123")
                        .param("category", "all"))
                .andExpect(redirectedUrl("/cs"));

    }

    @Test
    @DisplayName("고객 문의글 생성 요청 처리")
    void createAsk() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", customer);

        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/cs/ask")
                        .file(file)
                        .param("category", "칭찬해요")
                        .param("title", "test1")
                        .param("content", "내용입니다")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs"));

    }

    @Test
    @DisplayName("validAskHandleException 처리")
    void validAskHandleException() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", customer);

        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        mockMvc.perform(multipart("/cs/ask")
                .file(file)
                .param("category", "칭찬해요")
                .param("title", "1")
                .param("content", "내용입니다").session(session))
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("status", HttpStatus.NOT_FOUND));

    }
}