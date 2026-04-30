package com.offertalk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.common.ApiResponse;
import com.offertalk.common.PageResult;
import com.offertalk.entity.Company;
import com.offertalk.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/list")
    public ApiResponse<PageResult<List<Company>>> getCompanyList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<Company> pageResult = companyService.getCompanyPage(keyword, industry, city, page, size);
        PageResult<List<Company>> result = PageResult.of(
                pageResult.getTotal(),
                pageResult.getCurrent(),
                pageResult.getSize(),
                pageResult.getRecords()
        );
        return ApiResponse.success(result);
    }

    @GetMapping("/search")
    public ApiResponse<List<Company>> searchCompanies(@RequestParam String keyword) {
        List<Company> companies = companyService.searchCompanies(keyword);
        return ApiResponse.success(companies);
    }

    @GetMapping("/detail/{id}")
    public ApiResponse<Company> getCompanyDetail(@PathVariable Long id) {
        Company company = companyService.getCompanyDetail(id);
        companyService.incrementViewCount(id);
        return ApiResponse.success(company);
    }
}
