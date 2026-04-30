package com.offertalk.service;

import com.offertalk.controller.dto.request.NotificationSettingsRequest;
import com.offertalk.controller.dto.request.PrivacySettingsRequest;
import com.offertalk.controller.dto.request.UserProfileUpdateRequest;
import com.offertalk.controller.dto.response.AppVersionResponse;
import com.offertalk.controller.dto.response.NotificationSettingsResponse;
import com.offertalk.controller.dto.response.PrivacySettingsResponse;
import com.offertalk.controller.dto.response.UserProfileResponse;

public interface UserSettingsService {

    UserProfileResponse getUserProfile(Long userId);

    void updateUserProfile(UserProfileUpdateRequest request);

    PrivacySettingsResponse getPrivacySettings(Long userId);

    void updatePrivacySettings(PrivacySettingsRequest request);

    NotificationSettingsResponse getNotificationSettings(Long userId);

    void updateNotificationSettings(NotificationSettingsRequest request);

    AppVersionResponse getAppVersion();
}
