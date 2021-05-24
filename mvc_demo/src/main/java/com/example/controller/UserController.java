package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.Mapper.UserMapper;
import com.example.config.RedisConfig;
import com.example.pojo.User;
import com.example.service.UserService;
import com.example.util.BloomFilterHelper;
import com.example.util.RedisUtils;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    //保存使用post
    @PostMapping("/")
    public void insertUser(){
        User user = new User();
        user.setName("阿祖");
        user.setMail("789456@qq.com");
        user.setPassword("888999");
        userService.insertUser(user);
    }
    //查询get
    @GetMapping("/{id}")
    public void findUserById(@PathVariable Integer id){
        User user = userService.findUserById(id);
        System.out.println(user);
    }
    //修改
    @PutMapping("/{id}")
    public void updateUser(User user){
        user.setName("阿龙");
        userService.updateUserById(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id){
        userService.deleteUserById(id);
    }


    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private RedisUtils redisUtil;

    @PostMapping(value = "/addEmailToBloom", produces = "application/json")
    public ResponseEntity<String> addUser(@RequestBody String params) {
        ResponseEntity<String> response = null;
        String returnResultStr;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject requestJsonObj = JSON.parseObject(params);
            User inputUser = getUserFromJson(requestJsonObj);
            BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>)
                    (from, into)
                            -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8),
                    1500000, 0.00001);
            redisUtil.addByBloomFilter(myBloomFilterHelper, "email_existed_bloom", inputUser.getMail());
            result.put("code", HttpStatus.OK.value());
            result.put("message", "add into bloomFilter successfully");
            result.put("email", inputUser.getMail());
            returnResultStr = JSON.toJSONString(result);

            response = new ResponseEntity<>(returnResultStr, headers, HttpStatus.OK);
        } catch (Exception e) {
            result.put("message", "add a new product with error: " + e.getMessage());
            returnResultStr = JSON.toJSONString(result);
            response = new ResponseEntity<>(returnResultStr, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(value = "/checkEmailInBloom", produces = "application/json")
    public ResponseEntity<String> findEmailInBloom(@RequestBody String params) {
        ResponseEntity<String> response = null;
        String returnResultStr;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject requestJsonObj = JSON.parseObject(params);
            User inputUser = getUserFromJson(requestJsonObj);
            BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from,
                                                                                                      into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1500000, 0.00001);
            boolean answer = redisUtil.includeByBloomFilter(myBloomFilterHelper, "email_existed_bloom",
                    inputUser.getMail());
//            logger.info("answer=====" + answer);
            result.put("code", HttpStatus.OK.value());
            result.put("email", inputUser.getMail());
            result.put("exist", answer);
            returnResultStr = JSON.toJSONString(result);
//            logger.info("returnResultStr======>" + returnResultStr);
            response = new ResponseEntity<>(returnResultStr, headers, HttpStatus.OK);
        } catch (Exception e) {
//            logger.error("add a new product with error: " + e.getMessage(), e);
            result.put("message", "add a new product with error: " + e.getMessage());
            returnResultStr = JSON.toJSONString(result);
            response = new ResponseEntity<>(returnResultStr, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private User getUserFromJson(JSONObject requestObj) {
        String userName = requestObj.getString("username");
        String userAddress = requestObj.getString("address");
        String userEmail = requestObj.getString("email");
        int userAge = requestObj.getInteger("age");
        User u = new User();
        u.setName(userName);
        u.setMail(userEmail);
        return u;

    }




}
