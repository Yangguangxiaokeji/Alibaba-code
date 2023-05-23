package com.foogui.boot.skeleton.modules.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.foogui.boot.skeleton.common.enums.SexEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Foogui
 * @since 2023-05-22
 */
@TableName("t_user")
@Getter
@Setter
public class UserPO implements Serializable {


    private static final long serialVersionUID = 8544009670943015098L;
    /**
     * 数据表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField(value = "sex")
    private SexEnum sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime created;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modified;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 0:数据有效   1:删除状态
     */
    @TableLogic
    private Integer deleted;

    /**
     * 数据版本号
     */
    @Version
    private Long version;


}
