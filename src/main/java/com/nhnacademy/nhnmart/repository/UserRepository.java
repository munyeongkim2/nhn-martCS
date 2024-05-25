package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.Agent;
import com.nhnacademy.nhnmart.domain.Customer;
import com.nhnacademy.nhnmart.domain.User;

import java.util.List;

public interface UserRepository {
    void init(User user);

    boolean exists(String id);

    User getUser(String id);
//    User getAgent(String id);

    boolean matches(String id, String password);

    boolean logout(String id);

    List<User> getCustomerList();
    List<Agent> getAgentList();

}
