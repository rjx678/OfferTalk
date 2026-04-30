package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.offertalk.common.Constants;
import com.offertalk.entity.LikeRecord;
import com.offertalk.mapper.LikeRecordMapper;
import com.offertalk.service.InterviewExperienceService;
import com.offertalk.service.LikeService;
import com.offertalk.service.SalaryDisclosureService;
import com.offertalk.service.CompanyReviewService;
import com.offertalk.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private InterviewExperienceService interviewExperienceService;

    @Autowired
    private SalaryDisclosureService salaryDisclosureService;

    @Autowired
    private CompanyReviewService companyReviewService;

    @Autowired
    private CommentService commentService;

    @Override
    @Transactional
    public boolean toggleLike(Long userId, Integer contentType, Long contentId) {
        LambdaQueryWrapper<LikeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LikeRecord::getUserId, userId)
                .eq(LikeRecord::getContentType, contentType)
                .eq(LikeRecord::getContentId, contentId);
        LikeRecord record = likeRecordMapper.selectOne(wrapper);

        boolean hasLiked;
        if (record == null) {
            record = new LikeRecord();
            record.setUserId(userId);
            record.setContentType(contentType);
            record.setContentId(contentId);
            record.setStatus(Constants.LIKE_STATUS_ACTIVE);
            likeRecordMapper.insert(record);
            hasLiked = true;
            updateContentLikeCount(contentType, contentId, 1);
        } else if (record.getStatus().equals(Constants.LIKE_STATUS_ACTIVE)) {
            record.setStatus(Constants.LIKE_STATUS_CANCELLED);
            likeRecordMapper.updateById(record);
            hasLiked = false;
            updateContentLikeCount(contentType, contentId, -1);
        } else {
            record.setStatus(Constants.LIKE_STATUS_ACTIVE);
            likeRecordMapper.updateById(record);
            hasLiked = true;
            updateContentLikeCount(contentType, contentId, 1);
        }

        return hasLiked;
    }

    @Override
    public boolean hasLiked(Long userId, Integer contentType, Long contentId) {
        LambdaQueryWrapper<LikeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LikeRecord::getUserId, userId)
                .eq(LikeRecord::getContentType, contentType)
                .eq(LikeRecord::getContentId, contentId)
                .eq(LikeRecord::getStatus, Constants.LIKE_STATUS_ACTIVE);
        return likeRecordMapper.selectCount(wrapper) > 0;
    }

    private void updateContentLikeCount(Integer contentType, Long contentId, Integer delta) {
        switch (contentType) {
            case Constants.CONTENT_TYPE_INTERVIEW:
                interviewExperienceService.updateLikeCount(contentId, delta);
                break;
            case Constants.CONTENT_TYPE_SALARY:
                salaryDisclosureService.updateLikeCount(contentId, delta);
                break;
            case Constants.CONTENT_TYPE_REVIEW:
                companyReviewService.updateLikeCount(contentId, delta);
                break;
            case Constants.CONTENT_TYPE_COMMENT:
                commentService.updateLikeCount(contentId, delta);
                break;
        }
    }
}
