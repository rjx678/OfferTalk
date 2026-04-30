package com.offertalk.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.offertalk.common.ApiResponse;
import com.offertalk.controller.dto.request.FeedbackCreateRequest;
import com.offertalk.entity.Feedback;
import com.offertalk.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 意见反馈控制器
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    /**
     * 创建反馈
     */
    @PostMapping("/create")
    public ApiResponse<Feedback> create(@RequestBody FeedbackCreateRequest request) {
        try {
            Feedback feedback = feedbackService.create(request);
            return ApiResponse.success(feedback);
        } catch (Exception e) {
            return ApiResponse.error("提交反馈失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取反馈详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Feedback> getById(@PathVariable Long id) {
        try {
            Feedback feedback = feedbackService.getById(id);
            if (feedback == null) {
                return ApiResponse.error("反馈不存在");
            }
            return ApiResponse.success(feedback);
        } catch (Exception e) {
            return ApiResponse.error("获取反馈失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取用户反馈列表
     */
    @GetMapping("/list")
    public ApiResponse<IPage<Feedback>> getByUserId(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<Feedback> pageParam = new Page<>(page, size);
            IPage<Feedback> result = feedbackService.getByUserId(userId, pageParam);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("获取反馈列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新反馈状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Boolean> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        try {
            boolean success = feedbackService.updateStatus(id, status);
            return ApiResponse.success(success);
        } catch (Exception e) {
            return ApiResponse.error("更新状态失败：" + e.getMessage());
        }
    }
    
    /**
     * 回复反馈
     */
    @PutMapping("/{id}/reply")
    public ApiResponse<Boolean> reply(
            @PathVariable Long id,
            @RequestBody String replyContent) {
        try {
            boolean success = feedbackService.reply(id, replyContent);
            return ApiResponse.success(success);
        } catch (Exception e) {
            return ApiResponse.error("回复失败：" + e.getMessage());
        }
    }
}