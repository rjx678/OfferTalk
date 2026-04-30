package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import com.offertalk.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    @PostMapping("/toggle")
    public ApiResponse<Map<String, Boolean>> toggleCollect(
            @RequestParam Long userId,
            @RequestParam Integer contentType,
            @RequestParam Long contentId) {

        boolean hasCollected = collectService.toggleCollect(userId, contentType, contentId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("hasCollected", hasCollected);
        return ApiResponse.success(hasCollected ? "收藏成功" : "取消收藏", result);
    }

    @GetMapping("/check")
    public ApiResponse<Map<String, Boolean>> checkCollect(
            @RequestParam Long userId,
            @RequestParam Integer contentType,
            @RequestParam Long contentId) {

        boolean hasCollected = collectService.hasCollected(userId, contentType, contentId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("hasCollected", hasCollected);
        return ApiResponse.success(result);
    }
}
