package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import com.offertalk.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/create")
    public ApiResponse<String> createReport(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Integer contentType = Integer.valueOf(params.get("contentType").toString());
        Long contentId = Long.valueOf(params.get("contentId").toString());
        Integer reportType = Integer.valueOf(params.get("reportType").toString());
        String reason = params.get("reason") != null ? params.get("reason").toString() : "";

        reportService.createReport(userId, contentType, contentId, reportType, reason);
        return ApiResponse.success("举报成功，我们会尽快处理");
    }
}
