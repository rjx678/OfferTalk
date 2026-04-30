package com.offertalk.controller.dto.request;

import lombok.Data;

/**
 * 意见反馈创建请求DTO
 */
@Data
public class FeedbackCreateRequest {
    
    /**
     * 反馈类型（1-问题反馈，2-功能建议，3-Bug报告）
     */
    private Integer type;
    
    /**
     * 反馈标题
     */
    private String title;
    
    /**
     * 反馈内容
     */
    private String content;
    
    /**
     * 图片URL列表
     */
    private java.util.List<String> images;
    
    /**
     * 联系方式（邮箱/手机号）
     */
    private String contact;
    
    /**
     * 用户ID
     */
    private Long userId;
}