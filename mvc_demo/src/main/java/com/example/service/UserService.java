package com.example.service;


import com.example.pojo.User;

public interface UserService {

    User findUserById(Integer id);

    User insertUser(User user);

    User updateUserById(User user);

    void deleteUserById(Integer id);
}
