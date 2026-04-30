package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offertalk.common.BusinessException;
import com.offertalk.entity.Company;
import com.offertalk.mapper.CompanyMapper;
import com.offertalk.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Override
    public Page<Company> getCompanyPage(String keyword, String industry, String city, Integer page, Integer size) {
        Page<Company> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Company::getStatus, 1);

        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(Company::getName, keyword)
                    .or()
                    .like(Company::getShortName, keyword));
        }
        if (StringUtils.isNotBlank(industry)) {
            wrapper.eq(Company::getIndustry, industry);
        }
        if (StringUtils.isNotBlank(city)) {
            wrapper.eq(Company::getCity, city);
        }

        wrapper.orderByDesc(Company::getSortOrder)
                .orderByDesc(Company::getRatingTotal);

        Page<Company> result = this.page(pageParam, wrapper);
        return result;
    }

    @Override
    public List<Company> searchCompanies(String keyword) {
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Company::getStatus, 1);
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(Company::getName, keyword)
                    .or()
                    .like(Company::getShortName, keyword));
        }
        wrapper.orderByDesc(Company::getRatingTotal).last("LIMIT 20");
        return this.list(wrapper);
    }

    @Override
    public Company getCompanyDetail(Long id) {
        Company company = this.getById(id);
        if (company == null || company.getStatus() == 0) {
            throw new BusinessException(404, "公司不存在");
        }
        return company;
    }

    @Override
    public void incrementViewCount(Long id) {
        Company company = this.getById(id);
        if (company != null) {
            company.setViewCount(company.getViewCount() + 1);
            this.updateById(company);
        }
    }

    @Override
    public void updateCompanyStats(Long companyId) {
        Company company = this.getById(companyId);
        if (company != null) {
            this.baseMapper.selectCount(null);
        }
    }
}
