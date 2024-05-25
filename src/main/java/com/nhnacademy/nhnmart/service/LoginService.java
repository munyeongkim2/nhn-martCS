package com.nhnacademy.nhnmart.service;

import com.nhnacademy.nhnmart.domain.Customer;
import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.repository.UserRepository;
import com.nhnacademy.nhnmart.exception.UserNotMatchesException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String id, String password) {
        if(!userRepository.matches(id, password)){
            throw new UserNotMatchesException();
        }

        return userRepository.getUser(id);
    }

}
