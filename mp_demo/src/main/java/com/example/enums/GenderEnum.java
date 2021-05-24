package com.example.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;

//通用枚举设置
@Getter
public enum GenderEnum {

    MALE(1,"男"),
    FEMALE(1,"女");

    @EnumValue
    private final int code;
    private final String desc;

    GenderEnum(int code,String desc){
        this.code = code;
        this.desc = desc;
    }




}
