package com.offertalk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.offertalk.entity.Company;

import java.util.List;

public interface CompanyService extends IService<Company> {
    Page<Company> getCompanyPage(String keyword, String industry, String city, Integer page, Integer size);
    List<Company> searchCompanies(String keyword);
    Company getCompanyDetail(Long id);
    void incrementViewCount(Long id);
    void updateCompanyStats(Long companyId);
}
