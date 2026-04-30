package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offertalk.common.Constants;
import com.offertalk.entity.Company;
import com.offertalk.entity.SalaryDisclosure;
import com.offertalk.entity.SysUser;
import com.offertalk.mapper.SalaryDisclosureMapper;
import com.offertalk.service.CompanyService;
import com.offertalk.service.SalaryDisclosureService;
import com.offertalk.service.SensitiveWordService;
import com.offertalk.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SalaryDisclosureServiceImpl extends ServiceImpl<SalaryDisclosureMapper, SalaryDisclosure>
        implements SalaryDisclosureService {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Page<SalaryDisclosure> getSalaryPage(Integer companyId, Integer recruitType, String sortBy, String keyword, Integer page, Integer size) {
        Page<SalaryDisclosure> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SalaryDisclosure> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SalaryDisclosure::getAuditStatus, Constants.AUDIT_STATUS_PASS)
                .eq(SalaryDisclosure::getIsDeleted, Constants.DELETED_NO);

        if (recruitType != null && recruitType > 0) {
            wrapper.eq(SalaryDisclosure::getRecruitType, recruitType);
        }

        if (companyId != null && companyId > 0) {
            wrapper.eq(SalaryDisclosure::getCompanyId, companyId);
        }

        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(SalaryDisclosure::getTitle, keyword)
                    .or()
                    .like(SalaryDisclosure::getAddText, keyword));
        }

        switch (sortBy != null ? sortBy : Constants.SORT_BY_SALARY) {
            case Constants.SORT_BY_HOT:
                wrapper.orderByDesc(SalaryDisclosure::getLikeCount)
                        .orderByDesc(SalaryDisclosure::getViewCount);
                break;
            case Constants.SORT_BY_TRUTH:
                wrapper.orderByDesc(SalaryDisclosure::getTruthScore);
                break;
            case Constants.SORT_BY_NEW:
                wrapper.orderByDesc(SalaryDisclosure::getCreateTime);
                break;
            case Constants.SORT_BY_SALARY:
            default:
                wrapper.orderByDesc(SalaryDisclosure::getTotalPackage);
                break;
        }

        Page<SalaryDisclosure> result = this.page(pageParam, wrapper);

        // 添加公司信息
        result.getRecords().forEach(salary -> {
            addCompanyInfo(salary);
        });

        return result;
    }

    @Override
    public SalaryDisclosure getSalaryDetail(Long id) {
        SalaryDisclosure salary = this.getById(id);
        if (salary == null) {
            throw new com.offertalk.common.BusinessException(404, "薪资爆料不存在");
        }
        if (salary.getIsDeleted() == Constants.DELETED_YES) {
            throw new com.offertalk.common.BusinessException(404, "薪资爆料已被删除");
        }

        addCompanyInfo(salary);
        return salary;
    }

    private void addCompanyInfo(SalaryDisclosure salary) {
        if (salary.getCompanyId() != null) {
            try {
                Company company = companyService.getCompanyDetail(salary.getCompanyId());
                if (company != null) {
                    // 动态添加公司信息
                    salary.setCompanyName(company.getName());
                    salary.setCompanyLogo(company.getLogoUrl());
                }
            } catch (Exception e) {
                // 忽略异常，保持原有逻辑
            }
        }
    }

    @Override
    @Transactional
    public Long createSalary(SalaryDisclosure salary) {
        if (sensitiveWordService.shouldBlock(salary.getTitle())) {
            throw new BusinessException("标题包含敏感内容，请修改后重试");
        }
        if (sensitiveWordService.shouldBlock(salary.getAddText())) {
            throw new BusinessException("内容包含敏感内容，请修改后重试");
        }

        salary.setTitle(sensitiveWordService.filterSensitiveWords(salary.getTitle()));
        salary.setAddText(sensitiveWordService.filterSensitiveWords(salary.getAddText()));
        salary.setAuditStatus(Constants.AUDIT_STATUS_PASS);
        salary.setTruthScore(BigDecimal.valueOf(5.0));
        salary.setTruthScoreCount(0);
        salary.setViewCount(0);
        salary.setLikeCount(0);
        salary.setCollectCount(0);
        salary.setCommentCount(0);
        salary.setReportCount(0);
        salary.setIsDeleted(Constants.DELETED_NO);

        if (salary.getIsAnonymous() == null) {
            salary.setIsAnonymous(1);
        }
        if (salary.getUserId() == null) {
            salary.setUserId(Constants.ANONYMOUS_USER_ID);
        }

        this.save(salary);

        // 更新用户发布计数
        if (salary.getUserId() != null && salary.getUserId() > 0) {
            SysUser user = sysUserService.getById(salary.getUserId());
            if (user != null) {
                user.setTotalPostCount(user.getTotalPostCount() + 1);
                sysUserService.updateById(user);
            }
        }

        return salary.getId();
    }

    @Override
    public void incrementViewCount(Long id) {
        SalaryDisclosure salary = this.getById(id);
        if (salary != null) {
            salary.setViewCount(salary.getViewCount() + 1);
            this.updateById(salary);
        }
    }

    @Override
    public void updateLikeCount(Long id, Integer delta) {
        SalaryDisclosure salary = this.getById(id);
        if (salary != null) {
            salary.setLikeCount(salary.getLikeCount() + delta);
            this.updateById(salary);
        }
    }

    @Override
    public void updateCommentCount(Long id, Integer delta) {
        SalaryDisclosure salary = this.getById(id);
        if (salary != null) {
            salary.setCommentCount(salary.getCommentCount() + delta);
            this.updateById(salary);
        }
    }

    @Override
    public void updateTruthScore(Long id, Double score) {
        SalaryDisclosure salary = this.getById(id);
        if (salary != null) {
            int count = salary.getTruthScoreCount();
            double currentScore = salary.getTruthScore().doubleValue();
            double newScore = (currentScore * count + score) / (count + 1);
            salary.setTruthScore(BigDecimal.valueOf(newScore));
            salary.setTruthScoreCount(count + 1);
            this.updateById(salary);
        }
    }

    @Override
    public List<SalaryDisclosure> getTopSalaries(Integer limit) {
        LambdaQueryWrapper<SalaryDisclosure> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryDisclosure::getAuditStatus, Constants.AUDIT_STATUS_PASS)
                .eq(SalaryDisclosure::getIsDeleted, Constants.DELETED_NO)
                .orderByDesc(SalaryDisclosure::getTotalPackage)
                .last("LIMIT " + limit);

        List<SalaryDisclosure> salaries = this.list(wrapper);
        salaries.forEach(this::addCompanyInfo);
        return salaries;
    }

    private static class BusinessException extends com.offertalk.common.BusinessException {
        public BusinessException(String message) {
            super(message);
        }
    }
}