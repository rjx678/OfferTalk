package com.offertalk.controller.dto.response;

import com.offertalk.controller.dto.request.CompanyCompareRequest;
import lombok.Data;

import java.util.List;

/**
 * 公司对比响应DTO
 */
@Data
public class CompanyCompareResponse {
    
    /**
     * 对比结果列表
     */
    private List<CompanyCompareItem> items;
    
    /**
     * 最优公司ID（综合评分最高）
     */
    private Long bestCompanyId;
    
    /**
     * 对比维度权重
     */
    private CompanyCompareRequest.DimensionWeights weights;
    
    @Data
    public static class CompanyCompareItem {
        private Long companyId;
        private String companyName;
        private String logoUrl;
        private String industry;
        private String stage;
        
        // 评分数据
        private Double overallScore;
        private Double salaryScore;
        private Double cultureScore;
        private Double workLifeScore;
        private Double developmentScore;
        private Double welfareScore;
        
        // 薪资数据
        private String salaryRange;
        private Integer avgMonthlySalary;
        private Integer avgAnnualBonus;
        
        // 评价数据
        private Integer reviewCount;
        private String prosSummary;
        private String consSummary;
        
        // 标签
        private List<String> tags;
    }
}