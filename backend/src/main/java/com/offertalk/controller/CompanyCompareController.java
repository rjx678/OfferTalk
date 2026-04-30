package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import com.offertalk.controller.dto.request.CompanyCompareRequest;

import com.offertalk.controller.dto.response.CompanyCompareResponse;
import com.offertalk.service.CompanyCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公司对比控制器
 */
@RestController
@RequestMapping("/company-compare")
public class CompanyCompareController {
    
    @Autowired
    private CompanyCompareService companyCompareService;
    
    /**
     * 对比公司
     */
    @PostMapping("/compare")
    public ApiResponse<CompanyCompareResponse> compare(@RequestBody CompanyCompareRequest request) {
        try {
            CompanyCompareResponse response = companyCompareService.compare(request);
            return ApiResponse.success(response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("对比失败：" + e.getMessage());
        }
    }
    
    /**
     * 搜索公司
     */
    @GetMapping("/search")
    public ApiResponse<List<CompanyCompareResponse.CompanyCompareItem>> searchCompanies(
            @RequestParam(required = false) String keyword) {
        try {
            List<CompanyCompareResponse.CompanyCompareItem> companies = companyCompareService.searchCompanies(keyword);
            return ApiResponse.success(companies);
        } catch (Exception e) {
            return ApiResponse.error("搜索失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取公司对比信息
     */
    @GetMapping("/{companyId}")
    public ApiResponse<CompanyCompareResponse.CompanyCompareItem> getCompanyCompareInfo(@PathVariable Long companyId) {
        try {
            CompanyCompareResponse.CompanyCompareItem item = companyCompareService.getCompanyCompareInfo(companyId);
            return ApiResponse.success(item);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("获取公司信息失败：" + e.getMessage());
        }
    }
}