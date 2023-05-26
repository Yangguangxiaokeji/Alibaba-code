package com.foogui.mybatis.handwrite.factory;

import com.foogui.mybatis.handwrite.config.Configuration;
import com.foogui.mybatis.handwrite.config.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;

public class XmlMapperBuilder {
    private Configuration configuration;

    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析流，注入到configuration中的mappedStatementMap
     *
     * @param
     */
    public void parse(InputStream inputStream) throws DocumentException {
        // 这个document是UserMapper.xml解析后的Document对象
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        List<Element> selectList = rootElement.selectNodes("//select");
        List<Element> insertList = rootElement.selectNodes("//insert");
        List<Element> deleteList = rootElement.selectNodes("//delete");
        List<Element> updateList = rootElement.selectNodes("//update");
        setMappedStatementMap(namespace, selectList);
        setMappedStatementMap(namespace, insertList);
        setMappedStatementMap(namespace, deleteList);
        setMappedStatementMap(namespace, updateList);
    }

    /**
     * 设置MappedStatementMap
     *
     * @param namespace   名称空间
     * @param elementList 元素列表
     */
    private void setMappedStatementMap(String namespace, List<Element> elementList) {
        if (!CollectionUtils.isEmpty(elementList)) {
            for (Element element : elementList) {
                String id = element.attributeValue("id");
                String resultType = element.attributeValue("resultType");
                String parameterType = element.attributeValue("parameterType");
                // 拿到真正的sql语句
                String sqlText = element.getTextTrim();
                MappedStatement mappedStatement = new MappedStatement();
                mappedStatement.setId(id);
                mappedStatement.setResultType(resultType);
                mappedStatement.setParameterType(parameterType);
                mappedStatement.setSql(sqlText);
                // key为namespace.id
                String statementId = namespace + "." + id;
                configuration.getMappedStatementMap().put(statementId, mappedStatement);
            }
        }
    }
}
