package com.offertalk.controller.dto.request;

import lombok.Data;

/**
 * 职业路径规划请求DTO
 */
@Data
public class CareerPathRequest {
    
    /**
     * 当前职位ID
     */
    private String currentPositionId;
    
    /**
     * 目标职位ID
     */
    private String targetPositionId;
    
    /**
     * 规划时间（月）
     */
    private Integer planMonths;
}