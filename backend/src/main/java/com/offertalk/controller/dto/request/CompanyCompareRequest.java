package com.offertalk.controller.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 公司对比请求DTO
 */
@Data
public class CompanyCompareRequest {
    
    /**
     * 公司ID列表（2-3个）
     */
    private List<Long> companyIds;
    
    /**
     * 对比维度权重
     */
    private DimensionWeights weights;
    
    @Data
    public static class DimensionWeights {
        private Double salaryWeight = 0.3;
        private Double cultureWeight = 0.2;
        private Double workLifeWeight = 0.2;
        private Double developmentWeight = 0.2;
        private Double welfareWeight = 0.1;
    }
}