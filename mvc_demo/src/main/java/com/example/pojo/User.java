package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@TableName(value = "ay_user")
@Component
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Length(min = 2,max = 10,message = "用户名长度为2-10个字符")
    private String name;

    @Email(message = "请输入邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String mail;

    @NotNull(message = "密码不能为空")
    @Length(min = 6,max = 20,message = "密码长度为6-20个字符")
    private String password;


}
