package com.foogui.boot.skeleton.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SexEnum {
    MAN(1, "男"),
    WOMAN(0, "女");
    @EnumValue      //将注解所标识的属性的值存储到数据库中
    private final Integer code;
    private final String name;

    SexEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
