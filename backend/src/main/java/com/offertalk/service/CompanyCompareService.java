package com.offertalk.service;

import com.offertalk.controller.dto.request.CompanyCompareRequest;
import com.offertalk.controller.dto.response.CompanyCompareResponse;

import java.util.List;

/**
 * 公司对比服务接口
 */
public interface CompanyCompareService {
    
    /**
     * 对比公司
     */
    CompanyCompareResponse compare(CompanyCompareRequest request);
    
    /**
     * 搜索公司（用于选择对比对象）
     */
    List<CompanyCompareResponse.CompanyCompareItem> searchCompanies(String keyword);
    
    /**
     * 获取公司对比信息
     */
    CompanyCompareResponse.CompanyCompareItem getCompanyCompareInfo(Long companyId);
}