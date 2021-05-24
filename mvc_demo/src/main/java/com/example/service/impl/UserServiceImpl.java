package com.example.service.impl;

import com.example.Mapper.UserMapper;
import com.example.pojo.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

//CacheConfig :基于类的缓存配置注解， cacheNames设置缓存名称
@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    //Cacheable 以键值对的方式缓存类或方法的返回值  #p0  以第一个参数作为key
    @Override
    @Cacheable(key = "#p0")
    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }

    //CachePut: 方法调用后，结果被缓存,每次返回结果都会进行缓存
    @Override
    @CachePut(key = "#user.id")
    public User insertUser(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    @CachePut(key = "#user.id")
    public User updateUserById(User user) {
        userMapper.updateById(user);
        return user;
    }

    //指定缓存清除的key值  此缓存记录会被清除  allEntries: 清空所有缓存
    @Override
    @CacheEvict(key = "#id",allEntries = true)
    public void deleteUserById(Integer id) {
        userMapper.deleteById(id);
    }
}
