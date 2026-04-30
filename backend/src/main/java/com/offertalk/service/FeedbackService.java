package com.offertalk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.controller.dto.request.FeedbackCreateRequest;
import com.offertalk.entity.Feedback;

/**
 * 意见反馈服务接口
 */
public interface FeedbackService {
    
    /**
     * 创建反馈
     */
    Feedback create(FeedbackCreateRequest request);
    
    /**
     * 获取反馈详情
     */
    Feedback getById(Long id);
    
    /**
     * 分页查询用户反馈
     */
    IPage<Feedback> getByUserId(Long userId, Page<Feedback> page);
    
    /**
     * 更新反馈状态
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 回复反馈
     */
    boolean reply(Long id, String replyContent);
}