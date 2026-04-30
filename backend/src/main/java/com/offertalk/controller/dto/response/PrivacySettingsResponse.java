package com.offertalk.controller.dto.response;

import lombok.Data;

@Data
public class PrivacySettingsResponse {
    private Boolean anonymousDefault;
    private Boolean showCollections;
    private Boolean showPosts;
    private Boolean personalizedRecommend;
}
