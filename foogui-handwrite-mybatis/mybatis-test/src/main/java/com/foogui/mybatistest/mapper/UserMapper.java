package com.foogui.mybatistest.mapper;

import com.foogui.mybatistest.po.User;

import java.util.List;

public interface UserMapper {
    List<User> selectList();

    User selectOne(User user);

    int insert(User user);
}
