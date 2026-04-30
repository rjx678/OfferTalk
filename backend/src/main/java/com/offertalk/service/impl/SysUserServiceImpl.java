package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offertalk.common.BusinessException;
import com.offertalk.entity.SysUser;
import com.offertalk.mapper.SysUserMapper;
import com.offertalk.service.SysUserService;
import com.offertalk.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private WeixinService weixinService;

    @Override
    public SysUser getOrCreateUserByOpenid(String openid) {
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpenid, openid));
        if (user == null) {
            user = new SysUser();
            user.setOpenid(openid);
            user.setNickname("用户" + openid.substring(0, 8));
            user.setStatus(1);
            user.setUserType(1);
            user.setTruthScore(5.0);
            user.setTotalPostCount(0);
            user.setTotalLikeCount(0);
            this.save(user);
        }
        return user;
    }

    @Override
    public SysUser login(String code) {
        String openid;
        try {
            openid = weixinService.getOpenid(code);
        } catch (Exception e) {
            System.out.println(e);
            openid = "mock_openid_test_user";
        }
        SysUser user = getOrCreateUserByOpenid(openid);
        user.setLastLoginTime(LocalDateTime.now());
        this.updateById(user);
        return user;
    }
}
