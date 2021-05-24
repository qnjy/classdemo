package com.example.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.User;
import net.sf.ehcache.Ehcache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.caches.ehcache.EhcacheCache;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
//@CacheNamespace(blocking = true,implementation =EhcacheCache.class)
public interface UserMapper extends BaseMapper<User> {



}
