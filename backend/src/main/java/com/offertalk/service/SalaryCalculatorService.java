package com.offertalk.service;

import com.offertalk.controller.dto.request.SalaryCalculateRequest;
import com.offertalk.controller.dto.response.CityConfigResponse;
import com.offertalk.controller.dto.response.SalaryCalculateResponse;

import java.util.List;

/**
 * 薪资计算器服务接口
 */
public interface SalaryCalculatorService {
    
    /**
     * 计算薪资
     */
    SalaryCalculateResponse calculate(SalaryCalculateRequest request);
    
    /**
     * 获取所有城市配置
     */
    List<CityConfigResponse> getAllCityConfigs();
    
    /**
     * 获取单个城市配置
     */
    CityConfigResponse getCityConfig(String cityCode);
}