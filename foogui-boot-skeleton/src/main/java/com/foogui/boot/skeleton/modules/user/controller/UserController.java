package com.foogui.boot.skeleton.modules.user.controller;


import com.foogui.boot.skeleton.common.domain.PageQuery;
import com.foogui.boot.skeleton.common.domain.PageResult;
import com.foogui.boot.skeleton.common.domain.Result;
import com.foogui.boot.skeleton.common.enums.ErrorCode;
import com.foogui.boot.skeleton.modules.user.domain.UserDTO;
import com.foogui.boot.skeleton.modules.user.domain.UserPO;
import com.foogui.boot.skeleton.modules.user.domain.UserVO;
import com.foogui.boot.skeleton.modules.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Foogui
 * @since 2023-05-22
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;



    @Cacheable(cacheNames = "users-cache", key = "#userDTO.phone")
    @PostMapping
    public Result save(@RequestBody UserDTO userDTO) {
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(userDTO,userPO);
        if (userService.save(userPO)) {
            return Result.success();
        } else {
            return Result.fail(ErrorCode.SYSTEM_ERROR);
        }

    }

    @Cacheable(cacheNames = "users-cache", key = "#userDTO.phone")
    @PostMapping("/cache")
    public Result testCache(@RequestBody UserDTO userDTO) {
        System.out.println("没有走缓存");
        return Result.success();


    }


    @PutMapping("/{id}")
    public Result update(@NotNull @PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        UserPO userPO = new UserPO();
        userPO.setId(id);
        BeanUtils.copyProperties(userDTO,userPO);
        if (userService.updateById(userPO)) {
            return Result.success();
        } else {
            return Result.fail(ErrorCode.SYSTEM_ERROR);
        }

    }


    @DeleteMapping("/{id}")
    public Result delete(@NotNull @PathVariable("id") Long id) {

        if (userService.removeById(id)) {
            return Result.success();
        } else {
            return Result.fail(ErrorCode.SYSTEM_ERROR);
        }

    }


    @Cacheable(cacheNames = "users-cache")
    @GetMapping
    public Result<PageResult> query(Integer pageNo, Integer pageSize, UserDTO userDTO) {
        if (logger.isInfoEnabled()) {
            logger.info("未使用缓存！");
        }
        // 构造查询条件
        PageQuery<UserDTO> pageQuery = new PageQuery<>();
        pageQuery.setPageNo(pageNo);
        pageQuery.setPageSize(pageSize);
        pageQuery.setQuery(userDTO);

        // 查询
        PageResult<List<UserDTO>> pageResult =
                userService.query(pageQuery);

        // 实体转换
        List<UserVO> userVOList = Optional.ofNullable(pageResult.getData())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(ele -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(ele, userVO);
                    // 对特殊字段做处理
                    userVO.setPassword("******");
                    return userVO;
                })
                .collect(Collectors.toList());

        // 封装返回结果
        PageResult<List<UserVO>> result = new PageResult<>();
        BeanUtils.copyProperties(pageResult, result);
        result.setData(userVOList);

        return Result.success(result);
    }
}