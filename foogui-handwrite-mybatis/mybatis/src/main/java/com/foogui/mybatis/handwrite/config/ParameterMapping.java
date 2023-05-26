package com.foogui.mybatis.handwrite.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParameterMapping {
    // 具体装的是#{}内的值，String格式
    private String content;
}
