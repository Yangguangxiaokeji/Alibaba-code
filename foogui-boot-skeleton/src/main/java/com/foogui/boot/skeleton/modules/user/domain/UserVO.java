package com.foogui.boot.skeleton.modules.user.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class UserVO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 384822012852377022L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号
     */
    private String phone;

}
