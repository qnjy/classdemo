package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Repository;

/**
 * @CacheNamespace：  注解配置mybatis缓存使用
 */
@Repository
@CacheNamespace(blocking = true)
public interface UserMapper extends BaseMapper<User> {

    IPage<User> findPageVo(Page<?> page,String name);

}
