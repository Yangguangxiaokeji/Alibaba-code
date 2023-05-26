package com.foogui.mybatis.handwrite.factory;

import com.foogui.mybatis.handwrite.config.SqlSession;

public interface SqlSessionFactory {
    SqlSession openSession();
}
