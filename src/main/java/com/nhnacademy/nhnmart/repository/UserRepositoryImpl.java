package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.Agent;
import com.nhnacademy.nhnmart.domain.Customer;

import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> userMap = new HashMap<>();

    public UserRepositoryImpl() {
    }

    @Override
    public void init(User user) {
        userMap.put(user.getId(), user);
    }

    @Override
    public boolean exists(String id) {
        return userMap.containsKey(id);
    }

    @Override
    public User getUser(String id) {
        if(!exists(id)) {
            throw new UserNotFoundException();
        }
        return userMap.get(String.valueOf(id));
    }

    @Override
    public boolean matches(String id, String password) {
        if(!exists(id)) {
            throw new UserNotFoundException();
        }
        User user = userMap.get(id);
        return user.getPassword().equals(password);
    }

    @Override
    public boolean logout(String id) {
        if(!exists(id)) {
            throw new UserNotFoundException();
        }
        userMap.remove(id);
        return true;

    }

    @Override
    public List<User> getCustomerList() {
        return List.of();
    }

    @Override
    public List<Agent> getAgentList() {
        return List.of();
    }
}
