package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import com.offertalk.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/like")
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    private LikeService likeService;

    @PostMapping("/toggle")
    public ApiResponse<Map<String, Boolean>> toggleLike(@RequestBody Map<String, Object> params) {
        try {
            Long userId = params.containsKey("userId") ? Long.parseLong(params.get("userId").toString()) : null;
            Integer contentType = params.containsKey("contentType") ? Integer.parseInt(params.get("contentType").toString()) : null;
            Long contentId = params.containsKey("contentId") ? Long.parseLong(params.get("contentId").toString()) : null;

            logger.info("点赞请求 - userId: {}, contentType: {}, contentId: {}", userId, contentType, contentId);

            if (userId == null || contentType == null || contentId == null) {
                logger.warn("点赞参数缺失 - userId: {}, contentType: {}, contentId: {}", userId, contentType, contentId);
                return ApiResponse.error(400, "参数缺失");
            }

            boolean hasLiked = likeService.toggleLike(userId, contentType, contentId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("hasLiked", hasLiked);
            
            logger.info("点赞结果 - hasLiked: {}", hasLiked);
            return ApiResponse.success(hasLiked ? "点赞成功" : "取消点赞", result);
            
        } catch (NumberFormatException e) {
            logger.error("点赞参数格式错误", e);
            return ApiResponse.error(400, "参数格式错误");
        } catch (Exception e) {
            logger.error("点赞失败", e);
            return ApiResponse.error(500, "点赞失败: " + e.getMessage());
        }
    }

    @GetMapping("/check")
    public ApiResponse<Map<String, Boolean>> checkLike(
            @RequestParam Long userId,
            @RequestParam Integer contentType,
            @RequestParam Long contentId) {

        try {
            logger.info("检查点赞状态 - userId: {}, contentType: {}, contentId: {}", userId, contentType, contentId);
            
            boolean hasLiked = likeService.hasLiked(userId, contentType, contentId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("hasLiked", hasLiked);
            return ApiResponse.success(result);
            
        } catch (Exception e) {
            logger.error("检查点赞状态失败", e);
            return ApiResponse.error(500, "检查失败: " + e.getMessage());
        }
    }
}
