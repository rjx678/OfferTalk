package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import com.offertalk.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/toggle")
    public ApiResponse<Map<String, Boolean>> toggleLike(
            @RequestParam Long userId,
            @RequestParam Integer contentType,
            @RequestParam Long contentId) {

        boolean hasLiked = likeService.toggleLike(userId, contentType, contentId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("hasLiked", hasLiked);
        return ApiResponse.success(hasLiked ? "点赞成功" : "取消点赞", result);
    }

    @GetMapping("/check")
    public ApiResponse<Map<String, Boolean>> checkLike(
            @RequestParam Long userId,
            @RequestParam Integer contentType,
            @RequestParam Long contentId) {

        boolean hasLiked = likeService.hasLiked(userId, contentType, contentId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("hasLiked", hasLiked);
        return ApiResponse.success(result);
    }
}
