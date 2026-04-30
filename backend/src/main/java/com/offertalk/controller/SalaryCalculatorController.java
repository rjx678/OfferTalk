package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import com.offertalk.controller.dto.request.SalaryCalculateRequest;

import com.offertalk.controller.dto.response.CityConfigResponse;
import com.offertalk.controller.dto.response.SalaryCalculateResponse;
import com.offertalk.service.SalaryCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 薪资计算器控制器
 */
@RestController
@RequestMapping("/salary-calculator")
public class SalaryCalculatorController {
    
    @Autowired
    private SalaryCalculatorService salaryCalculatorService;
    
    /**
     * 计算薪资
     */
    @PostMapping("/calculate")
    public ApiResponse<SalaryCalculateResponse> calculate(@RequestBody SalaryCalculateRequest request) {
        try {
            SalaryCalculateResponse response = salaryCalculatorService.calculate(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("计算失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有城市配置
     */
    @GetMapping("/cities")
    public ApiResponse<List<CityConfigResponse>> getAllCities() {
        try {
            List<CityConfigResponse> cities = salaryCalculatorService.getAllCityConfigs();
            return ApiResponse.success(cities);
        } catch (Exception e) {
            return ApiResponse.error("获取城市配置失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取单个城市配置
     */
    @GetMapping("/cities/{cityCode}")
    public ApiResponse<CityConfigResponse> getCityConfig(@PathVariable String cityCode) {
        try {
            CityConfigResponse config = salaryCalculatorService.getCityConfig(cityCode);
            return ApiResponse.success(config);
        } catch (Exception e) {
            return ApiResponse.error("获取城市配置失败：" + e.getMessage());
        }
    }
}