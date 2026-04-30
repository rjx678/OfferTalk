package com.offertalk.service.impl;

import com.offertalk.controller.dto.request.CareerPathRequest;
import com.offertalk.controller.dto.response.CareerPathResponse;
import com.offertalk.service.CareerPathService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 职业路径规划服务实现
 */
@Service
public class CareerPathServiceImpl implements CareerPathService {
    
    /**
     * 职位数据（模拟数据）
     */
    private static final Map<String, CareerPathResponse.PositionInfo> POSITIONS = new HashMap<>();
    
    static {
        // 初级职位
        POSITIONS.put("junior-dev", createPosition("junior-dev", "初级开发工程师", "初级", "技术部", 0, 3,
                Arrays.asList("Java/Python基础", "SQL基础", "Git协作", "基础算法"),
                Arrays.asList("参与需求开发", "编写单元测试", "代码review", "文档编写")));
        
        POSITIONS.put("junior-fe", createPosition("junior-fe", "初级前端工程师", "初级", "技术部", 0, 3,
                Arrays.asList("HTML/CSS/JS", "Vue/React基础", "响应式设计", "调试技能"),
                Arrays.asList("页面开发", "组件开发", "性能优化", "Bug修复")));
        
        // 中级职位
        POSITIONS.put("mid-dev", createPosition("mid-dev", "中级开发工程师", "中级", "技术部", 2, 5,
                Arrays.asList("Java/Python进阶", "分布式系统", "微服务架构", "性能优化"),
                Arrays.asList("模块设计", "技术方案制定", "Code Review", "指导新人")));
        
        POSITIONS.put("mid-fe", createPosition("mid-fe", "中级前端工程师", "中级", "技术部", 2, 5,
                Arrays.asList("Vue/React进阶", "工程化实践", "性能优化", "跨端开发"),
                Arrays.asList("架构设计", "技术选型", "团队协作", "技术分享")));
        
        // 高级职位
        POSITIONS.put("senior-dev", createPosition("senior-dev", "高级开发工程师", "高级", "技术部", 4, 8,
                Arrays.asList("系统架构设计", "高并发处理", "技术选型", "团队管理"),
                Arrays.asList("系统架构设计", "技术难点攻关", "团队建设", "技术规划")));
        
        POSITIONS.put("tech-lead", createPosition("tech-lead", "技术负责人", "Lead", "技术部", 5, 10,
                Arrays.asList("技术战略", "团队管理", "项目管理", "跨部门协作"),
                Arrays.asList("技术方向把控", "团队管理", "资源协调", "对外沟通")));
        
        // 管理职位
        POSITIONS.put("tech-manager", createPosition("tech-manager", "技术经理", "管理", "技术部", 6, 12,
                Arrays.asList("团队管理", "项目管理", "预算管理", "战略规划"),
                Arrays.asList("团队建设", "项目交付", "预算控制", "技术规划")));
        
        POSITIONS.put("cto", createPosition("cto", "CTO", "高管", "技术部", 10, Integer.MAX_VALUE,
                Arrays.asList("技术战略", "商业洞察", "团队建设", "行业视野"),
                Arrays.asList("技术战略制定", "团队管理", "投资人沟通", "行业影响力")));
    }
    
    private static CareerPathResponse.PositionInfo createPosition(String id, String name, String level, 
                                                                 String department, int minYears, int maxYears,
                                                                 List<String> skills, List<String> responsibilities) {
        CareerPathResponse.PositionInfo pos = new CareerPathResponse.PositionInfo();
        pos.setPositionId(id);
        pos.setPositionName(name);
        pos.setLevel(level);
        pos.setDepartment(department);
        pos.setMinYears(minYears);
        pos.setMaxYears(maxYears);
        pos.setRequiredSkills(skills);
        pos.setResponsibilities(responsibilities);
        return pos;
    }
    
    /**
     * 学习资源数据（模拟数据）
     */
    private static final Map<String, List<CareerPathResponse.LearningResource>> SKILL_RESOURCES = new HashMap<>();
    
    static {
        SKILL_RESOURCES.put("Java/Python基础", Arrays.asList(
                createResource("res-001", "Java核心技术卷I", "书籍", "https://example.com/java1", "机械工业出版社", 40, 4.8, "Java入门经典"),
                createResource("res-002", "Python编程从入门到实践", "书籍", "https://example.com/python1", "人民邮电出版社", 30, 4.7, "Python入门")
        ));
        
        SKILL_RESOURCES.put("分布式系统", Arrays.asList(
                createResource("res-003", "分布式系统设计模式", "书籍", "https://example.com/dist1", "O'Reilly", 35, 4.9, "分布式经典"),
                createResource("res-004", "分布式系统原理与实践", "课程", "https://example.com/course1", "极客时间", 20, 4.8, "系统课程")
        ));
        
        SKILL_RESOURCES.put("微服务架构", Arrays.asList(
                createResource("res-005", "微服务设计", "书籍", "https://example.com/microservice", "O'Reilly", 25, 4.7, "微服务设计原则"),
                createResource("res-006", "Spring Cloud实战", "课程", "https://example.com/springcloud", "慕课网", 30, 4.6, "Spring Cloud实践")
        ));
        
        SKILL_RESOURCES.put("Vue/React进阶", Arrays.asList(
                createResource("res-007", "深入浅出React和Redux", "书籍", "https://example.com/react1", "人民邮电出版社", 20, 4.6, "React进阶"),
                createResource("res-008", "Vue3完全指南", "课程", "https://example.com/vue3", "掘金小册", 15, 4.8, "Vue3详解")
        ));
        
        SKILL_RESOURCES.put("系统架构设计", Arrays.asList(
                createResource("res-009", "架构师修炼之道", "书籍", "https://example.com/arch1", "电子工业出版社", 35, 4.8, "架构设计"),
                createResource("res-010", "大型网站技术架构", "书籍", "https://example.com/arch2", "电子工业出版社", 30, 4.7, "大型系统架构")
        ));
        
        SKILL_RESOURCES.put("高并发处理", Arrays.asList(
                createResource("res-011", "高并发编程实战", "书籍", "https://example.com/concurrency", "机械工业出版社", 30, 4.7, "高并发"),
                createResource("res-012", "Redis设计与实现", "书籍", "https://example.com/redis", "机械工业出版社", 20, 4.9, "Redis深入")
        ));
        
        SKILL_RESOURCES.put("团队管理", Arrays.asList(
                createResource("res-013", "技术团队管理", "书籍", "https://example.com/team1", "人民邮电出版社", 25, 4.5, "技术管理"),
                createResource("res-014", "人月神话", "书籍", "https://example.com/myth", "清华大学出版社", 20, 4.8, "经典管理")
        ));
        
        SKILL_RESOURCES.put("项目管理", Arrays.asList(
                createResource("res-015", "项目管理知识体系指南", "书籍", "https://example.com/pmp", "电子工业出版社", 40, 4.6, "PMP备考"),
                createResource("res-016", "敏捷开发实战", "课程", "https://example.com/agile", "极客时间", 15, 4.7, "敏捷方法")
        ));
    }
    
    private static CareerPathResponse.LearningResource createResource(String id, String title, String type, 
                                                                      String url, String platform, int duration, 
                                                                      double rating, String desc) {
        CareerPathResponse.LearningResource res = new CareerPathResponse.LearningResource();
        res.setResourceId(id);
        res.setTitle(title);
        res.setType(type);
        res.setUrl(url);
        res.setPlatform(platform);
        res.setDurationHours(duration);
        res.setRating(rating);
        res.setDescription(desc);
        return res;
    }
    
    @Override
    public CareerPathResponse generatePath(CareerPathRequest request) {
        String currentId = request.getCurrentPositionId();
        String targetId = request.getTargetPositionId();
        Integer planMonths = request.getPlanMonths() != null ? request.getPlanMonths() : 24;
        
        CareerPathResponse.PositionInfo currentPos = getPosition(currentId);
        CareerPathResponse.PositionInfo targetPos = getPosition(targetId);
        
        CareerPathResponse response = new CareerPathResponse();
        response.setCurrentPosition(currentPos);
        response.setTargetPosition(targetPos);
        response.setPlanMonths(planMonths);
        
        // 生成发展阶段
        response.setStages(generateStages(currentPos, targetPos, planMonths));
        
        // 技能差距分析
        response.setSkillGap(analyzeSkillGap(currentPos, targetPos));
        
        // 获取学习资源
        CareerPathResponse.SkillGapAnalysis skillGap = response.getSkillGap();
        List<String> allSkills = new ArrayList<>();
        if (skillGap.getMissingSkills() != null) {
            allSkills.addAll(skillGap.getMissingSkills());
        }
        if (skillGap.getToImproveSkills() != null) {
            allSkills.addAll(skillGap.getToImproveSkills());
        }
        response.setResources(getResources(allSkills));
        
        return response;
    }
    
    /**
     * 生成发展阶段
     */
    private List<CareerPathResponse.PathStage> generateStages(CareerPathResponse.PositionInfo current, 
                                                             CareerPathResponse.PositionInfo target, 
                                                             int totalMonths) {
        List<CareerPathResponse.PathStage> stages = new ArrayList<>();
        
        // 根据职位级别差确定阶段数
        int currentLevel = getLevelScore(current.getLevel());
        int targetLevel = getLevelScore(target.getLevel());
        int levelDiff = targetLevel - currentLevel;
        int stageCount = Math.max(2, levelDiff + 1);
        
        int monthsPerStage = totalMonths / stageCount;
        
        String[] stageTitles = {"基础巩固期", "技能提升期", "能力突破期", "进阶冲刺期", "目标达成期"};
        
        for (int i = 0; i < stageCount; i++) {
            CareerPathResponse.PathStage stage = new CareerPathResponse.PathStage();
            stage.setStageNumber(i + 1);
            stage.setTitle(stageTitles[Math.min(i, stageTitles.length - 1)]);
            stage.setDurationMonths(i == stageCount - 1 ? totalMonths - i * monthsPerStage : monthsPerStage);
            
            // 设置阶段目标
            List<String> objectives = new ArrayList<>();
            List<String> keySkills = new ArrayList<>();
            
            if (i == 0) {
                objectives.add("巩固当前技能");
                objectives.add("深入理解现有业务");
                objectives.add("建立良好工作习惯");
                keySkills.addAll(current.getRequiredSkills().subList(0, Math.min(2, current.getRequiredSkills().size())));
            } else if (i == stageCount - 1) {
                objectives.add("冲刺目标职位要求");
                objectives.add("准备晋升答辩");
                objectives.add("拓展人脉资源");
                keySkills.addAll(target.getRequiredSkills().subList(0, Math.min(2, target.getRequiredSkills().size())));
            } else {
                objectives.add("学习进阶技能");
                objectives.add("承担更复杂任务");
                objectives.add("积累项目经验");
                int skillIdx = (i * current.getRequiredSkills().size()) / stageCount;
                keySkills.addAll(current.getRequiredSkills().subList(Math.min(skillIdx, current.getRequiredSkills().size() - 1), 
                        Math.min(skillIdx + 2, current.getRequiredSkills().size())));
            }
            
            stage.setObjectives(objectives);
            stage.setKeySkills(keySkills);
            stage.setMilestone("完成阶段" + (i + 1) + "目标");
            
            stages.add(stage);
        }
        
        return stages;
    }
    
    /**
     * 获取级别分数
     */
    private int getLevelScore(String level) {
        switch (level) {
            case "初级": return 1;
            case "中级": return 2;
            case "高级": return 3;
            case "Lead": return 4;
            case "管理": return 5;
            case "高管": return 6;
            default: return 1;
        }
    }
    
    /**
     * 技能差距分析
     */
    private CareerPathResponse.SkillGapAnalysis analyzeSkillGap(CareerPathResponse.PositionInfo current, 
                                                               CareerPathResponse.PositionInfo target) {
        CareerPathResponse.SkillGapAnalysis gap = new CareerPathResponse.SkillGapAnalysis();
        
        Set<String> currentSkills = new HashSet<>(current.getRequiredSkills());
        Set<String> targetSkills = new HashSet<>(target.getRequiredSkills());
        
        // 缺失技能
        List<String> missingSkills = targetSkills.stream()
                .filter(s -> !currentSkills.contains(s))
                .collect(Collectors.toList());
        
        // 需要提升的技能（共有但需要进阶）
        List<String> toImproveSkills = currentSkills.stream()
                .filter(targetSkills::contains)
                .collect(Collectors.toList());
        
        gap.setCurrentSkills(new ArrayList<>(currentSkills));
        gap.setTargetSkills(new ArrayList<>(targetSkills));
        gap.setMissingSkills(missingSkills);
        gap.setToImproveSkills(toImproveSkills);
        
        return gap;
    }
    
    @Override
    public List<CareerPathResponse.PositionInfo> getAllPositions() {
        return new ArrayList<>(POSITIONS.values());
    }
    
    @Override
    public CareerPathResponse.PositionInfo getPosition(String positionId) {
        if (positionId == null || !POSITIONS.containsKey(positionId)) {
            // 默认返回初级开发工程师
            return POSITIONS.get("junior-dev");
        }
        return POSITIONS.get(positionId);
    }
    
    @Override
    public List<CareerPathResponse.LearningResource> getResources(List<String> skillNames) {
        Set<CareerPathResponse.LearningResource> resources = new HashSet<>();
        
        if (skillNames != null) {
            for (String skill : skillNames) {
                List<CareerPathResponse.LearningResource> skillResources = SKILL_RESOURCES.get(skill);
                if (skillResources != null) {
                    resources.addAll(skillResources);
                }
            }
        }
        
        // 如果没有匹配资源，返回一些通用资源
        if (resources.isEmpty()) {
            resources.addAll(Arrays.asList(
                    createResource("res-def-001", "代码整洁之道", "书籍", "https://example.com/clean", "人民邮电出版社", 15, 4.8, "代码质量"),
                    createResource("res-def-002", "程序员修炼之道", "书籍", "https://example.com/pragmatic", "电子工业出版社", 20, 4.7, "职业发展")
            ));
        }
        
        return new ArrayList<>(resources);
    }
}