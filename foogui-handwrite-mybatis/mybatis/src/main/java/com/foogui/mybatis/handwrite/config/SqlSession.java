package com.foogui.mybatis.handwrite.config;

import java.util.List;

/**
 * sql会话，相当于对外暴露的API
 *
 * @author wangxin213
 * @date 2023/03/10
 */
public interface SqlSession {
    /**
     * 选择列表
     *
     * @param statementId mapper.xml的namespace.id
     * @param params      参数
     * @return {@link List}<{@link E}>
     * @throws Exception 异常
     */
    <E> List<E> selectList(String statementId, Object... params) throws Exception;

    <T> T selectOne(String statementId, Object... params) throws Exception;

    int insert(String statementId, Object... params) throws Exception;

    int delete(String statementId, Object... params) throws Exception;

    int update(String statementId, Object... params) throws Exception;

    /**
     * 反射获得接口的代理对象
     * @param cls cls
     * @return {@link T}
     */
    <T> T getMapper(Class<T> cls);
}
