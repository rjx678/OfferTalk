package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import com.offertalk.controller.dto.request.UserProfileUpdateRequest;
import com.offertalk.controller.dto.request.PrivacySettingsRequest;
import com.offertalk.controller.dto.request.NotificationSettingsRequest;
import com.offertalk.controller.dto.response.AppVersionResponse;
import com.offertalk.controller.dto.response.PrivacySettingsResponse;
import com.offertalk.controller.dto.response.NotificationSettingsResponse;
import com.offertalk.controller.dto.response.UserProfileResponse;
import com.offertalk.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/settings")
public class UserSettingsController {

    @Autowired
    private UserSettingsService userSettingsService;

    @GetMapping("/profile")
    public ApiResponse<UserProfileResponse> getUserProfile(@RequestParam Long userId) {
        UserProfileResponse profile = userSettingsService.getUserProfile(userId);
        return ApiResponse.success(profile);
    }

    @PostMapping("/profile/update")
    public ApiResponse<Void> updateUserProfile(@RequestBody UserProfileUpdateRequest request) {
        userSettingsService.updateUserProfile(request);
        return ApiResponse.success();
    }

    @GetMapping("/privacy")
    public ApiResponse<PrivacySettingsResponse> getPrivacySettings(@RequestParam Long userId) {
        PrivacySettingsResponse settings = userSettingsService.getPrivacySettings(userId);
        return ApiResponse.success(settings);
    }

    @PostMapping("/privacy/update")
    public ApiResponse<Void> updatePrivacySettings(@RequestBody PrivacySettingsRequest request) {
        userSettingsService.updatePrivacySettings(request);
        return ApiResponse.success();
    }

    @GetMapping("/notification")
    public ApiResponse<NotificationSettingsResponse> getNotificationSettings(@RequestParam Long userId) {
        NotificationSettingsResponse settings = userSettingsService.getNotificationSettings(userId);
        return ApiResponse.success(settings);
    }

    @PostMapping("/notification/update")
    public ApiResponse<Void> updateNotificationSettings(@RequestBody NotificationSettingsRequest request) {
        userSettingsService.updateNotificationSettings(request);
        return ApiResponse.success();
    }

    @GetMapping("/app/version")
    public ApiResponse<AppVersionResponse> getAppVersion() {
        AppVersionResponse version = userSettingsService.getAppVersion();
        return ApiResponse.success(version);
    }
}
