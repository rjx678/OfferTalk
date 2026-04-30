package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offertalk.common.Constants;
import com.offertalk.entity.Company;
import com.offertalk.entity.InterviewExperience;
import com.offertalk.entity.SysUser;
import com.offertalk.mapper.InterviewExperienceMapper;
import com.offertalk.service.CompanyService;
import com.offertalk.service.InterviewExperienceService;
import com.offertalk.service.SensitiveWordService;
import com.offertalk.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class InterviewExperienceServiceImpl extends ServiceImpl<InterviewExperienceMapper, InterviewExperience>
        implements InterviewExperienceService {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Page<InterviewExperience> getExperiencePage(Integer companyId,Integer recruitType, String sortBy, String keyword, Integer page, Integer size) {
        Page<InterviewExperience> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<InterviewExperience> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(InterviewExperience::getAuditStatus, Constants.AUDIT_STATUS_PASS)
                .eq(InterviewExperience::getIsDeleted, Constants.DELETED_NO);

        if (recruitType != null && recruitType > 0) {
            wrapper.eq(InterviewExperience::getRecruitType, recruitType);
        }

        if (companyId != null && companyId > 0) {
            wrapper.eq(InterviewExperience::getCompanyId, companyId);
        }

        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(InterviewExperience::getTitle, keyword)
                    .or()
                    .like(InterviewExperience::getExperienceText, keyword));
        }

        switch (sortBy != null ? sortBy : Constants.SORT_BY_NEW) {
            case Constants.SORT_BY_HOT:
                wrapper.orderByDesc(InterviewExperience::getLikeCount)
                        .orderByDesc(InterviewExperience::getViewCount);
                break;
            case Constants.SORT_BY_TRUTH:
                wrapper.orderByDesc(InterviewExperience::getTruthScore);
                break;
            case Constants.SORT_BY_NEW:
            default:
                wrapper.orderByDesc(InterviewExperience::getCreateTime);
                break;
        }

        Page<InterviewExperience> result = this.page(pageParam, wrapper);

        // 添加公司信息
        result.getRecords().forEach(experience -> {
            addCompanyInfo(experience);
        });

        return result;
    }

    @Override
    public InterviewExperience getExperienceDetail(Long id) {
        InterviewExperience experience = this.getById(id);
        if (experience == null) {
            throw new com.offertalk.common.BusinessException(404, "面经不存在");
        }
        if (experience.getIsDeleted() == Constants.DELETED_YES) {
            throw new com.offertalk.common.BusinessException(404, "面经已被删除");
        }

        addCompanyInfo(experience);
        return experience;
    }

    private void addCompanyInfo(InterviewExperience experience) {
        if (experience.getCompanyId() != null) {
            try {
                Company company = companyService.getCompanyDetail(experience.getCompanyId());
                if (company != null) {
                    // 动态添加公司信息
                    experience.setCompanyName(company.getName());
                    experience.setCompanyLogo(company.getLogoUrl());
                }
            } catch (Exception e) {
                // 忽略异常，保持原有逻辑
            }
        }
    }

    @Override
    @Transactional
    public Long createExperience(InterviewExperience experience) {
        if (sensitiveWordService.shouldBlock(experience.getTitle())) {
            throw new BusinessException("标题包含敏感内容，请修改后重试");
        }
        if (sensitiveWordService.shouldBlock(experience.getExperienceText())) {
            throw new BusinessException("内容包含敏感内容，请修改后重试");
        }

        experience.setTitle(sensitiveWordService.filterSensitiveWords(experience.getTitle()));
        experience.setExperienceText(sensitiveWordService.filterSensitiveWords(experience.getExperienceText()));
        experience.setAuditStatus(Constants.AUDIT_STATUS_PASS);
        experience.setTruthScore(BigDecimal.valueOf(5.0));
        experience.setTruthScoreCount(0);
        experience.setViewCount(0);
        experience.setLikeCount(0);
        experience.setCollectCount(0);
        experience.setCommentCount(0);
        experience.setReportCount(0);
        experience.setShareCount(0);
        experience.setIsDeleted(Constants.DELETED_NO);

        if (experience.getIsAnonymous() == null) {
            experience.setIsAnonymous(1);
        }
        if (experience.getUserId() == null) {
            experience.setUserId(Constants.ANONYMOUS_USER_ID);
        }

        this.save(experience);

        // 更新用户发布计数
        if (experience.getUserId() != null && experience.getUserId() > 0) {
            SysUser user = sysUserService.getById(experience.getUserId());
            if (user != null) {
                user.setTotalPostCount(user.getTotalPostCount() + 1);
                sysUserService.updateById(user);
            }
        }

        return experience.getId();
    }

    @Override
    public void incrementViewCount(Long id) {
        InterviewExperience experience = this.getById(id);
        if (experience != null) {
            experience.setViewCount(experience.getViewCount() + 1);
            this.updateById(experience);
        }
    }

    @Override
    public void updateLikeCount(Long id, Integer delta) {
        InterviewExperience experience = this.getById(id);
        if (experience != null) {
            experience.setLikeCount(experience.getLikeCount() + delta);
            this.updateById(experience);
        }
    }

    @Override
    public void updateCommentCount(Long id, Integer delta) {
        InterviewExperience experience = this.getById(id);
        if (experience != null) {
            experience.setCommentCount(experience.getCommentCount() + delta);
            this.updateById(experience);
        }
    }

    @Override
    public void updateTruthScore(Long id, Double score) {
        InterviewExperience experience = this.getById(id);
        if (experience != null) {
            int count = experience.getTruthScoreCount();
            double currentScore = experience.getTruthScore().doubleValue();
            double newScore = (currentScore * count + score) / (count + 1);
            experience.setTruthScore(BigDecimal.valueOf(newScore));
            experience.setTruthScoreCount(count + 1);
            this.updateById(experience);
        }
    }

    private static class BusinessException extends com.offertalk.common.BusinessException {
        public BusinessException(String message) {
            super(message);
        }
    }
}