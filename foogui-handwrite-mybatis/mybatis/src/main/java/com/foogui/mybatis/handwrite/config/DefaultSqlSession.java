package com.foogui.mybatis.handwrite.config;

import com.foogui.mybatis.handwrite.executor.SimpleExecutor;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        // 调用SimpleExecutor执行器的方法进行执行
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);
        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<T> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return objects.get(0);
        } else {
            throw new RuntimeException("查询结果为空或者返回结果过多");
        }
    }

    @Override
    public int insert(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        int res = simpleExecutor.insert(configuration, mappedStatement, params);
        return res;
    }

    @Override
    public int delete(String statementId, Object... params) throws Exception {
        return 0;
    }

    @Override
    public int update(String statementId, Object... params) throws Exception {
        return 0;
    }

    /**
     * 创建Mapper接口的代理对象
     * @param cls cls
     * @return {@link T}
     */
    @Override
    public <T> T getMapper(Class<T> cls) {
        Object instance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{cls}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                // 接口全限定名
                String className = cls.getName();
                String statementId = className + "." + methodName;
                // 获取方法的返回值类型
                Type genericReturnType = method.getGenericReturnType();
                // 判断的是返回值类型是否is参数化类型，如集合<字符串>
                if (genericReturnType instanceof ParameterizedType) {
                    List<T> objects = selectList(statementId, args);
                    return objects;
                } else if ("int".equals(genericReturnType.getTypeName())) {
                    return insert(statementId, args);
                } else {
                    return selectOne(statementId, args);
                }
            }
        });
        return (T) instance;
    }
}
