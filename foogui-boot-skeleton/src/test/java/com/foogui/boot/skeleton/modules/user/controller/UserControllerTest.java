package com.foogui.boot.skeleton.modules.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.foogui.boot.skeleton.common.domain.PageResult;
import com.foogui.boot.skeleton.common.domain.Result;
import com.foogui.boot.skeleton.modules.user.domain.UserDTO;
import com.foogui.boot.skeleton.modules.user.domain.UserPO;
import com.foogui.boot.skeleton.modules.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    private UserDTO userDTO;

    private List<UserDTO> list;

    private LambdaQueryWrapper<UserPO> lambdaQueryWrapper;

    private QueryChainWrapper<UserPO> queryChainWrapper;

    @Before
    public void setUp() throws Exception {
        // 数据初始化
        userDTO = new UserDTO();
        userDTO.setUsername("username");
        userDTO.setPassword("password");
        userDTO.setSex("1");
        userDTO.setAge(10);
        userDTO.setPhone("18840832314");
        userDTO.setVersion(0L);
        userDTO.setCreated(LocalDateTime.now());

        list = new ArrayList<>();
        // 打桩
        lambdaQueryWrapper = new LambdaQueryWrapper<>();
    }

    @Test
    public void save() {
        Mockito.doReturn(Boolean.TRUE).when(service).save(Mockito.any(UserPO.class));
        Result success = controller.save(userDTO);
        Assertions.assertTrue(success.getSuccess());
        Mockito.doReturn(Boolean.FALSE).when(service).save(Mockito.any(UserPO.class));
        Result fail = controller.save(userDTO);
        Assertions.assertFalse(fail.getSuccess());

    }

    @Test
    public void update() {
        Mockito.doReturn(Boolean.TRUE).when(service).updateById(Mockito.any(UserPO.class));
        Result success = controller.update(1L,userDTO);
        Assertions.assertTrue(success.getSuccess());
        Mockito.doReturn(Boolean.FALSE).when(service).updateById(Mockito.any(UserPO.class));
        Result fail = controller.update(1L,userDTO);
        Assertions.assertFalse(fail.getSuccess());
    }

    @Test
    public void delete() {
        Mockito.doReturn(Boolean.TRUE).when(service).removeById(Mockito.any(Long.class));
        Result success = controller.delete(1L);
        Assertions.assertTrue(success.getSuccess());
        Mockito.doReturn(Boolean.FALSE).when(service).removeById(Mockito.any(Long.class));
        Result fail = controller.delete(1L);
        Assertions.assertFalse(fail.getSuccess());
    }

    @Test
    public void query() {

        list.add(userDTO);
        PageResult<List<UserDTO>> pageResult = new PageResult<>();
        pageResult.setData(list);
        Mockito.doReturn(pageResult).when(service).query(Mockito.any());

        Result<PageResult> result = controller.query(1, 10, userDTO);

        Assertions.assertTrue(result.getSuccess());
        list.clear();
    }
}