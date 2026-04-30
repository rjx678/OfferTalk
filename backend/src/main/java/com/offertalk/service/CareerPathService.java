package com.offertalk.service;

import com.offertalk.controller.dto.request.CareerPathRequest;
import com.offertalk.controller.dto.response.CareerPathResponse;

import java.util.List;

/**
 * 职业路径规划服务接口
 */
public interface CareerPathService {
    
    /**
     * 生成职业发展路径
     */
    CareerPathResponse generatePath(CareerPathRequest request);
    
    /**
     * 获取所有职位列表
     */
    List<CareerPathResponse.PositionInfo> getAllPositions();
    
    /**
     * 获取职位详情
     */
    CareerPathResponse.PositionInfo getPosition(String positionId);
    
    /**
     * 获取学习资源
     */
    List<CareerPathResponse.LearningResource> getResources(List<String> skillNames);
}