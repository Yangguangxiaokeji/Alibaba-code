package com.foogui.mybatis.handwrite.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 装载符合PreparedStatement执行的sql
 *
 * @author wangxin213
 * @date 2023/03/12
 */
@Data
@AllArgsConstructor
public class BoundSql {

    //装符合PreparedStatement的格式sql
    private String sqlText;
    //#{}里面的值的集合
    private List<ParameterMapping> parameterMappings = new ArrayList<>();
}

