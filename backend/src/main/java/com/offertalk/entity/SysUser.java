package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String openid;
    private String unionid;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String bio;
    private Integer status;
    private Integer userType;
    private Double truthScore;
    private Integer totalPostCount;
    private Integer totalLikeCount;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
}
