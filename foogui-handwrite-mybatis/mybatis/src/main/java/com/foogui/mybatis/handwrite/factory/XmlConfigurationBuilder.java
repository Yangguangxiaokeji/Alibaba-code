package com.foogui.mybatis.handwrite.factory;

import com.foogui.mybatis.handwrite.config.Configuration;
import com.foogui.mybatis.handwrite.utils.Resources;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Setter
@Getter
public class XmlConfigurationBuilder {
    // 保存Configuration内容到自身
    private Configuration configuration;

    public XmlConfigurationBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 解析配置
     *
     * @param inputStream 配置文件的输入流
     * @return {@link Configuration}
     */
    public Configuration parseConfig(InputStream inputStream) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(inputStream);
        // 获取根节点
        Element rootElement = document.getRootElement();
        // 获取节点下的Element
        Node node = rootElement.selectSingleNode("//dataSource");
        List<Element> list = node.selectNodes("//property");
        // 类似map
        Properties properties = new Properties();
        for (Element ele : list) {
            // 获取Element内的属性
            String name = ele.attributeValue("name");
            String value = ele.attributeValue("value");
            properties.setProperty(name, value);
        }
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        // 设置数据源连接信息
        configuration.setDataSource(comboPooledDataSource);
        //UserMapper.xml解析：拿到路径》字节输入流》进行解析
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element ele : mapperList) {
            String mapperPath = ele.attributeValue("resource");
            InputStream stream = Resources.getResourceAsStream(mapperPath);
            // 解析UserMapper.xml输入流,注入到configuration中的mappedStatementMap
            XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
            xmlMapperBuilder.parse(stream);
        }
        return configuration;
    }
}
