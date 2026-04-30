package com.offertalk.controller.dto.request;

import lombok.Data;

@Data
public class NotificationSettingsRequest {
    private Long userId;
    private Boolean commentNotify;
    private Boolean likeNotify;
    private Boolean atNotify;
    private Boolean systemNotify;
    private Boolean weeklyReport;
}
