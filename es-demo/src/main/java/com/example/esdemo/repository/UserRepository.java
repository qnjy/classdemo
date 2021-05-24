package com.example.esdemo.repository;

import com.example.esdemo.pojo.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//继承ElasticsearchRepository，实现集成操作   jpa一致
@Repository
public interface UserRepository extends ElasticsearchRepository<User, Long> {


    //自定义操作 参考 jpa
    // Spring-data-es 4.x 版本中   推荐使用自定义操作 进行复杂查询
    User findByName(String name);

    List<User> findUserByAgeBetween(int from,int to);

    List<User> findUserByNameLike(String name, Pageable pageable);

}
