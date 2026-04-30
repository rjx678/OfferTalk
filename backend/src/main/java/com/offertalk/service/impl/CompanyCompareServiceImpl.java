package com.offertalk.service.impl;

import com.offertalk.controller.dto.request.CompanyCompareRequest;
import com.offertalk.controller.dto.response.CompanyCompareResponse;
import com.offertalk.entity.Company;
import com.offertalk.service.CompanyCompareService;
import com.offertalk.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公司对比服务实现
 */
@Service
public class CompanyCompareServiceImpl implements CompanyCompareService {
    
    @Autowired
    private CompanyService companyService;
    
    @Override
    public CompanyCompareResponse compare(CompanyCompareRequest request) {
        List<Long> companyIds = request.getCompanyIds();
        if (companyIds == null || companyIds.size() < 2) {
            throw new IllegalArgumentException("至少需要选择2家公司进行对比");
        }
        
        CompanyCompareRequest.DimensionWeights weights = request.getWeights();
        if (weights == null) {
            weights = new CompanyCompareRequest.DimensionWeights();
        }
        
        List<CompanyCompareResponse.CompanyCompareItem> items = new ArrayList<>();
        Long bestCompanyId = null;
        double bestScore = 0;
        
        for (Long companyId : companyIds) {
            CompanyCompareResponse.CompanyCompareItem item = getCompanyCompareInfo(companyId);
            
            // 计算综合评分
            double overallScore = calculateOverallScore(item, weights);
            item.setOverallScore(overallScore);
            
            items.add(item);
            
            if (overallScore > bestScore) {
                bestScore = overallScore;
                bestCompanyId = companyId;
            }
        }
        
        // 按综合评分排序
        items.sort(Comparator.comparingDouble(CompanyCompareResponse.CompanyCompareItem::getOverallScore).reversed());
        
        CompanyCompareResponse response = new CompanyCompareResponse();
        response.setItems(items);
        response.setBestCompanyId(bestCompanyId);
        response.setWeights(weights);
        
        return response;
    }
    
    /**
     * 计算综合评分
     */
    private double calculateOverallScore(CompanyCompareResponse.CompanyCompareItem item, 
                                        CompanyCompareRequest.DimensionWeights weights) {
        double score = 0;
        score += (item.getSalaryScore() != null ? item.getSalaryScore() : 0) * (weights.getSalaryWeight() != null ? weights.getSalaryWeight() : 0);
        score += (item.getCultureScore() != null ? item.getCultureScore() : 0) * (weights.getCultureWeight() != null ? weights.getCultureWeight() : 0);
        score += (item.getWorkLifeScore() != null ? item.getWorkLifeScore() : 0) * (weights.getWorkLifeWeight() != null ? weights.getWorkLifeWeight() : 0);
        score += (item.getDevelopmentScore() != null ? item.getDevelopmentScore() : 0) * (weights.getDevelopmentWeight() != null ? weights.getDevelopmentWeight() : 0);
        score += (item.getWelfareScore() != null ? item.getWelfareScore() : 0) * (weights.getWelfareWeight() != null ? weights.getWelfareWeight() : 0);
        return Math.round(score * 100.0) / 100.0;
    }
    
    @Override
    public List<CompanyCompareResponse.CompanyCompareItem> searchCompanies(String keyword) {
        List<Company> companies = companyService.searchCompanies(keyword);

        return companies.stream()
                .map(this::convertToCompareItem)
                .collect(Collectors.toList());
    }
    
    @Override
    public CompanyCompareResponse.CompanyCompareItem getCompanyCompareInfo(Long companyId) {
        Company company = companyService.getById(companyId);
        if (company == null) {
            throw new IllegalArgumentException("公司不存在");
        }
        return convertToCompareItem(company);
    }
    
    /**
     * 转换为对比项
     */
    private CompanyCompareResponse.CompanyCompareItem convertToCompareItem(Company company) {
        CompanyCompareResponse.CompanyCompareItem item = new CompanyCompareResponse.CompanyCompareItem();
        item.setCompanyId(company.getId());
        item.setCompanyName(company.getName());
        item.setLogoUrl(company.getLogoUrl());
        item.setIndustry(company.getIndustry());
        item.setStage(company.getStage());
        
        // 设置模拟评分数据
        setMockScores(item);
        
        return item;
    }
    
    /**
     * 设置模拟评分数据
     */
    private void setMockScores(CompanyCompareResponse.CompanyCompareItem item) {
        // 根据公司名称生成模拟数据
        String companyName = item.getCompanyName();
        
        // 模拟薪资评分（3.5-5.0）
        item.setSalaryScore(generateMockScore(companyName, "salary", 3.5, 5.0));
        
        // 模拟企业文化评分（3.0-5.0）
        item.setCultureScore(generateMockScore(companyName, "culture", 3.0, 5.0));
        
        // 模拟工作生活平衡评分（2.5-5.0）
        item.setWorkLifeScore(generateMockScore(companyName, "worklife", 2.5, 5.0));
        
        // 模拟发展前景评分（3.0-5.0）
        item.setDevelopmentScore(generateMockScore(companyName, "dev", 3.0, 5.0));
        
        // 模拟福利评分（3.0-5.0）
        item.setWelfareScore(generateMockScore(companyName, "welfare", 3.0, 5.0));
        
        // 设置薪资范围
        int avgSalary = (int) (item.getSalaryScore() * 3000 + 12000);
        item.setAvgMonthlySalary(avgSalary);
        item.setSalaryRange((avgSalary - 3000) + "K-" + (avgSalary + 5000) + "K");
        item.setAvgAnnualBonus((int) (avgSalary * 2.5));
        
        // 设置评价数据
        item.setReviewCount((int) (Math.random() * 500 + 50));
        item.setProsSummary(generatePros(companyName));
        item.setConsSummary(generateCons(companyName));
        
        // 设置标签
        item.setTags(generateTags(item.getSalaryScore(), item.getWorkLifeScore()));
    }
    
    /**
     * 根据名称生成模拟评分
     */
    private double generateMockScore(String name, String seed, double min, double max) {
        int hash = (name + seed).hashCode();
        double random = (hash & 0x7FFFFFFF) / (double) Integer.MAX_VALUE;
        double score = min + random * (max - min);
        return Math.round(score * 10.0) / 10.0;
    }
    
    /**
     * 生成优点描述
     */
    private String generatePros(String companyName) {
        List<String> prosList = Arrays.asList(
                "薪资待遇优厚",
                "发展空间大",
                "团队氛围好",
                "技术栈先进",
                "培训机会多",
                "晋升通道清晰",
                "福利待遇完善",
                "工作环境舒适"
        );
        int idx = Math.abs(companyName.hashCode()) % prosList.size();
        return prosList.get(idx) + "，" + prosList.get((idx + 2) % prosList.size());
    }
    
    /**
     * 生成缺点描述
     */
    private String generateCons(String companyName) {
        List<String> consList = Arrays.asList(
                "加班较多",
                "压力较大",
                "流程繁琐",
                "沟通成本高",
                "晋升较慢",
                "薪资涨幅有限",
                "技术债务较重",
                "会议较多"
        );
        int idx = Math.abs(companyName.hashCode()) % consList.size();
        return consList.get(idx) + "，" + consList.get((idx + 1) % consList.size());
    }
    
    /**
     * 生成标签
     */
    private List<String> generateTags(double salaryScore, double workLifeScore) {
        List<String> tags = new ArrayList<>();
        if (salaryScore >= 4.5) {
            tags.add("高薪");
        }
        if (workLifeScore >= 4.0) {
            tags.add("WLB友好");
        } else if (workLifeScore < 3.0) {
            tags.add("强度较大");
        }
        if (salaryScore >= 4.0 && workLifeScore >= 3.5) {
            tags.add("推荐");
        }
        return tags;
    }
}