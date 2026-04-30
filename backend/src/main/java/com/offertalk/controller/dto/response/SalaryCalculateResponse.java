package com.offertalk.controller.dto.response;

import lombok.Data;

/**
 * 薪资计算响应DTO
 */
@Data
public class SalaryCalculateResponse {
    
    /**
     * 税前年薪（元）
     */
    private Integer grossAnnualSalary;
    
    /**
     * 税后年薪（元）
     */
    private Integer netAnnualSalary;
    
    /**
     * 税前月薪（元）
     */
    private Integer grossMonthlySalary;
    
    /**
     * 税后月薪（元）
     */
    private Integer netMonthlySalary;
    
    /**
     * 年度个税（元）
     */
    private Integer annualTax;
    
    /**
     * 年度社保个人缴纳（元）
     */
    private Integer annualSocialInsurance;
    
    /**
     * 年度公积金个人缴纳（元）
     */
    private Integer annualHousingFund;
    
    /**
     * RSU价值（元）
     */
    private Integer rsuValue;
    
    /**
     * 月度明细
     */
    private MonthlyDetail monthlyDetail;
    
    /**
     * 构成比例
     */
    private CompositionRatio composition;
    
    @Data
    public static class MonthlyDetail {
        private Integer grossSalary;
        private Integer pension;
        private Integer medical;
        private Integer unemployment;
        private Integer housingFund;
        private Integer tax;
        private Integer netSalary;
    }
    
    @Data
    public static class CompositionRatio {
        private Double baseRatio;
        private Double bonusRatio;
        private Double rsuRatio;
        private Double socialInsuranceRatio;
        private Double housingFundRatio;
        private Double taxRatio;
    }
}