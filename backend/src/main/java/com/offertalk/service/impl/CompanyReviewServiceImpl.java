package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offertalk.common.Constants;
import com.offertalk.entity.Company;
import com.offertalk.entity.CompanyReview;
import com.offertalk.entity.InterviewExperience;
import com.offertalk.entity.SysUser;
import com.offertalk.mapper.CompanyReviewMapper;
import com.offertalk.service.CompanyReviewService;
import com.offertalk.service.CompanyService;
import com.offertalk.service.SensitiveWordService;
import com.offertalk.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompanyReviewServiceImpl extends ServiceImpl<CompanyReviewMapper, CompanyReview>
        implements CompanyReviewService {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Page<CompanyReview> getReviewPage(Integer companyId,String keyword, Integer recruitType, Integer page, Integer size) {
        Page<CompanyReview> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<CompanyReview> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(CompanyReview::getAuditStatus, Constants.AUDIT_STATUS_PASS)
                .eq(CompanyReview::getIsDeleted, Constants.DELETED_NO);

        if (StringUtils.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(CompanyReview::getTitle, keyword)
                    .or()
                    .like(CompanyReview::getProsText, keyword)
                    .or()
                    .like(CompanyReview::getConsText, keyword));
        }

        if (companyId != null && companyId > 0) {
            wrapper.eq(CompanyReview::getCompanyId, companyId);
        }

        if (recruitType != null && recruitType > 0) {
            wrapper.eq(CompanyReview::getRecruitType, recruitType);
        }

        wrapper.orderByDesc(CompanyReview::getCreateTime);

        Page<CompanyReview> result = this.page(pageParam, wrapper);

        // 添加公司信息
        result.getRecords().forEach(review -> {
            addCompanyInfo(review);
        });

        return result;
    }

    @Override
    public CompanyReview getReviewDetail(Long id) {
        CompanyReview review = this.getById(id);
        if (review == null) {
            throw new com.offertalk.common.BusinessException(404, "公司评价不存在");
        }
        if (review.getIsDeleted() == Constants.DELETED_YES) {
            throw new com.offertalk.common.BusinessException(404, "公司评价已被删除");
        }

        addCompanyInfo(review);
        return review;
    }

    private void addCompanyInfo(CompanyReview review) {
        if (review.getCompanyId() != null) {
            try {
                Company company = companyService.getCompanyDetail(review.getCompanyId());
                if (company != null) {
                    // 动态添加公司信息
                    review.setCompanyName(company.getName());
                    review.setCompanyLogo(company.getLogoUrl());
                }
            } catch (Exception e) {
                // 忽略异常，保持原有逻辑
            }
        }
    }

    @Override
    @Transactional
    public Long createReview(CompanyReview review) {
        if (sensitiveWordService.shouldBlock(review.getTitle())) {
            throw new BusinessException("标题包含敏感内容，请修改后重试");
        }
        if (sensitiveWordService.shouldBlock(review.getProsText())) {
            throw new BusinessException("优点包含敏感内容，请修改后重试");
        }
        if (sensitiveWordService.shouldBlock(review.getConsText())) {
            throw new BusinessException("缺点包含敏感内容，请修改后重试");
        }

        review.setTitle(sensitiveWordService.filterSensitiveWords(review.getTitle()));
        review.setProsText(sensitiveWordService.filterSensitiveWords(review.getProsText()));
        review.setConsText(sensitiveWordService.filterSensitiveWords(review.getConsText()));
        review.setAuditStatus(Constants.AUDIT_STATUS_PASS);
        review.setTruthScore(BigDecimal.valueOf(5.0));
        review.setTruthScoreCount(0);
        review.setViewCount(0);
        review.setLikeCount(0);
        review.setCollectCount(0);
        review.setCommentCount(0);
        review.setReportCount(0);
        review.setIsDeleted(Constants.DELETED_NO);

        if (review.getIsAnonymous() == null) {
            review.setIsAnonymous(1);
        }
        if (review.getUserId() == null) {
            review.setUserId(Constants.ANONYMOUS_USER_ID);
        }

        this.save(review);

        // 更新用户发布计数
        if (review.getUserId() != null && review.getUserId() > 0) {
            SysUser user = sysUserService.getById(review.getUserId());
            if (user != null) {
                user.setTotalPostCount(user.getTotalPostCount() + 1);
                sysUserService.updateById(user);
            }
        }

        return review.getId();
    }

    @Override
    public void incrementViewCount(Long id) {
        CompanyReview review = this.getById(id);
        if (review != null) {
            review.setViewCount(review.getViewCount() + 1);
            this.updateById(review);
        }
    }

    @Override
    public void updateLikeCount(Long id, Integer delta) {
        CompanyReview review = this.getById(id);
        if (review != null) {
            review.setLikeCount(review.getLikeCount() + delta);
            this.updateById(review);
        }
    }

    @Override
    public void updateCommentCount(Long id, Integer delta) {
        CompanyReview review = this.getById(id);
        if (review != null) {
            review.setCommentCount(review.getCommentCount() + delta);
            this.updateById(review);
        }
    }

    @Override
    public void updateTruthScore(Long id, Double score) {
        CompanyReview review = this.getById(id);
        if (review != null) {
            int count = review.getTruthScoreCount();
            double currentScore = review.getTruthScore().doubleValue();
            double newScore = (currentScore * count + score) / (count + 1);
            review.setTruthScore(BigDecimal.valueOf(newScore));
            review.setTruthScoreCount(count + 1);
            this.updateById(review);
        }
    }

    @Override
    public List<CompanyReview> getHotReviews(Integer limit) {
        LambdaQueryWrapper<CompanyReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CompanyReview::getAuditStatus, Constants.AUDIT_STATUS_PASS)
                .eq(CompanyReview::getIsDeleted, Constants.DELETED_NO)
                .orderByDesc(CompanyReview::getLikeCount)
                .orderByDesc(CompanyReview::getViewCount)
                .last("LIMIT " + limit);

        List<CompanyReview> reviews = this.list(wrapper);
        reviews.forEach(this::addCompanyInfo);
        return reviews;
    }

    private static class BusinessException extends com.offertalk.common.BusinessException {
        public BusinessException(String message) {
            super(message);
        }
    }
}