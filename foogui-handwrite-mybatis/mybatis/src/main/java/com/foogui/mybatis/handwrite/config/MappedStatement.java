package com.foogui.mybatis.handwrite.config;

import lombok.Data;

/**
 * mapper.xml的信息映射；
 * 例如：对应一个UserMapper.xml
 *
 * @author wangxin213
 * @date 2023/03/10
 */
@Data
public class MappedStatement {

    /**
     * id标识
     */
    private String id;

    /**
     * 返回值类型
     */
    private String resultType;

    /**
     * 参数值类型
     */
    private String parameterType;

    /**
     * sql语句
     */
    private String sql;


}

