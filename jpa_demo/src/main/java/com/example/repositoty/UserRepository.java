package com.example.repositoty;

import com.example.entity.AyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public interface UserRepository extends JpaRepository<AyUser,Integer>, JpaSpecificationExecutor<AyUser> {
    //nativeQuery = true 测试执行原生sql  此时要求sql是原生的，不能再使用实体进行表映射
    @Query(nativeQuery = true,value = "select u.* from ay_user u where u.name = ?1 ")
    AyUser findByName(String name);

    @Modifying
    @Query("delete from AyUser where id = ?1")
    void deleteUserById(Integer id);


}
