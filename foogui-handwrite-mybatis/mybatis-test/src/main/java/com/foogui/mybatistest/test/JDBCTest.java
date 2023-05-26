package com.foogui.mybatistest.test;


import com.foogui.mybatistest.po.User;
import org.junit.Test;

import java.sql.*;

public class JDBCTest {
    /**
     * 1.加载数据库驱动
     * 2.通过驱动管理类获取数据库链接
     * 3.定义sql语句?表示占位符
     * 4.获取预处理statement
     * 5.设置参数，第一个参数为sql语句中参数的序号(从1开始)，第二个参数为设置的参数值
     * 6.向数据库发出sql执行查询，查询出结果集
     * 7.遍历查询结果集，并封装返回对象
     */
    @Test
    public void TestJDBC() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/mybatis?characterEncoding=utf-8", "root", "1234");

            ps = connection.prepareStatement("select * from user where username=?");
            ps.setString(1, "tom");

            rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                System.out.println(user);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
