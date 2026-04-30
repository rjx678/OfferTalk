package com.offertalk.controller;

import com.offertalk.common.ApiResponse;
import com.offertalk.controller.dto.request.CareerPathRequest;
import com.offertalk.controller.dto.response.CareerPathResponse;
import com.offertalk.service.CareerPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 职业路径规划控制器
 */
@RestController
@RequestMapping("/career-path")
public class CareerPathController {
    
    @Autowired
    private CareerPathService careerPathService;
    
    /**
     * 生成职业发展路径
     */
    @PostMapping("/generate")
    public ApiResponse<CareerPathResponse> generatePath(@RequestBody CareerPathRequest request) {
        try {
            CareerPathResponse response = careerPathService.generatePath(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("生成路径失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有职位列表
     */
    @GetMapping("/positions")
    public ApiResponse<List<CareerPathResponse.PositionInfo>> getAllPositions() {
        try {
            List<CareerPathResponse.PositionInfo> positions = careerPathService.getAllPositions();
            return ApiResponse.success(positions);
        } catch (Exception e) {
            return ApiResponse.error("获取职位列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取职位详情
     */
    @GetMapping("/positions/{positionId}")
    public ApiResponse<CareerPathResponse.PositionInfo> getPosition(@PathVariable String positionId) {
        try {
            CareerPathResponse.PositionInfo position = careerPathService.getPosition(positionId);
            return ApiResponse.success(position);
        } catch (Exception e) {
            return ApiResponse.error("获取职位详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取学习资源
     */
    @GetMapping("/resources")
    public ApiResponse<List<CareerPathResponse.LearningResource>> getResources(
            @RequestParam(required = false) List<String> skills) {
        try {
            List<CareerPathResponse.LearningResource> resources = careerPathService.getResources(skills);
            return ApiResponse.success(resources);
        } catch (Exception e) {
            return ApiResponse.error("获取学习资源失败：" + e.getMessage());
        }
    }
}