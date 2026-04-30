package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.offertalk.controller.dto.request.NotificationSettingsRequest;
import com.offertalk.controller.dto.request.PrivacySettingsRequest;
import com.offertalk.controller.dto.request.UserProfileUpdateRequest;
import com.offertalk.controller.dto.response.AppVersionResponse;
import com.offertalk.controller.dto.response.NotificationSettingsResponse;
import com.offertalk.controller.dto.response.PrivacySettingsResponse;
import com.offertalk.controller.dto.response.UserProfileResponse;
import com.offertalk.entity.SysUser;
import com.offertalk.entity.UserSettings;
import com.offertalk.mapper.UserSettingsMapper;
import com.offertalk.service.UserSettingsService;
import com.offertalk.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsServiceImpl implements UserSettingsService {

    @Autowired
    private UserSettingsMapper userSettingsMapper;

    @Autowired
    private SysUserService sysUserService;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.version.name:1.0.0}")
    private String appVersionName;

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            return new UserProfileResponse();
        }

        UserProfileResponse response = new UserProfileResponse();
        response.setUserId(user.getId());
        response.setNickname(user.getNickname());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setPhone(user.getPhone());
        response.setBio(getOrCreateUserSettings(userId).getBio());
        response.setTotalPostCount(user.getTotalPostCount());
        response.setTotalLikeCount(user.getTotalLikeCount());

        return response;
    }

    @Override
    public void updateUserProfile(UserProfileUpdateRequest request) {
        SysUser user = sysUserService.getById(request.getUserId());
        if (user == null) {
            return;
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
            UserSettings settings = getOrCreateUserSettings(request.getUserId());
            settings.setBio(request.getBio());
            userSettingsMapper.updateById(settings);
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        sysUserService.updateById(user);
    }

    @Override
    public PrivacySettingsResponse getPrivacySettings(Long userId) {
        UserSettings settings = getOrCreateUserSettings(userId);
        PrivacySettingsResponse response = new PrivacySettingsResponse();
        response.setAnonymousDefault(settings.getAnonymousDefault());
        response.setShowCollections(settings.getShowCollections());
        response.setShowPosts(settings.getShowPosts());
        response.setPersonalizedRecommend(settings.getPersonalizedRecommend());
        return response;
    }

    @Override
    public void updatePrivacySettings(PrivacySettingsRequest request) {
        UserSettings settings = getOrCreateUserSettings(request.getUserId());

        if (request.getAnonymousDefault() != null) {
            settings.setAnonymousDefault(request.getAnonymousDefault());
        }
        if (request.getShowCollections() != null) {
            settings.setShowCollections(request.getShowCollections());
        }
        if (request.getShowPosts() != null) {
            settings.setShowPosts(request.getShowPosts());
        }
        if (request.getPersonalizedRecommend() != null) {
            settings.setPersonalizedRecommend(request.getPersonalizedRecommend());
        }

        userSettingsMapper.updateById(settings);
    }

    @Override
    public NotificationSettingsResponse getNotificationSettings(Long userId) {
        UserSettings settings = getOrCreateUserSettings(userId);
        NotificationSettingsResponse response = new NotificationSettingsResponse();
        response.setCommentNotify(settings.getCommentNotify());
        response.setLikeNotify(settings.getLikeNotify());
        response.setAtNotify(settings.getAtNotify());
        response.setSystemNotify(settings.getSystemNotify());
        response.setWeeklyReport(settings.getWeeklyReport());
        return response;
    }

    @Override
    public void updateNotificationSettings(NotificationSettingsRequest request) {
        UserSettings settings = getOrCreateUserSettings(request.getUserId());

        if (request.getCommentNotify() != null) {
            settings.setCommentNotify(request.getCommentNotify());
        }
        if (request.getLikeNotify() != null) {
            settings.setLikeNotify(request.getLikeNotify());
        }
        if (request.getAtNotify() != null) {
            settings.setAtNotify(request.getAtNotify());
        }
        if (request.getSystemNotify() != null) {
            settings.setSystemNotify(request.getSystemNotify());
        }
        if (request.getWeeklyReport() != null) {
            settings.setWeeklyReport(request.getWeeklyReport());
        }

        userSettingsMapper.updateById(settings);
    }

    @Override
    public AppVersionResponse getAppVersion() {
        AppVersionResponse response = new AppVersionResponse();
        response.setVersion(appVersion);
        response.setVersionName(appVersionName);
        response.setHasUpdate(false);
        response.setUpdateContent("当前已是最新版本");
        response.setDownloadUrl("");
        return response;
    }

    private UserSettings getOrCreateUserSettings(Long userId) {
        LambdaQueryWrapper<UserSettings> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserSettings::getUserId, userId);
        UserSettings settings = userSettingsMapper.selectOne(wrapper);

        if (settings == null) {
            settings = new UserSettings();
            settings.setUserId(userId);
            settings.setAnonymousDefault(true);
            settings.setShowCollections(true);
            settings.setShowPosts(true);
            settings.setPersonalizedRecommend(true);
            settings.setCommentNotify(true);
            settings.setLikeNotify(true);
            settings.setAtNotify(true);
            settings.setSystemNotify(true);
            settings.setWeeklyReport(false);
            settings.setBio("");
            userSettingsMapper.insert(settings);
        }

        return settings;
    }
}