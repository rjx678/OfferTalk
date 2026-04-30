package com.offertalk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.controller.dto.request.FeedbackCreateRequest;
import com.offertalk.entity.Feedback;
import com.offertalk.mapper.FeedbackMapper;
import com.offertalk.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 意见反馈服务实现
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {
    
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    @Override
    @Transactional
    public Feedback create(FeedbackCreateRequest request) {
        Feedback feedback = new Feedback();
        feedback.setUserId(request.getUserId());
        feedback.setType(request.getType());
        feedback.setTitle(request.getTitle());
        feedback.setContent(request.getContent());
        
        // 处理图片列表
        List<String> images = request.getImages();
        if (images != null && !images.isEmpty()) {
            feedback.setImages(String.join(",", images));
        }
        
        feedback.setContact(request.getContact());
        feedback.setStatus(0); // 0-待处理
        feedback.setCreateTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        
        feedbackMapper.insert(feedback);
        return feedback;
    }
    
    @Override
    public Feedback getById(Long id) {
        return feedbackMapper.selectById(id);
    }
    
    @Override
    public IPage<Feedback> getByUserId(Long userId, Page<Feedback> page) {
        LambdaQueryWrapper<Feedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Feedback::getUserId, userId)
                .orderByDesc(Feedback::getCreateTime);
        return feedbackMapper.selectPage(page, wrapper);
    }
    
    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status) {
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            return false;
        }
        feedback.setStatus(status);
        feedback.setUpdateTime(LocalDateTime.now());
        return feedbackMapper.updateById(feedback) > 0;
    }
    
    @Override
    @Transactional
    public boolean reply(Long id, String replyContent) {
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            return false;
        }
        feedback.setReplyContent(replyContent);
        feedback.setReplyTime(LocalDateTime.now());
        feedback.setStatus(2); // 2-已回复
        feedback.setUpdateTime(LocalDateTime.now());
        return feedbackMapper.updateById(feedback) > 0;
    }
}