package com.foogui.mybatistest.test;

import com.foogui.mybatis.handwrite.config.SqlSession;
import com.foogui.mybatis.handwrite.factory.SqlSessionFactory;
import com.foogui.mybatis.handwrite.factory.SqlSessionFactoryBuilder;

import com.foogui.mybatis.handwrite.utils.Resources;
import com.foogui.mybatistest.mapper.UserMapper;
import com.foogui.mybatistest.po.User;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class MybatisTest {
    /**
     * 直接调用sqlSession的API
     *
     * @throws Exception 异常
     */
    @Test
    public void selectOne() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setId(1);
        user.setUsername("lucy");
        User one = sqlSession.selectOne("com.foogui.mybatistest.mapper.UserMapper.selectOne", user);
        System.out.println(one);
    }
    /**
     * 直接调用sqlSession的API
     *
     * @throws Exception 异常
     */
    @Test
    public void selectList() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //调用
        User user = new User();
        user.setId(1);
        user.setUsername("lucy");
        List<User> users = sqlSession.selectList("com.foogui.mybatistest.mapper.UserMapper.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    /**
     * 利用接口代理进行查询
     *
     * @throws Exception 异常
     */
    @Test
    public void selectListOfProxy() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //调用
        User user = new User();
        user.setId(1);
        user.setUsername("lucy");

        // 返回接口的代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 代理对象调用接口中任意方法，都会执行invoke方法
        System.out.println(userMapper.selectList());
        System.out.println(userMapper.selectOne(user));
    }
    /**
     * 利用接口代理进行插入
     *
     * @throws Exception 异常
     */
    @Test
    public void insertOfProxy() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //调用
        User user = new User();

        user.setUsername("wangxin");
        // 返回接口的代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(userMapper.insert(user));
    }
}
