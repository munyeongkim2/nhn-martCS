package com.nhnacademy.nhnmart.listener;

import com.nhnacademy.nhnmart.domain.Agent;
import com.nhnacademy.nhnmart.domain.Ask;
import com.nhnacademy.nhnmart.domain.Customer;
import com.nhnacademy.nhnmart.repository.AskRepository;
import com.nhnacademy.nhnmart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyApplicationListener {

    private final UserRepository userRepository;
    private final AskRepository askRepository;


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
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
    }

}
