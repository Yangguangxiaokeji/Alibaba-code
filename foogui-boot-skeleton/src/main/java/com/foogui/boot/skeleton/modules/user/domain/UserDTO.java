package com.foogui.boot.skeleton.modules.user.domain;

import com.foogui.boot.skeleton.common.valid.InsertValidationGroup;
import com.foogui.boot.skeleton.common.valid.UpdateValidationGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO implements Serializable {


    private static final long serialVersionUID = 6728497964686236776L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空!", groups = InsertValidationGroup.class)
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空!", groups = InsertValidationGroup.class)
    @Length(min = 6, max = 20, message = "密码长度不能少于6位，不能多于20位")
    private String password;



    /**
     * 用户性别（0男 1女 2未知）
     */
    @NotEmpty(message = "性别不能为空！", groups = InsertValidationGroup.class)
    private String sex;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空！", groups = {InsertValidationGroup.class})
    @Max(value = 60, message = "年龄不能大于60岁！")
    @Min(value = 18, message = "年龄不能小于18岁！")
    private Integer age;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空！", groups = {InsertValidationGroup.class})
    private String phone;

    /**
     * 版本号
     */
    @NotNull(message = "版本号不能为空！", groups = {UpdateValidationGroup.class})
    private Long version;

    /**
     * 创建时间
     */
    private LocalDateTime created;



}
