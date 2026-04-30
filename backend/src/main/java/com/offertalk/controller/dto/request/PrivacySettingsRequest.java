package com.offertalk.controller.dto.request;

import lombok.Data;

@Data
public class PrivacySettingsRequest {
    private Long userId;
    private Boolean anonymousDefault;
    private Boolean showCollections;
    private Boolean showPosts;
    private Boolean personalizedRecommend;
}
