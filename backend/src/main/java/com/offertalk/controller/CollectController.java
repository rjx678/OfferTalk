package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import com.offertalk.service.CollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/collect")
public class CollectController {

    private static final Logger logger = LoggerFactory.getLogger(CollectController.class);

    @Autowired
    private CollectService collectService;

    @PostMapping("/toggle")
    public ApiResponse<Map<String, Boolean>> toggleCollect(@RequestBody Map<String, Object> params) {
        try {
            Long userId = params.containsKey("userId") ? Long.parseLong(params.get("userId").toString()) : null;
            Integer contentType = params.containsKey("contentType") ? Integer.parseInt(params.get("contentType").toString()) : null;
            Long contentId = params.containsKey("contentId") ? Long.parseLong(params.get("contentId").toString()) : null;

            logger.info("收藏请求 - userId: {}, contentType: {}, contentId: {}", userId, contentType, contentId);

            if (userId == null || contentType == null || contentId == null) {
                logger.warn("收藏参数缺失 - userId: {}, contentType: {}, contentId: {}", userId, contentType, contentId);
                return ApiResponse.error(400, "参数缺失");
            }

            boolean hasCollected = collectService.toggleCollect(userId, contentType, contentId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("hasCollected", hasCollected);
            
            logger.info("收藏结果 - hasCollected: {}", hasCollected);
            return ApiResponse.success(hasCollected ? "收藏成功" : "取消收藏", result);
            
        } catch (NumberFormatException e) {
            logger.error("收藏参数格式错误", e);
            return ApiResponse.error(400, "参数格式错误");
        } catch (Exception e) {
            logger.error("收藏失败", e);
            return ApiResponse.error(500, "收藏失败: " + e.getMessage());
        }
    }

    @GetMapping("/check")
    public ApiResponse<Map<String, Boolean>> checkCollect(
            @RequestParam Long userId,
            @RequestParam Integer contentType,
            @RequestParam Long contentId) {

        try {
            logger.info("检查收藏状态 - userId: {}, contentType: {}, contentId: {}", userId, contentType, contentId);
            
            boolean hasCollected = collectService.hasCollected(userId, contentType, contentId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("hasCollected", hasCollected);
            return ApiResponse.success(result);
            
        } catch (Exception e) {
            logger.error("检查收藏状态失败", e);
            return ApiResponse.error(500, "检查失败: " + e.getMessage());
        }
    }
}
