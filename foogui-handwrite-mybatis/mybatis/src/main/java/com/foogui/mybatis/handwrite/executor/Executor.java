package com.foogui.mybatis.handwrite.executor;

import com.foogui.mybatis.handwrite.config.Configuration;
import com.foogui.mybatis.handwrite.config.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... param) throws Exception;


    int insert(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws Exception;
}
