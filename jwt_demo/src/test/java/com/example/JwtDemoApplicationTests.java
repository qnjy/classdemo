package com.example;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class JwtDemoApplicationTests {

    //生成jwt
    @Test
    void contextLoads() {
        //失效时间设定
        long date = System.currentTimeMillis();
        long exp = date + 60 * 1000;
        JwtBuilder jwtBuilder = Jwts.builder()
                //唯一id
                .setId("999")
                //接收的用户
                .setSubject("Rose")
                //签发时间
                .setIssuedAt(new Date())
                //签名算法及秘钥
                .signWith(SignatureAlgorithm.HS256, "xxxx")
                //设置jwt失效时间
                .setExpiration(new Date(exp))
                ;
        //生成签发token
        String token = jwtBuilder.compact();
        System.out.println(token);
        //生成的token可以通过官网解 也可以手动解
        String[] split = token.split("\\.");
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));

    }

    //token的解析
    @Test
    void testParse(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5OTkiLCJzdWIiOiJSb3NlIiwiaWF0IjoxNjE5MjczMDEzLCJleHAiOjE2MTkyNzMwNzN9.tBbivwU4a7Njlx76n3yB14HasyxfjUFWRwwvah-kKKQ";
        //解析token，获取Claims，jwt中荷载声明的对象
        Claims claims =(Claims) Jwts
                .parser()
                .setSigningKey("xxxx")
                .parse(token)
                .getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
    }

    //自定义声明  生成jwt
    @Test
    void createJwtClaim() {
        //失效时间设定
        long date = System.currentTimeMillis();
        long exp = date + 60 * 1000 * 2;
        JwtBuilder jwtBuilder = Jwts.builder()
                //唯一id
                .setId("999")
                //接收的用户
                .setSubject("Rose")
                //签发时间
                .setIssuedAt(new Date())
                //签名算法及秘钥
                .signWith(SignatureAlgorithm.HS256, "xxxx")
                .setExpiration(new Date(exp))
                //自定义声明
                .claim("name","tom")
                ;
        //生成签发token
        String token = jwtBuilder.compact();
        System.out.println(token);
    }
    @Test
    void testParseClaim(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5OTkiLCJzdWIiOiJSb3NlIiwiaWF0IjoxNjE5MjczNjQ2LCJleHAiOjE2MTkyNzM3NjYsIm5hbWUiOiJ0b20ifQ.G2o4Z2mBbDad_Z7Qo22pZ05BszKoaeLHIhE35mVlZEU";
        //解析token，获取Claims，jwt中荷载声明的对象
        Claims claims =(Claims) Jwts
                .parser()
                .setSigningKey("xxxx")
                .parse(token)
                .getBody();
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
        System.out.println(claims.get("name"));


    }

}
