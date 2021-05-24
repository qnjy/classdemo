package com.example.service.impl;

import com.example.entity.AyUser;
import com.example.repositoty.UserRepository;
import com.example.service.AyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AyUserServiceImpl implements AyUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public AyUser findById(Integer id) {

        return userRepository.findById(id).get();
    }

    @Override
    public List<AyUser> findAll() {
        PageRequest pageRequest = PageRequest.of(1, 2);
        Page<AyUser> page = userRepository.findAll(pageRequest);
        page.getContent().forEach(System.out::println);
        return userRepository.findAll();
    }

    @Override
    public AyUser findByName(String name) {
        return userRepository.findByName(name);
//        return null;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        userRepository.deleteUserById(id);
    }


}
