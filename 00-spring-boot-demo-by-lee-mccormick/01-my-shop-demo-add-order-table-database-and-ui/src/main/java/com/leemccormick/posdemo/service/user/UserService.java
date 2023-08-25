package com.leemccormick.posdemo.service.user;

import com.leemccormick.posdemo.entity.User;

import java.util.List;

public interface UserService {
    // READ
    List<User> findAllUsers();

    User findById(String theId);

    String findUserFullName(String theId);
}
