package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/disclaimer")
    public ApiResponse<Map<String, String>> getDisclaimer() {
        Map<String, String> result = new HashMap<>();
        result.put("disclaimer", "所有面经、薪资、公司评价均为用户自主分享与网络公开整理，仅作求职参考，不构成任何就业、择业建议。");
        return ApiResponse.success(result);
    }

    @GetMapping("/config")
    public ApiResponse<Map<String, Object>> getConfig() {
        Map<String, Object> result = new HashMap<>();
        result.put("siteName", "OfferTalk求职爆料");
        result.put("version", "1.0.0");
        result.put("recruitTypes", new String[]{"全部", "校招", "社招", "实习"});
        result.put("sortOptions", new String[]{"最新", "最热", "最真实"});
        return ApiResponse.success(result);
    }

    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "offertalk-backend");
        return ApiResponse.success(result);
    }
}
