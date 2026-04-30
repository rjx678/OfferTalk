package com.offertalk.service.impl;

import com.offertalk.controller.dto.request.SalaryCalculateRequest;
import com.offertalk.controller.dto.response.CityConfigResponse;
import com.offertalk.controller.dto.response.SalaryCalculateResponse;
import com.offertalk.service.SalaryCalculatorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 薪资计算器服务实现
 */
@Service
public class SalaryCalculatorServiceImpl implements SalaryCalculatorService {
    
    /**
     * 城市社保配置数据（模拟数据）
     */
    private static final Map<String, CityConfigResponse> CITY_CONFIGS = new HashMap<>();
    
    static {
        // 北京
        CITY_CONFIGS.put("BJ", createCityConfig("BJ", "北京", 6326, 31884, 2320, 31884, 8.0, 2.0, 0.5, 12.0));
        // 上海
        CITY_CONFIGS.put("SH", createCityConfig("SH", "上海", 7310, 36549, 2590, 36549, 8.0, 2.0, 0.5, 7.0));
        // 深圳
        CITY_CONFIGS.put("SZ", createCityConfig("SZ", "深圳", 2360, 36042, 2360, 36042, 8.0, 2.0, 0.3, 5.0));
        // 杭州
        CITY_CONFIGS.put("HZ", createCityConfig("HZ", "杭州", 4310, 22311, 2280, 22311, 8.0, 2.0, 0.5, 12.0));
        // 广州
        CITY_CONFIGS.put("GZ", createCityConfig("GZ", "广州", 2300, 30876, 2300, 30876, 8.0, 2.0, 0.2, 5.0));
        // 成都
        CITY_CONFIGS.put("CD", createCityConfig("CD", "成都", 2463, 19389, 2463, 19389, 8.0, 2.0, 0.4, 6.0));
        // 武汉
        CITY_CONFIGS.put("WH", createCityConfig("WH", "武汉", 2700, 18699, 2700, 18699, 8.0, 2.0, 0.5, 8.0));
        // 西安
        CITY_CONFIGS.put("XA", createCityConfig("XA", "西安", 2150, 18159, 1950, 18159, 8.0, 2.0, 0.5, 5.0));
    }
    
    private static CityConfigResponse createCityConfig(String cityCode, String cityName,
                                                      int socialInsuranceMin, int socialInsuranceMax,
                                                      int housingFundMin, int housingFundMax,
                                                      double pensionRatio, double medicalRatio,
                                                      double unemploymentRatio, double housingFundRatio) {
        CityConfigResponse config = new CityConfigResponse();
        config.setCityCode(cityCode);
        config.setCityName(cityName);
        config.setSocialInsuranceMin(socialInsuranceMin);
        config.setSocialInsuranceMax(socialInsuranceMax);
        config.setHousingFundMin(housingFundMin);
        config.setHousingFundMax(housingFundMax);
        config.setPensionRatio(pensionRatio);
        config.setMedicalRatio(medicalRatio);
        config.setUnemploymentRatio(unemploymentRatio);
        config.setHousingFundRatio(housingFundRatio);
        return config;
    }
    
    /**
     * 个税税率表（年度）
     */
    private static final double[][] TAX_BRACKETS = {
            {0, 36000, 0.03, 0},
            {36000, 144000, 0.10, 2520},
            {144000, 300000, 0.20, 16920},
            {300000, 420000, 0.25, 31920},
            {420000, 660000, 0.30, 52920},
            {660000, 960000, 0.35, 85920},
            {960000, Double.MAX_VALUE, 0.45, 181920}
    };
    
    @Override
    public SalaryCalculateResponse calculate(SalaryCalculateRequest request) {
        // 获取城市配置
        CityConfigResponse cityConfig = getCityConfig(request.getCityCode());
        
        // 使用自定义比例或城市默认比例
        double pensionRatio = request.getPensionRatio() != null ? request.getPensionRatio() : cityConfig.getPensionRatio();
        double medicalRatio = request.getMedicalRatio() != null ? request.getMedicalRatio() : cityConfig.getMedicalRatio();
        double unemploymentRatio = request.getUnemploymentRatio() != null ? request.getUnemploymentRatio() : cityConfig.getUnemploymentRatio();
        double housingFundRatio = request.getHousingFundRatio() != null ? request.getHousingFundRatio() : cityConfig.getHousingFundRatio();
        
        // 计算社保和公积金基数
        int monthlyBase = request.getMonthlyBase() != null ? request.getMonthlyBase() : 0;
        int socialInsuranceBase = Math.min(Math.max(monthlyBase, cityConfig.getSocialInsuranceMin()), cityConfig.getSocialInsuranceMax());
        int housingFundBase = Math.min(Math.max(monthlyBase, cityConfig.getHousingFundMin()), cityConfig.getHousingFundMax());
        
        // 计算月度社保个人缴纳
        int monthlyPension = (int) (socialInsuranceBase * pensionRatio / 100);
        int monthlyMedical = (int) (socialInsuranceBase * medicalRatio / 100);
        int monthlyUnemployment = (int) (socialInsuranceBase * unemploymentRatio / 100);
        int monthlyHousingFund = (int) (housingFundBase * housingFundRatio / 100);
        
        // 计算月度应纳税所得额
        int monthlyDeduction = monthlyPension + monthlyMedical + monthlyUnemployment + monthlyHousingFund;
        int monthlyTaxable = monthlyBase - monthlyDeduction - 5000; // 5000起征点
        
        // 计算年度应纳税所得额
        int annualBonus = request.getAnnualBonus() != null ? request.getAnnualBonus() : 0;
        int salaryMonths = request.getSalaryMonths() != null ? request.getSalaryMonths() : 12;
        int annualTaxable = Math.max(0, monthlyTaxable) * 12 + annualBonus;
        
        // 计算年度个税
        int annualTax = calculateAnnualTax(annualTaxable);
        
        // 计算月度个税（简化计算）
        int monthlyTax = annualTax / 12;
        
        // 计算税后月薪
        int monthlyNet = monthlyBase - monthlyDeduction - monthlyTax;
        
        // 计算RSU价值
        int rsuCount = request.getRsuCount() != null ? request.getRsuCount() : 0;
        int rsuPrice = request.getRsuPrice() != null ? request.getRsuPrice() : 0;
        int rsuValue = rsuCount * rsuPrice;
        
        // 计算税前年薪
        int grossAnnualSalary = monthlyBase * salaryMonths + annualBonus + rsuValue;
        
        // 计算税后年薪
        int netAnnualSalary = monthlyNet * 12 + Math.max(0, annualBonus - (annualTax - monthlyTax * 12));
        
        // 构建响应
        SalaryCalculateResponse response = new SalaryCalculateResponse();
        response.setGrossAnnualSalary(grossAnnualSalary);
        response.setNetAnnualSalary(netAnnualSalary);
        response.setGrossMonthlySalary(monthlyBase);
        response.setNetMonthlySalary(monthlyNet);
        response.setAnnualTax(annualTax);
        response.setAnnualSocialInsurance((monthlyPension + monthlyMedical + monthlyUnemployment) * 12);
        response.setAnnualHousingFund(monthlyHousingFund * 12);
        response.setRsuValue(rsuValue);
        
        // 设置月度明细
        SalaryCalculateResponse.MonthlyDetail detail = new SalaryCalculateResponse.MonthlyDetail();
        detail.setGrossSalary(monthlyBase);
        detail.setPension(monthlyPension);
        detail.setMedical(monthlyMedical);
        detail.setUnemployment(monthlyUnemployment);
        detail.setHousingFund(monthlyHousingFund);
        detail.setTax(monthlyTax);
        detail.setNetSalary(monthlyNet);
        response.setMonthlyDetail(detail);
        
        // 设置构成比例
        SalaryCalculateResponse.CompositionRatio composition = new SalaryCalculateResponse.CompositionRatio();
        if (grossAnnualSalary > 0) {
            composition.setBaseRatio(round2((monthlyBase * salaryMonths) * 100.0 / grossAnnualSalary));
            composition.setBonusRatio(round2(annualBonus * 100.0 / grossAnnualSalary));
            composition.setRsuRatio(round2(rsuValue * 100.0 / grossAnnualSalary));
            composition.setSocialInsuranceRatio(round2(response.getAnnualSocialInsurance() * 100.0 / grossAnnualSalary));
            composition.setHousingFundRatio(round2(response.getAnnualHousingFund() * 100.0 / grossAnnualSalary));
            composition.setTaxRatio(round2(annualTax * 100.0 / grossAnnualSalary));
        }
        response.setComposition(composition);
        
        return response;
    }
    
    /**
     * 计算年度个税
     */
    private int calculateAnnualTax(int taxableIncome) {
        if (taxableIncome <= 0) {
            return 0;
        }
        
        double tax = 0;
        for (double[] bracket : TAX_BRACKETS) {
            double lower = bracket[0];
            double upper = bracket[1];
            double rate = bracket[2];
            double deduction = bracket[3];
            
            if (taxableIncome > lower) {
                double taxableInBracket = Math.min(taxableIncome, upper) - lower;
                tax += taxableInBracket * rate;
            } else {
                break;
            }
        }
        
        // 减去速算扣除数
        for (int i = TAX_BRACKETS.length - 1; i >= 0; i--) {
            if (taxableIncome > TAX_BRACKETS[i][0]) {
                tax -= TAX_BRACKETS[i][3];
                break;
            }
        }
        
        return (int) Math.round(tax);
    }
    
    /**
     * 保留两位小数
     */
    private double round2(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
    @Override
    public List<CityConfigResponse> getAllCityConfigs() {
        return new ArrayList<>(CITY_CONFIGS.values());
    }
    
    @Override
    public CityConfigResponse getCityConfig(String cityCode) {
        if (cityCode == null || !CITY_CONFIGS.containsKey(cityCode)) {
            return CITY_CONFIGS.get("BJ"); // 默认返回北京配置
        }
        return CITY_CONFIGS.get(cityCode);
    }
}