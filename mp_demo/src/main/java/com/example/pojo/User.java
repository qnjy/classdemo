package com.example.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "zy_user")
public class User extends Model<User> implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String email;

//    private GenderEnum gender;


    //自动填充  添加时
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //自动填充  添加和更新时
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //乐观锁version注解
    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;




}
