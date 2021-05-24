package com.example.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * (AyUser)实体类
 *
 * @author xy
 * @since 2021-03-01 20:22:14
 */
@Data
@Entity
@Table(name = "ay_user")
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor()
public class AyUser implements Serializable {
    private static final long serialVersionUID = 736559751038807273L;
    /**
     * @GeneratedValue : 主键生成策略
     *      GenerationType.IDENTITY ： 自增  mysql
     *      GenerationType.AUTO : 由程序自动帮助选择主键生成策略
     *      GenerationType.SEQUENCE ：序列 oracle
     *      GenerationType.TABLE : 通过一张基础数据库表完成主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @NonNull
    private Integer id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String mail;




}