package com.offertalk.controller.dto.response;

import lombok.Data;

@Data
public class UserProfileResponse {
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String bio;
    private Integer totalPostCount;
    private Integer totalLikeCount;
    private Integer totalCollectCount;
    private Integer totalCommentCount;
}
