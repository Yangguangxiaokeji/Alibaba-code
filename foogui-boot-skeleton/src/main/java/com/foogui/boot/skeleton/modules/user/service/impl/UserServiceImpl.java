package com.foogui.boot.skeleton.modules.user.service.impl;

import com.foogui.boot.skeleton.common.domain.PageQuery;
import com.foogui.boot.skeleton.common.domain.PageResult;
import com.foogui.boot.skeleton.modules.user.domain.UserDTO;
import com.foogui.boot.skeleton.modules.user.domain.UserPO;
import com.foogui.boot.skeleton.modules.user.mapper.UserMapper;
import com.foogui.boot.skeleton.modules.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Foogui
 * @since 2023-05-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {

    @Override
    public PageResult<List<UserDTO>> query(PageQuery<UserDTO> pageQuery) {
        return null;
    }
}
