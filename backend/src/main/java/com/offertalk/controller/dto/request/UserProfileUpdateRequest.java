package com.offertalk.controller.dto.request;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String bio;
}
