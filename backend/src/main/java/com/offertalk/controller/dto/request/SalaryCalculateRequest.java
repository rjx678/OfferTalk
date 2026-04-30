package com.offertalk.controller.dto.request;

import lombok.Data;

/**
 * 薪资计算请求DTO
 */
@Data
public class SalaryCalculateRequest {
    
    /**
     * 月薪（元）
     */
    private Integer monthlyBase;
    
    /**
     * 年终奖（元）
     */
    private Integer annualBonus;
    
    /**
     * 年薪形式（13/14/15/16薪）
     */
    private Integer salaryMonths;
    
    /**
     * RSU数量
     */
    private Integer rsuCount;
    
    /**
     * RSU单价（元）
     */
    private Integer rsuPrice;
    
    /**
     * 城市编码
     */
    private String cityCode;
    
    /**
     * 养老保险比例（%）
     */
    private Double pensionRatio;
    
    /**
     * 医疗保险比例（%）
     */
    private Double medicalRatio;
    
    /**
     * 失业保险比例（%）
     */
    private Double unemploymentRatio;
    
    /**
     * 公积金比例（%）
     */
    private Double housingFundRatio;
    
    /**
     * 计算类型（1-税前算税后，2-税后反推税前）
     */
    private Integer calculateType = 1;
}