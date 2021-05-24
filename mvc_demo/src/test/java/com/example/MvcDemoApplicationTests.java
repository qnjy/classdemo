package com.example;

import com.example.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class MvcDemoApplicationTests {

    //redis操作的工具类
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    void test(){
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.flushDb();

    }

    @Test
    void testRedis() {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("name","jack");
        ops.set("age","20");
        System.out.println(ops.get("name"));
        System.out.println(ops.get("age"));
        User user = new User();
        user.setName("jack");
//        RedisAutoConfiguration
//        ops.set("user",user);

//        System.out.println(ops.get("user"));

        ops.append("name","tom");

        System.out.println(ops.get("name")+"****");
//        redisTemplate.delete("name");
//        redisTemplate.delete("age");

    }


    @Test
    void testStringRedis(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

    }

    @Test
    void testString(){
        redisTemplate.opsForValue().set("name","tom",3, TimeUnit.SECONDS);
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
        try {
            Thread.currentThread().sleep(5000);
            Object name2 = redisTemplate.opsForValue().get("name");
            System.out.println(name2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    void redisList(){

    }




}
