package com.foogui.boot.skeleton.modules.user.service;

import com.foogui.boot.skeleton.common.domain.PageQuery;
import com.foogui.boot.skeleton.common.domain.PageResult;
import com.foogui.boot.skeleton.modules.user.domain.UserDTO;
import com.foogui.boot.skeleton.modules.user.domain.UserPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Foogui
 * @since 2023-05-22
 */
public interface UserService extends IService<UserPO> {

    PageResult<List<UserDTO>> query(PageQuery<UserDTO> pageQuery);
}
