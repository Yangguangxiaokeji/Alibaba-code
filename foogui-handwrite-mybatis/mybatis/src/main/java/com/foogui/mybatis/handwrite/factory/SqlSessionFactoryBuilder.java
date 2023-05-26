package com.foogui.mybatis.handwrite.factory;

import com.foogui.mybatis.handwrite.config.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * sql会话工厂建造者,将配置文件的流装载到factory中，用于后续生成SqlSession
 *
 * @author wangxin213
 * @date 2023/03/10
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream inputStream) throws DocumentException, PropertyVetoException {
        // 1.使用dom4j解析配置文件，将解析出来的内容封装到Configuration
        XmlConfigurationBuilder builder = new XmlConfigurationBuilder();
        // 解析配置文件到配置类中
        Configuration configuration=builder.parseConfig(inputStream);
        // 2.创建sqlSessionFactory对象：工厂类，生产sqlSession
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
