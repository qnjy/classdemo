package com.example.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;

/**
 * spring-boot整合redis操作，即spring 中使用redis缓存
 * CachingConfigurerSupport : spring中提供的缓存配置基础类，例如提供的key生成策略
 *      自定义redis时，需要注意key生成策略
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    //在Redis中，关于对象保存时，若不指定key会默认采用SimpleKeyGenerator机制，
    // 默认根据方法参数组合一个key，实际中，需要记性修改，重写key生成策略
    @Override
    public KeyGenerator keyGenerator() {
        //重新定义key根据类名、方法名、参数进行生成  避免出现缓存不同操作key一样
         return (target, method, params) -> {
             StringBuilder sb = new StringBuilder();
             sb.append(target.getClass().getName());
             sb.append(method.getName());
             for (Object param : params) {
                 sb.append(params);
             }
             return sb;
         };
    }
    //配置使用redis的缓存管理器
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory){
        RedisCacheManager cacheManager = RedisCacheManager.create(factory);
        return cacheManager;
    }

    //自定义RedisTemplate
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        //利用jackson实现json序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        //设置任何字段可见
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入类型
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);

        jackson2JsonRedisSerializer.setObjectMapper(om);
        //String序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        System.out.println("自定义");

        return template;
    }

}
