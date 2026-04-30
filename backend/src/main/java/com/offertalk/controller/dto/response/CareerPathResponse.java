package com.offertalk.controller.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 职业路径规划响应DTO
 */
@Data
public class CareerPathResponse {
    
    /**
     * 当前职位
     */
    private PositionInfo currentPosition;
    
    /**
     * 目标职位
     */
    private PositionInfo targetPosition;
    
    /**
     * 规划时间（月）
     */
    private Integer planMonths;
    
    /**
     * 发展路径阶段
     */
    private List<PathStage> stages;
    
    /**
     * 技能差距分析
     */
    private SkillGapAnalysis skillGap;
    
    /**
     * 学习资源推荐
     */
    private List<LearningResource> resources;
    
    @Data
    public static class PositionInfo {
        private String positionId;
        private String positionName;
        private String level;
        private String department;
        private Integer minYears;
        private Integer maxYears;
        private List<String> requiredSkills;
        private List<String> responsibilities;
    }
    
    @Data
    public static class PathStage {
        private Integer stageNumber;
        private String title;
        private Integer durationMonths;
        private List<String> objectives;
        private List<String> keySkills;
        private String milestone;
    }
    
    @Data
    public static class SkillGapAnalysis {
        private List<String> currentSkills;
        private List<String> targetSkills;
        private List<String> missingSkills;
        private List<String> toImproveSkills;
    }
    
    @Data
    public static class LearningResource {
        private String resourceId;
        private String title;
        private String type;
        private String url;
        private String platform;
        private Integer durationHours;
        private Double rating;
        private String description;
    }
}