package com.foogui.mybatis.handwrite.factory;

import com.foogui.mybatis.handwrite.config.Configuration;
import com.foogui.mybatis.handwrite.config.DefaultSqlSession;
import com.foogui.mybatis.handwrite.config.SqlSession;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultSqlSessionFactory implements SqlSessionFactory{
    private Configuration configuration;

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
