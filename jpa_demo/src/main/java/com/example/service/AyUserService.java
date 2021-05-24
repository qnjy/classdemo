package com.example.service;

import com.example.entity.AyUser;

import java.util.List;

public interface AyUserService {

    AyUser findById(Integer id);
    List<AyUser> findAll();
    AyUser findByName(String name);
    void deleteById(Integer id);

}
