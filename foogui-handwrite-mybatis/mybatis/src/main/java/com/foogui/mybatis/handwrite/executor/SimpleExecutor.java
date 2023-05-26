package com.foogui.mybatis.handwrite.executor;

import com.foogui.mybatis.handwrite.config.BoundSql;
import com.foogui.mybatis.handwrite.config.Configuration;
import com.foogui.mybatis.handwrite.config.MappedStatement;
import com.foogui.mybatis.handwrite.config.ParameterMapping;
import com.foogui.mybatis.handwrite.utils.GenericTokenParser;
import com.foogui.mybatis.handwrite.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * sql执行器。真正执行sql的地方
 *
 * @author wangxin213
 * @date 2023/03/10
 */
public class SimpleExecutor implements Executor{

    /**
     * 查询
     *
     * @param configuration   全局配置
     * @param mappedStatement select标签内的属性映射，包含了sql语句
     * @param param           输入的查询参数
     * @return {@link List}<{@link E}>
     * @throws Exception 异常
     */
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... param) throws Exception {
        // 获取预编译的SQL对象PreparedStatement
        PreparedStatement preparedStatement = getPreparedStatement(configuration, mappedStatement, param);
        // 6.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        // 7.封装返回结果集到PO
        String resultType = mappedStatement.getResultType();
        // 获取返回类型对应的class对象，用于创建bean
        Class<?> resultTypeClass = getClassType(resultType);

        ArrayList<Object> objects = new ArrayList<>();
        while (resultSet.next()) {
            // 创建bean
            Object instance = resultTypeClass.newInstance();
            // 查询结果的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            // 遍历metaData往instance写入属性
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段值
                Object columnValue = resultSet.getObject(columnName);
                // 反射往bean内写入属性
                // 创建一个字段属性，并往内写入值
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(instance, columnValue);
            }
            objects.add(instance);
        }

        return (List<E>)objects;
    }

    @Override
    public int insert(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws Exception {
        PreparedStatement preparedStatement = getPreparedStatement(configuration, mappedStatement, params);
        // 6.执行sql
        int res = preparedStatement.executeUpdate();
        //查询主键并返回
        // TODO
        return res;
    }

    private PreparedStatement getPreparedStatement(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        // 1.注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();
        // 2.获取sql语句
        String sql = mappedStatement.getSql();
        // 3.转换sql语句格式
        BoundSql boundSql = getBoundSql(sql);
        // 4.获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        // 5.设置输入参数
        String parameterType = mappedStatement.getParameterType();
        // 获取输入参数的Class对象
        Class<?> parameterTypeClass = getClassType(parameterType);
        // 获取sql里的#{}内的值的集合
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            // 获取#{}内真正的值，与po的字段属性相同
            String content = parameterMapping.getContent();
            // 反射
            Field declaredFields = parameterTypeClass.getDeclaredField(content);
            // 暴力访问private属性
            declaredFields.setAccessible(true);
            // 获取输入参数po对应的字段的值
            Object o = declaredFields.get(params[0]);
            // 为sql占位符依次赋值
            preparedStatement.setObject(i+1, o);
        }
        return preparedStatement;
    }

    /**
     * 根据类名的路径，得到类Class对象
     *
     * @param parameterType 参数类型
     * @return {@link Class}<{@link ?}>
     */
    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (parameterType==null) throw new RuntimeException("输入参数parameterType属性必须定义");
        if ("int".equals(parameterType)){
            return int.class;
        } else if ("String".equals(parameterType)) {
            return String.class;
        }else {
            return Class.forName(parameterType);
        }
    }

    /**
     * sql类型转换成PreparedStatement的"?"格式，完成对#{}的解析工作
     *
     * @param sql sql
     * @return {@link BoundSql}
     */
    private BoundSql getBoundSql(String sql) {
        // 1.将#{}使用?代替
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parse = genericTokenParser.parse(sql);

        // 2.解析出#{}里面的值
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parse, parameterMappings);
        return boundSql;
    }
}
