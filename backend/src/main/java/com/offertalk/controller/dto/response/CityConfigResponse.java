package com.offertalk.controller.dto.response;

import lombok.Data;

/**
 * 城市社保配置响应DTO
 */
@Data
public class CityConfigResponse {
    
    /**
     * 城市编码
     */
    private String cityCode;
    
    /**
     * 城市名称
     */
    private String cityName;
    
    /**
     * 社保基数下限（元）
     */
    private Integer socialInsuranceMin;
    
    /**
     * 社保基数上限（元）
     */
    private Integer socialInsuranceMax;
    
    /**
     * 公积金基数下限（元）
     */
    private Integer housingFundMin;
    
    /**
     * 公积金基数上限（元）
     */
    private Integer housingFundMax;
    
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
}