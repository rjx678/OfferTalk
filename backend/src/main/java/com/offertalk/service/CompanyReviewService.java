package com.offertalk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.offertalk.entity.CompanyReview;

import java.util.List;

public interface CompanyReviewService extends IService<CompanyReview> {
    Page<CompanyReview> getReviewPage(Integer companyId,String keyword, Integer recruitType, Integer page, Integer size);
    CompanyReview getReviewDetail(Long id);
    Long createReview(CompanyReview review);
    void incrementViewCount(Long id);
    void updateLikeCount(Long id, Integer delta);
    void updateTruthScore(Long id, Double score);
    void updateCommentCount(Long id, Integer delta);
    List<CompanyReview> getHotReviews(Integer limit);
}
