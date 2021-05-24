package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

//EnableCaching : 开启spring的cache功能
@SpringBootApplication
@EnableCaching
//@EntityScan(basePackages = {"com.example.pojo"})
public class MvcDemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(MvcDemoApplication.class, args);
    }

}
