package com.offertalk.controller.dto.response;

import lombok.Data;

@Data
public class NotificationSettingsResponse {
    private Boolean commentNotify;
    private Boolean likeNotify;
    private Boolean atNotify;
    private Boolean systemNotify;
    private Boolean weeklyReport;
}
