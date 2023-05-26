package com.foogui.mybatis.handwrite.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置对象-mybatis-config.xml的映射
 *
 * @author wangxin213
 * @date 2023/03/10
 */
@Data
public class Configuration {
    // 存放的是数据源的连接信息，与mybatis-config.xml中的<dataSource>标签对应
    private DataSource dataSource;
    // 存放的是XXXMapper.xml的信息，与mybatis-config.xml中的<mapper>标签对应，key对应mapper.xml中的namespace.id
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
}
