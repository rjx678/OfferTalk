package com.offertalk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.offertalk.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    SysUser getOrCreateUserByOpenid(String openid);
    SysUser login(String code);
}
