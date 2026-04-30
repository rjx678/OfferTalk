-- ============================================
-- OfferTalk求职爆料小程序 - MySQL数据库建表脚本
-- 数据库版本: MySQL 8.0
-- 创建时间: 2024年
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS offertalk DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE offertalk;

-- ============================================
-- 1. 用户表 - 存储微信小程序用户信息
-- ============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `openid` VARCHAR(64) NOT NULL COMMENT '微信OpenID',
    `unionid` VARCHAR(64) DEFAULT NULL COMMENT '微信UnionID',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT '用户昵称',
    `avatar_url` VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
    `bio` VARCHAR(255) DEFAULT NULL COMMENT '个人简介',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '账号状态: 0-禁用 1-正常',
    `user_type` TINYINT NOT NULL DEFAULT 1 COMMENT '用户类型: 1-普通用户 2-认证用户 3-管理员',
    `truth_score` DECIMAL(3,2) DEFAULT NULL COMMENT '用户真实度评分',
    `total_post_count` INT DEFAULT 0 COMMENT '发布内容总数',
    `total_like_count` INT DEFAULT 0 COMMENT '获赞总数',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    KEY `idx_unionid` (`unionid`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 2. 公司信息表 - 存储企业基本信息
-- ============================================
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公司ID',
    `name` VARCHAR(100) NOT NULL COMMENT '公司名称',
    `short_name` VARCHAR(50) DEFAULT NULL COMMENT '公司简称',
    `logo_url` VARCHAR(512) DEFAULT NULL COMMENT '公司Logo URL',
    `industry` VARCHAR(50) DEFAULT NULL COMMENT '所属行业',
    `scale` VARCHAR(20) DEFAULT NULL COMMENT '公司规模: 初创/中小型/大型/上市公司',
    `stage` VARCHAR(30) DEFAULT NULL COMMENT '融资阶段: 未融资/A轮/B轮/C轮/上市公司',
    `website` VARCHAR(255) DEFAULT NULL COMMENT '公司官网',
    `city` VARCHAR(30) DEFAULT NULL COMMENT '总部城市',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
    `description` TEXT DEFAULT NULL COMMENT '公司简介',
    `tags` VARCHAR(255) DEFAULT NULL COMMENT '公司标签: 大厂/外企/国企/独角兽等',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架 1-上架',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `post_count` INT DEFAULT 0 COMMENT '内容总数',
    `interview_count` INT DEFAULT 0 COMMENT '面经总数',
    `salary_count` INT DEFAULT 0 COMMENT '薪资爆料总数',
    `review_count` INT DEFAULT 0 COMMENT '评价总数',
    `rating_total` DECIMAL(2,1) DEFAULT 0.0 COMMENT '综合评分',
    `rating_salary` DECIMAL(2,1) DEFAULT 0.0 COMMENT '薪资满意度',
    `rating_culture` DECIMAL(2,1) DEFAULT 0.0 COMMENT '企业文化',
    `rating_overtime` DECIMAL(2,1) DEFAULT 0.0 COMMENT '加班强度',
    `rating_promotion` DECIMAL(2,1) DEFAULT 0.0 COMMENT '晋升空间',
    `rating_interview` DECIMAL(2,1) DEFAULT 0.0 COMMENT '面试难度',
    `difficulty_level` VARCHAR(10) DEFAULT NULL COMMENT '面试难度等级: 简单/中等/困难',
    `overtime_level` VARCHAR(10) DEFAULT NULL COMMENT '加班等级:955/996/007',
    `blacklist_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '黑名单标志: 0-正常 1-黑名单',
    `sort_order` INT DEFAULT 0 COMMENT '排序权重',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删除 1-已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    KEY `idx_industry` (`industry`),
    KEY `idx_city` (`city`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_rating` (`rating_total`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司信息表';

-- ============================================
-- 3. 岗位信息表 - 存储招聘岗位信息
-- ============================================
DROP TABLE IF EXISTS `job_position`;
CREATE TABLE `job_position` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `name` VARCHAR(100) NOT NULL COMMENT '岗位名称',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '岗位类别: 前端/后端/移动端/测试/运维/算法等',
    `job_type` TINYINT NOT NULL COMMENT '招聘类型: 1-校招 2-社招 3-实习',
    `city` VARCHAR(30) DEFAULT NULL COMMENT '工作城市',
    `experience` VARCHAR(20) DEFAULT NULL COMMENT '经验要求',
    `education` VARCHAR(20) DEFAULT NULL COMMENT '学历要求',
    `salary_range` VARCHAR(50) DEFAULT NULL COMMENT '薪资范围',
    `description` TEXT DEFAULT NULL COMMENT '岗位描述',
    `requirement` TEXT DEFAULT NULL COMMENT '岗位要求',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架 1-上架',
    `post_count` INT DEFAULT 0 COMMENT '相关面经数量',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_company_id` (`company_id`),
    KEY `idx_job_type` (`job_type`),
    KEY `idx_city` (`city`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位信息表';

-- ============================================
-- 4. 面经内容主表 - 存储面试经验分享
-- ============================================
DROP TABLE IF EXISTS `interview_experience`;
CREATE TABLE `interview_experience` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '面经ID',
    `user_id` BIGINT NOT NULL COMMENT '发布用户ID',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `position_id` BIGINT DEFAULT NULL COMMENT '岗位ID',
    `title` VARCHAR(200) NOT NULL COMMENT '面经标题',
    `recruit_type` TINYINT NOT NULL COMMENT '招聘类型: 1-校招 2-社招 3-实习',
    `job_category` VARCHAR(50) DEFAULT NULL COMMENT '岗位类别',
    `city` VARCHAR(30) DEFAULT NULL COMMENT '工作城市',
    `status` VARCHAR(20) DEFAULT NULL COMMENT '面试结果: 面试中/已通过/已拒绝/Offer/未通过',
    `difficulty` TINYINT DEFAULT NULL COMMENT '难度评分 1-5',
    `experience_text` TEXT DEFAULT NULL COMMENT '面试经验正文',
    `timeline_data` JSON DEFAULT NULL COMMENT 'Timeline时间线JSON数据',
    `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签: 大厂/外企/避雷等',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `collect_count` INT DEFAULT 0 COMMENT '收藏数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `report_count` INT DEFAULT 0 COMMENT '举报数',
    `share_count` INT DEFAULT 0 COMMENT '分享数',
    `truth_score` DECIMAL(3,2) DEFAULT 5.00 COMMENT '真实度评分',
    `truth_score_count` INT DEFAULT 0 COMMENT '评分次数',
    `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态: 0-待审核 1-审核通过 2-审核拒绝 3-人工复审',
    `audit_remark` VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
    `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `is_anonymous` TINYINT NOT NULL DEFAULT 1 COMMENT '是否匿名: 0-实名 1-匿名',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶: 0-否 1-是',
    `is_hot` TINYINT NOT NULL DEFAULT 0 COMMENT '是否热门: 0-否 1-是',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
    `delete_reason` VARCHAR(255) DEFAULT NULL COMMENT '删除原因',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删除 1-已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_company_id` (`company_id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_recruit_type` (`recruit_type`),
    KEY `idx_city` (`city`),
    KEY `idx_status` (`status`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_truth_score` (`truth_score`),
    KEY `idx_like_count` (`like_count`),
    KEY `idx_view_count` (`view_count`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_is_hot` (`is_hot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='面经内容主表';

-- ============================================
-- 5. 面经时间线明细表 - 面试各环节详情
-- ============================================
DROP TABLE IF EXISTS `interview_timeline`;
CREATE TABLE `interview_timeline` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '时间线ID',
    `experience_id` BIGINT NOT NULL COMMENT '面经ID',
    `stage` VARCHAR(50) NOT NULL COMMENT '面试阶段: 投递/笔试/一面/二面/三面/HR面/终面/Offer',
    `stage_order` TINYINT NOT NULL COMMENT '阶段顺序',
    `interview_date` DATE DEFAULT NULL COMMENT '面试日期',
    `interview_type` VARCHAR(20) DEFAULT NULL COMMENT '面试形式: 线上/线下/电话',
    `duration` VARCHAR(30) DEFAULT NULL COMMENT '面试时长',
    `interviewer_type` VARCHAR(30) DEFAULT NULL COMMENT '面试官类型: 技术/HR/主管/CEO',
    `content` TEXT DEFAULT NULL COMMENT '面试内容',
    `algorithm_questions` TEXT DEFAULT NULL COMMENT '算法题目',
    `system_design` TEXT DEFAULT NULL COMMENT '系统设计题目',
    `behavior_questions` TEXT DEFAULT NULL COMMENT '行为面试问题',
    `result` VARCHAR(20) DEFAULT NULL COMMENT '本轮结果: 通过/未通过/待定',
    `result_note` VARCHAR(255) DEFAULT NULL COMMENT '结果备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_experience_id` (`experience_id`),
    KEY `idx_stage_order` (`stage_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='面经时间线明细表';

-- ============================================
-- 6. 薪资爆料表 - 结构化薪资信息
-- ============================================
DROP TABLE IF EXISTS `salary_disclosure`;
CREATE TABLE `salary_disclosure` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '薪资ID',
    `user_id` BIGINT NOT NULL COMMENT '发布用户ID',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `position_id` BIGINT DEFAULT NULL COMMENT '岗位ID',
    `title` VARCHAR(200) NOT NULL COMMENT '爆料标题',
    `recruit_type` TINYINT NOT NULL COMMENT '招聘类型: 1-校招 2-社招 3-实习',
    `job_category` VARCHAR(50) DEFAULT NULL COMMENT '岗位类别',
    `city` VARCHAR(30) DEFAULT NULL COMMENT '工作城市',
    `level` VARCHAR(50) DEFAULT NULL COMMENT '职级/级别',
    `working_years` INT DEFAULT NULL COMMENT '工作年限',
    `entry_date` DATE DEFAULT NULL COMMENT '入职时间',
    `monthly_base` DECIMAL(12,2) DEFAULT NULL COMMENT '月薪(元)',
    `annual_base` DECIMAL(12,2) DEFAULT NULL COMMENT '年度基本工资',
    `total_package` DECIMAL(12,2) DEFAULT NULL COMMENT '年度总包(含奖金股票)',
    `signing_bonus` DECIMAL(12,2) DEFAULT NULL COMMENT '签约奖金',
    `annual_bonus` DECIMAL(12,2) DEFAULT NULL COMMENT '年终奖(月份)',
    `stock_options` VARCHAR(100) DEFAULT NULL COMMENT '股票期权详情',
    `stock_value` DECIMAL(12,2) DEFAULT NULL COMMENT '股票价值(元)',
    `meal_allowance` DECIMAL(8,2) DEFAULT NULL COMMENT '餐补(元/月)',
    `housing_allowance` DECIMAL(8,2) DEFAULT NULL COMMENT '房补(元/月)',
    `transport_allowance` DECIMAL(8,2) DEFAULT NULL COMMENT '交通补贴(元/月)',
    `other_allowance` DECIMAL(8,2) DEFAULT NULL COMMENT '其他补贴(元/月)',
    `social_insurance_ratio` VARCHAR(20) DEFAULT NULL COMMENT '社保缴纳比例',
    `housing_fund_ratio` VARCHAR(20) DEFAULT NULL COMMENT '公积金缴纳比例',
    `overtime_situation` VARCHAR(100) DEFAULT NULL COMMENT '加班情况',
    `probation_period` VARCHAR(50) DEFAULT NULL COMMENT '试用期时长及待遇',
    `probation_salary_ratio` DECIMAL(5,2) DEFAULT NULL COMMENT '试用期工资比例',
    `wage_level` VARCHAR(20) DEFAULT NULL COMMENT '薪资倒挂等级: 正常/倒挂严重',
    `add_text` TEXT DEFAULT NULL COMMENT '文字补充(吐槽/建议)',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `collect_count` INT DEFAULT 0 COMMENT '收藏数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `report_count` INT DEFAULT 0 COMMENT '举报数',
    `truth_score` DECIMAL(3,2) DEFAULT 5.00 COMMENT '真实度评分',
    `truth_score_count` INT DEFAULT 0 COMMENT '评分次数',
    `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态',
    `audit_remark` VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
    `is_anonymous` TINYINT NOT NULL DEFAULT 1 COMMENT '是否匿名',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶',
    `is_hot` TINYINT NOT NULL DEFAULT 0 COMMENT '是否热门',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删除 1-已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_company_id` (`company_id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_recruit_type` (`recruit_type`),
    KEY `idx_city` (`city`),
    KEY `idx_total_package` (`total_package`),
    KEY `idx_monthly_base` (`monthly_base`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_truth_score` (`truth_score`),
    KEY `idx_like_count` (`like_count`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='薪资爆料表';

-- ============================================
-- 7. 公司评价表 - 用户对公司的评价
-- ============================================
DROP TABLE IF EXISTS `company_review`;
CREATE TABLE `company_review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `user_id` BIGINT NOT NULL COMMENT '发布用户ID',
    `company_id` BIGINT NOT NULL COMMENT '公司ID',
    `title` VARCHAR(200) NOT NULL COMMENT '评价标题',
    `recruit_type` TINYINT NOT NULL COMMENT '用户身份: 1-在职 2-离职 3-面试过',
    `job_category` VARCHAR(50) DEFAULT NULL COMMENT '岗位类别',
    `city` VARCHAR(30) DEFAULT NULL COMMENT '工作城市',
    `tenure_years` DECIMAL(4,1) DEFAULT NULL COMMENT '在职时长(年)',
    `is_currently_employed` TINYINT DEFAULT NULL COMMENT '是否在职',
    `pros_text` TEXT DEFAULT NULL COMMENT '优点描述',
    `cons_text` TEXT DEFAULT NULL COMMENT '缺点描述',
    `rating_total` DECIMAL(2,1) NOT NULL COMMENT '综合评分 1-5',
    `rating_salary` DECIMAL(2,1) DEFAULT NULL COMMENT '薪资满意度 1-5',
    `rating_culture` DECIMAL(2,1) DEFAULT NULL COMMENT '企业文化 1-5',
    `rating_overtime` DECIMAL(2,1) DEFAULT NULL COMMENT '加班强度 1-5',
    `rating_promotion` DECIMAL(2,1) DEFAULT NULL COMMENT '晋升空间 1-5',
    `rating_management` DECIMAL(2,1) DEFAULT NULL COMMENT '管理水平 1-5',
    `overtime_frequency` VARCHAR(20) DEFAULT NULL COMMENT '加班频率: 从不/偶尔/经常/强制',
    `overtime_salary` VARCHAR(20) DEFAULT NULL COMMENT '加班薪资: 无薪/调休/给薪',
    `layoff_situation` VARCHAR(100) DEFAULT NULL COMMENT '裁员情况',
    `internship_exp` TEXT DEFAULT NULL COMMENT '实习体验',
    `advice_to_company` TEXT DEFAULT NULL COMMENT '给公司的建议',
    `tags` VARCHAR(255) DEFAULT NULL COMMENT '评价标签: 避雷/推荐/996/955等',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `collect_count` INT DEFAULT 0 COMMENT '收藏数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `report_count` INT DEFAULT 0 COMMENT '举报数',
    `truth_score` DECIMAL(3,2) DEFAULT 5.00 COMMENT '真实度评分',
    `truth_score_count` INT DEFAULT 0 COMMENT '评分次数',
    `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态',
    `is_anonymous` TINYINT NOT NULL DEFAULT 1 COMMENT '是否匿名',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶',
    `is_hot` TINYINT NOT NULL DEFAULT 0 COMMENT '是否热门',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删除 1-已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_company_id` (`company_id`),
    KEY `idx_recruit_type` (`recruit_type`),
    KEY `idx_rating_total` (`rating_total`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_truth_score` (`truth_score`),
    KEY `idx_like_count` (`like_count`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司评价表';

-- ============================================
-- 8. 评论表 - 用户评论互动
-- ============================================
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
    `content_type` TINYINT NOT NULL COMMENT '内容类型: 1-面经 2-薪资 3-评价',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID(回复)',
    `root_id` BIGINT DEFAULT NULL COMMENT '根评论ID',
    `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `report_count` INT DEFAULT 0 COMMENT '举报数',
    `audit_status` TINYINT NOT NULL DEFAULT 1 COMMENT '审核状态: 0-待审核 1-通过 2-拒绝',
    `is_anonymous` TINYINT NOT NULL DEFAULT 1 COMMENT '是否匿名',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `device_info` VARCHAR(100) DEFAULT NULL COMMENT '设备信息',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删 1-已删',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_content` (`content_type`, `content_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_root_id` (`root_id`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ============================================
-- 9. 点赞记录表
-- ============================================
DROP TABLE IF EXISTS `like_record`;
CREATE TABLE `like_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content_type` TINYINT NOT NULL COMMENT '内容类型: 1-面经 2-薪资 3-评价 4-评论',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-取消 1-点赞',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删除 1-已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_content` (`user_id`, `content_type`, `content_id`),
    KEY `idx_content` (`content_type`, `content_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞记录表';

-- ============================================
-- 10. 收藏记录表
-- ============================================
DROP TABLE IF EXISTS `collect_record`;
CREATE TABLE `collect_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content_type` TINYINT NOT NULL COMMENT '内容类型: 1-面经 2-薪资 3-评价',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-取消 1-收藏',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删除 1-已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_content` (`user_id`, `content_type`, `content_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_content` (`content_type`, `content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏记录表';

-- ============================================
-- 11. 浏览记录表
-- ============================================
DROP TABLE IF EXISTS `browse_history`;
CREATE TABLE `browse_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content_type` TINYINT NOT NULL COMMENT '内容类型: 1-面经 2-薪资 3-评价',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `browse_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    `source` VARCHAR(50) DEFAULT NULL COMMENT '来源: 首页/搜索/推荐等',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_content` (`content_type`, `content_id`),
    KEY `idx_browse_time` (`browse_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览记录表';

-- ============================================
-- 12. 举报记录表
-- ============================================
DROP TABLE IF EXISTS `report_record`;
CREATE TABLE `report_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '举报ID',
    `user_id` BIGINT NOT NULL COMMENT '举报用户ID',
    `content_type` TINYINT NOT NULL COMMENT '内容类型: 1-面经 2-薪资 3-评价 4-评论',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `report_type` TINYINT NOT NULL COMMENT '举报类型: 1-虚假信息 2-广告 3-人身攻击 4-敏感内容 5-其他',
    `report_reason` VARCHAR(500) DEFAULT NULL COMMENT '举报详细原因',
    `evidence_urls` VARCHAR(1000) DEFAULT NULL COMMENT '证据截图URLs',
    `contact_info` VARCHAR(100) DEFAULT NULL COMMENT '联系方式',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态: 0-待处理 1-已处理 2-误报 3-恶意举报',
    `handle_remark` VARCHAR(255) DEFAULT NULL COMMENT '处理备注',
    `handle_user_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_content` (`content_type`, `content_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='举报记录表';

-- ============================================
-- 13. 敏感词配置表
-- ============================================
DROP TABLE IF EXISTS `sensitive_word`;
CREATE TABLE `sensitive_word` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `word` VARCHAR(100) NOT NULL COMMENT '敏感词',
    `word_type` TINYINT NOT NULL COMMENT '类型: 1-政治敏感 2-色情低俗 3-暴力恐怖 4-违法犯罪 5-广告推广 6-其他',
    `level` TINYINT NOT NULL DEFAULT 1 COMMENT '敏感等级: 1-警告 2-替换 3-拦截',
    `replace_word` VARCHAR(50) DEFAULT NULL COMMENT '替换词',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志: 0-未删除 1-已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_word` (`word`),
    KEY `idx_word_type` (`word_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='敏感词配置表';

-- ============================================
-- 14. 系统配置表
-- ============================================
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT DEFAULT NULL COMMENT '配置值',
    `config_name` VARCHAR(100) DEFAULT NULL COMMENT '配置名称',
    `config_type` VARCHAR(50) DEFAULT NULL COMMENT '配置类型',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '配置描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`),
    KEY `idx_config_type` (`config_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ============================================
-- 15. 内容审核记录表
-- ============================================
DROP TABLE IF EXISTS `audit_record`;
CREATE TABLE `audit_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '审核ID',
    `content_type` TINYINT NOT NULL COMMENT '内容类型: 1-面经 2-薪资 3-评价 4-评论',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `audit_type` TINYINT NOT NULL COMMENT '审核类型: 1-机器初审 2-人工复审',
    `audit_status` TINYINT NOT NULL COMMENT '审核结果: 0-待审核 1-通过 2-拒绝 3-人工复审',
    `audit_remark` VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
    `risk_words` VARCHAR(500) DEFAULT NULL COMMENT '风险词列表',
    `risk_score` DECIMAL(5,2) DEFAULT NULL COMMENT '风险评分',
    `audit_user_id` BIGINT DEFAULT NULL COMMENT '审核人ID',
    `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_content` (`content_type`, `content_id`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_audit_time` (`audit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容审核记录表';

-- ============================================
-- 16. 爬虫数据源配置表
-- ============================================
DROP TABLE IF EXISTS `crawler_source`;
CREATE TABLE `crawler_source` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '数据源ID',
    `source_name` VARCHAR(100) NOT NULL COMMENT '数据源名称',
    `source_url` VARCHAR(500) NOT NULL COMMENT '数据源URL',
    `source_type` VARCHAR(50) DEFAULT NULL COMMENT '数据源类型: 牛客/Offershow/脉脉等',
    `content_type` TINYINT DEFAULT NULL COMMENT '采集内容类型: 1-面经 2-薪资 3-评价',
    `cookie_required` TINYINT DEFAULT 0 COMMENT '是否需要Cookie: 0-不需要 1-需要',
    `login_url` VARCHAR(255) DEFAULT NULL COMMENT '登录URL',
    `headers` TEXT DEFAULT NULL COMMENT '请求头配置',
    `params` TEXT DEFAULT NULL COMMENT '请求参数配置',
    `interval_minutes` INT DEFAULT 60 COMMENT '采集间隔(分钟)',
    `last_crawl_time` DATETIME DEFAULT NULL COMMENT '最后采集时间',
    `last_crawl_status` TINYINT DEFAULT NULL COMMENT '最后采集状态: 0-失败 1-成功',
    `last_crawl_count` INT DEFAULT 0 COMMENT '最后采集数量',
    `crawl_rule` TEXT DEFAULT NULL COMMENT '采集规则JSON',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_source_type` (`source_type`),
    KEY `idx_content_type` (`content_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='爬虫数据源配置表';

-- ============================================
-- 17. 爬虫采集记录表
-- ============================================
DROP TABLE IF EXISTS `crawler_record`;
CREATE TABLE `crawler_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '采集记录ID',
    `source_id` BIGINT NOT NULL COMMENT '数据源ID',
    `content_type` TINYINT NOT NULL COMMENT '内容类型',
    `original_id` VARCHAR(100) NOT NULL COMMENT '原平台内容ID',
    `original_url` VARCHAR(500) DEFAULT NULL COMMENT '原文URL',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '标题',
    `content_hash` VARCHAR(64) DEFAULT NULL COMMENT '内容哈希(去重)',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待处理 1-已采集 2-已去重 3-已入库 4-已拒绝',
    `crawl_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '采集时间',
    `process_remark` VARCHAR(255) DEFAULT NULL COMMENT '处理备注',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_source_id` (`source_id`),
    KEY `idx_content_hash` (`content_hash`),
    KEY `idx_status` (`status`),
    KEY `idx_crawl_time` (`crawl_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='爬虫采集记录表';

-- ============================================
-- 18. 用户反馈表
-- ============================================
DROP TABLE IF EXISTS `user_feedback`;
CREATE TABLE `user_feedback` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
    `user_id` BIGINT NOT NULL COMMENT '反馈用户ID',
    `feedback_type` TINYINT NOT NULL COMMENT '反馈类型: 1-功能建议 2-BUG反馈 3-内容纠错 4-投诉建议',
    `content` TEXT NOT NULL COMMENT '反馈内容',
    `contact_info` VARCHAR(100) DEFAULT NULL COMMENT '联系方式',
    `images` VARCHAR(1000) DEFAULT NULL COMMENT '截图URLs',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态: 0-待处理 1-处理中 2-已处理 3-已回复',
    `handle_remark` VARCHAR(255) DEFAULT NULL COMMENT '处理备注',
    `handle_user_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `reply_content` TEXT DEFAULT NULL COMMENT '回复内容',
    `reply_time` DATETIME DEFAULT NULL COMMENT '回复时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_feedback_type` (`feedback_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户反馈表';

-- ============================================
-- 19. 通知消息表
-- ============================================
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `title` VARCHAR(100) NOT NULL COMMENT '通知标题',
    `content` TEXT DEFAULT NULL COMMENT '通知内容',
    `type` TINYINT NOT NULL COMMENT '通知类型: 1-系统通知 2-审核通知 3-互动通知',
    `related_type` TINYINT DEFAULT NULL COMMENT '关联类型: 1-面经 2-薪资 3-评价 4-评论',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联内容ID',
    `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读: 0-未读 1-已读',
    `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_type` (`type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知消息表';

-- ============================================
-- 20. 每日统计表
-- ============================================
DROP TABLE IF EXISTS `daily_statistics`;
CREATE TABLE `daily_statistics` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '统计ID',
    `stat_date` DATE NOT NULL COMMENT '统计日期',
    `content_type` TINYINT DEFAULT NULL COMMENT '内容类型: 1-面经 2-薪资 3-评价',
    `new_count` INT DEFAULT 0 COMMENT '新增数量',
    `audit_pass_count` INT DEFAULT 0 COMMENT '审核通过数量',
    `audit_reject_count` INT DEFAULT 0 COMMENT '审核拒绝数量',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `share_count` INT DEFAULT 0 COMMENT '分享数',
    `report_count` INT DEFAULT 0 COMMENT '举报数量',
    `user_active_count` INT DEFAULT 0 COMMENT '活跃用户数',
    `crawler_count` INT DEFAULT 0 COMMENT '爬虫采集数量',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_date_type` (`stat_date`, `content_type`),
    KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日统计表';

-- ============================================
-- 初始化默认系统配置
-- ============================================
INSERT INTO `system_config` (`config_key`, `config_value`, `config_name`, `config_type`, `description`) VALUES
('site_name', 'OfferTalk求职爆料', '网站名称', 'basic', '小程序名称'),
('site_version', '1.0.0', '版本号', 'basic', '当前版本'),
('disclaimer', '所有面经、薪资、公司评价均为用户自主分享与网络公开整理，仅作求职参考，不构成任何就业、择业建议。', '免责声明', 'legal', '全局免责声明'),
('privacy_policy', '', '隐私政策', 'legal', '隐私政策URL'),
('user_agreement', '', '用户协议', 'legal', '用户协议URL'),
('interview_audit_auto', 'true', '面经自动审核', 'audit', '是否开启面经自动审核'),
('salary_audit_auto', 'true', '薪资自动审核', 'audit', '是否开启薪资自动审核'),
('review_audit_auto', 'true', '评价自动审核', 'audit', '是否开启评价自动审核'),
('sensitive_word_filter', 'true', '敏感词过滤', 'filter', '是否开启敏感词过滤'),
('truth_score_weight_user', '0.4', '用户评分权重', 'algorithm', '真实度评分中用户评分权重'),
('truth_score_weight_audit', '0.3', '审核状态权重', 'algorithm', '真实度评分中审核状态权重'),
('truth_score_weight_content', '0.3', '内容完整度权重', 'algorithm', '真实度评分中内容完整度权重'),
('hot_score_weight_view', '1', '浏览量热度权重', 'algorithm', '热度评分中浏览量权重'),
('hot_score_weight_like', '2', '点赞热度权重', 'algorithm', '热度评分中点赞权重'),
('hot_score_weight_comment', '3', '评论热度权重', 'algorithm', '热度评分中评论权重'),
('crawler_enabled', 'false', '爬虫开关', 'crawler', '是否启用定时爬虫'),
('crawler_interval_minutes', '60', '爬虫间隔', 'crawler', '爬虫执行间隔(分钟)');

-- ============================================
-- 初始化示例公司数据
-- ============================================
INSERT INTO `company` (`name`, `short_name`, `logo_url`, `industry`, `scale`, `stage`, `website`, `city`, `address`, `description`, `tags`, `status`, `rating_total`, `rating_salary`, `rating_culture`, `rating_overtime`, `rating_promotion`, `rating_interview`, `difficulty_level`, `overtime_level`) VALUES
('腾讯', '腾讯', 'https://www.qq.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.tencent.com', '深圳', '广东省深圳市南山区科技园', '腾讯是中国领先的互联网科技公司，提供社交、游戏、金融等多种服务。', '大厂,上市公司,福利好', 1, 4.5, 4.3, 4.6, 3.5, 4.0, 4.2, '中等', '965'),
('字节跳动', '字节', 'https://www.bytedance.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.bytedance.com', '北京', '北京市海淀区知春路', '字节跳动是全球领先的内容平台，拥有抖音、TikTok等产品。', '大厂,上市公司,发展快', 1, 4.7, 4.8, 4.2, 2.5, 4.5, 4.8, '困难', '996'),
('阿里巴巴', '阿里', 'https://www.alibabagroup.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.alibaba.com', '杭州', '浙江省杭州市余杭区', '阿里巴巴是全球最大的电子商务公司之一，提供电商、云服务等。', '大厂,上市公司,平台大', 1, 4.4, 4.5, 4.3, 3.0, 4.2, 4.5, '中等', '996'),
('美团', '美团', 'https://www.meituan.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.meituan.com', '北京', '北京市朝阳区建国路', '美团是中国领先的本地生活服务平台，提供外卖、打车等服务。', '大厂,上市公司,本地生活', 1, 4.2, 4.0, 4.1, 3.8, 3.8, 4.0, '中等', '996'),
('京东', '京东', 'https://www.jd.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.jd.com', '北京', '北京市大兴区亦庄', '京东是中国领先的自营电商平台，以正品保障和快速配送著称。', '大厂,上市公司,电商', 1, 4.0, 4.2, 3.8, 4.2, 3.5, 3.8, '简单', '996'),
('华为', '华为', 'https://www.huawei.com/favicon.ico', '通信/硬件', '大型', '上市公司', 'https://www.huawei.com', '深圳', '广东省深圳市龙岗区', '华为是全球领先的信息与通信技术解决方案供应商。', '大厂,上市公司,狼性文化', 1, 4.3, 4.8, 3.8, 2.0, 4.0, 4.3, '困难', '007'),
('百度', '百度', 'https://www.baidu.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.baidu.com', '北京', '北京市海淀区上地', '百度是中国领先的搜索引擎和AI技术公司。', '大厂,上市公司,AI领先', 1, 4.1, 4.0, 4.0, 3.5, 3.8, 4.2, '中等', '965'),
('网易', '网易', 'https://www.163.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.163.com', '杭州', '浙江省杭州市滨江区', '网易是中国领先的互联网公司，在游戏、邮箱等领域处于领先地位。', '大厂,上市公司,游戏', 1, 4.4, 4.3, 4.5, 3.8, 4.0, 4.0, '简单', '965'),
('小米', '小米', 'https://www.xiaomi.com/favicon.ico', '互联网/硬件', '大型', '上市公司', 'https://www.mi.com', '北京', '北京市海淀区清河', '小米是全球领先的智能硬件和电子产品制造商。', '大厂,上市公司,性价比', 1, 4.2, 4.0, 4.3, 4.0, 3.8, 3.8, '简单', '965'),
('快手', '快手', 'https://www.kuaishou.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.kuaishou.com', '北京', '北京市海淀区中关村', '快手是中国领先的短视频平台，用户量超过3亿。', '大厂,上市公司,短视频', 1, 4.3, 4.5, 4.0, 3.2, 4.0, 4.5, '中等', '996'),
('拼多多', '拼多多', 'https://www.pinduoduo.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.pinduoduo.com', '上海', '上海市长宁区', '拼多多是中国领先的社交电商平台，以低价和拼团模式著称。', '大厂,上市公司,社交电商', 1, 4.0, 3.8, 3.9, 4.0, 3.5, 3.5, '简单', '996'),
('滴滴出行', '滴滴', 'https://www.didiglobal.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.didiglobal.com', '北京', '北京市海淀区中关村', '滴滴是中国领先的出行平台，提供网约车、共享单车等服务。', '大厂,上市公司,出行', 1, 4.1, 4.0, 3.8, 4.2, 3.6, 3.7, '中等', '996'),
('腾讯音乐', 'TME', 'https://www.tencentmusic.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.tencentmusic.com', '深圳', '广东省深圳市南山区', '腾讯音乐是中国领先的音乐流媒体平台，拥有QQ音乐等产品。', '大厂,上市公司,音乐', 1, 4.2, 4.1, 4.3, 3.8, 3.9, 4.0, '简单', '965'),
('爱奇艺', '爱奇艺', 'https://www.iqiyi.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.iqiyi.com', '北京', '北京市海淀区中关村', '爱奇艺是中国领先的视频流媒体平台，提供电影、电视剧等内容。', '大厂,上市公司,视频', 1, 4.0, 3.9, 4.0, 4.1, 3.7, 3.8, '中等', '996'),
('哔哩哔哩', 'B站', 'https://www.bilibili.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.bilibili.com', '上海', '上海市杨浦区', '哔哩哔哩是中国领先的年轻人文化社区，以弹幕视频著称。', '大厂,上市公司,年轻人', 1, 4.5, 4.2, 4.6, 3.9, 4.0, 4.1, '中等', '996'),
('蚂蚁集团', '蚂蚁', 'https://www.antgroup.com/favicon.ico', '金融科技', '大型', '独角兽', 'https://www.antgroup.com', '杭州', '浙江省杭州市余杭区', '蚂蚁集团是全球领先的金融科技公司，拥有支付宝等产品。', '大厂,独角兽,金融科技', 1, 4.4, 4.6, 4.2, 3.8, 4.1, 4.3, '困难', '996'),
('字节跳动电商', '字节电商', 'https://www.bytedance.com/favicon.ico', '互联网', '大型', '上市公司', 'https://www.douyin.com', '北京', '北京市海淀区知春路', '字节跳动电商是字节旗下的电商平台，依托抖音等产品。', '大厂,上市公司,电商', 1, 4.3, 4.4, 4.1, 3.5, 4.2, 4.4, '中等', '996'),
('腾讯云', '腾讯云', 'https://cloud.tencent.com/favicon.ico', '云计算', '大型', '上市公司', 'https://cloud.tencent.com', '深圳', '广东省深圳市南山区', '腾讯云是腾讯旗下的云计算服务提供商，提供IaaS、PaaS等服务。', '大厂,上市公司,云计算', 1, 4.2, 4.3, 4.0, 3.7, 3.9, 4.1, '中等', '965'),
('阿里云', '阿里云', 'https://www.aliyun.com/favicon.ico', '云计算', '大型', '上市公司', 'https://www.aliyun.com', '杭州', '浙江省杭州市余杭区', '阿里云是阿里巴巴旗下的云计算服务提供商，全球领先。', '大厂,上市公司,云计算', 1, 4.3, 4.4, 4.1, 3.8, 4.0, 4.2, '困难', '996'),
('商汤科技', '商汤', 'https://www.sensetime.com/favicon.ico', '人工智能', '大型', '独角兽', 'https://www.sensetime.com', '上海', '上海市徐汇区', '商汤科技是全球领先的人工智能公司，专注于计算机视觉技术。', '独角兽,AI,科技', 1, 4.5, 4.6, 4.3, 3.5, 4.2, 4.7, '困难', '996');

-- ============================================
-- 初始化示例薪资爆料数据
-- ============================================
INSERT INTO `salary_disclosure` (`user_id`, `company_id`, `title`, `recruit_type`, `job_category`, `city`, `level`, `working_years`, `monthly_base`, `total_package`, `signing_bonus`, `annual_bonus`, `overtime_situation`, `probation_period`, `add_text`, `audit_status`, `is_anonymous`) VALUES
(1, 1, '腾讯2026年校招前端开发薪资', 1, '前端', '深圳', 'T1', 0, 25000.00, 450000.00, 30000.00, 4.0, '965，偶尔加班', '6个月，80%薪资', '腾讯福利很好，食堂免费，健身房齐全', 1, 1),
(2, 2, '字节跳动2026年社招后端开发薪资', 2, '后端', '北京', 'P6', 3, 35000.00, 650000.00, 50000.00, 6.0, '996，经常加班', '3个月，100%薪资', '字节发展快，机会多，但压力大', 1, 1),
(3, 3, '阿里巴巴2026年校招算法工程师薪资', 1, '算法', '杭州', 'P5', 0, 30000.00, 550000.00, 40000.00, 5.0, '996，项目紧张时加班多', '6个月，80%薪资', '阿里平台大，技术氛围好', 1, 1),
(4, 4, '美团2026年社招产品经理薪资', 2, '产品', '北京', 'P6', 2, 28000.00, 500000.00, 30000.00, 4.0, '996，周末偶尔加班', '3个月，100%薪资', '美团业务多样，成长空间大', 1, 1),
(5, 5, '京东2026年校招测试工程师薪资', 1, '测试', '北京', 'T2', 0, 20000.00, 360000.00, 20000.00, 3.0, '996，电商大促时加班多', '6个月，80%薪资', '京东福利不错，有住房补贴', 1, 1),
(1, 6, '华为2026年社招硬件工程师薪资', 2, '硬件', '深圳', '15级', 4, 38000.00, 700000.00, 60000.00, 8.0, '007，压力大', '3个月，100%薪资', '华为技术强，平台好，但工作强度大', 1, 1),
(2, 7, '百度2026年校招AI工程师薪资', 1, 'AI', '北京', 'T3', 0, 32000.00, 580000.00, 45000.00, 5.0, '965，相对轻松', '6个月，80%薪资', '百度AI技术领先，学习机会多', 1, 1),
(3, 8, '网易2026年社招游戏策划薪资', 2, '游戏', '杭州', 'P6', 3, 30000.00, 550000.00, 35000.00, 6.0, '965，项目上线前加班', '3个月，100%薪资', '网易游戏氛围好，福利不错', 1, 1),
(4, 9, '小米2026年校招UI设计师薪资', 1, '设计', '北京', 'P5', 0, 22000.00, 400000.00, 25000.00, 3.0, '965，偶尔加班', '6个月，80%薪资', '小米扁平化管理，氛围轻松', 1, 1),
(5, 10, '快手2026年社招运营薪资', 2, '运营', '北京', 'P6', 2, 25000.00, 450000.00, 20000.00, 4.0, '996，活动期间加班多', '3个月，100%薪资', '快手用户增长快，机会多', 1, 1),
(1, 11, '拼多多2026年校招后端开发薪资', 1, '后端', '上海', 'P5', 0, 28000.00, 500000.00, 35000.00, 5.0, '996，压力大', '6个月，80%薪资', '拼多多发展快，晋升机会多', 1, 1),
(2, 12, '滴滴2026年社招数据分析师薪资', 2, '数据分析', '北京', 'P6', 3, 28000.00, 520000.00, 30000.00, 4.0, '996，偶尔加班', '3个月，100%薪资', '滴滴数据量大，分析机会多', 1, 1),
(3, 13, '腾讯音乐2026年校招产品运营薪资', 1, '运营', '深圳', 'T1', 0, 20000.00, 360000.00, 20000.00, 3.0, '965，相对轻松', '6个月，80%薪资', '腾讯音乐福利好，工作氛围不错', 1, 1),
(4, 14, '爱奇艺2026年社招前端开发薪资', 2, '前端', '北京', 'P6', 2, 26000.00, 480000.00, 25000.00, 4.0, '996，内容上线时加班', '3个月，100%薪资', '爱奇艺内容丰富，工作有趣', 1, 1),
(5, 15, '哔哩哔哩2026年校招游戏开发薪资', 1, '游戏', '上海', 'P5', 0, 25000.00, 450000.00, 30000.00, 4.0, '996，项目紧张时加班', '6个月，80%薪资', 'B站文化年轻，氛围好', 1, 1),
(1, 16, '蚂蚁集团2026年社招Java开发薪资', 2, '后端', '杭州', 'P7', 5, 40000.00, 750000.00, 60000.00, 8.0, '996，金融项目压力大', '3个月，100%薪资', '蚂蚁金融科技领先，技术挑战大', 1, 1),
(2, 17, '字节电商2026年校招产品经理薪资', 1, '产品', '北京', 'P5', 0, 26000.00, 480000.00, 35000.00, 4.0, '996，电商大促时加班', '6个月，80%薪资', '字节电商增长快，机会多', 1, 1),
(3, 18, '腾讯云2026年社招云计算工程师薪资', 2, '云计算', '深圳', 'T4', 4, 35000.00, 650000.00, 50000.00, 6.0, '965，偶尔加班', '3个月，100%薪资', '腾讯云技术强，发展空间大', 1, 1),
(4, 19, '阿里云2026年校招算法工程师薪资', 1, '算法', '杭州', 'P5', 0, 32000.00, 580000.00, 45000.00, 5.0, '996，项目紧张时加班', '6个月，80%薪资', '阿里云技术领先，平台大', 1, 1),
(5, 20, '商汤科技2026年社招AI研究员薪资', 2, 'AI', '上海', 'P6', 3, 38000.00, 700000.00, 60000.00, 6.0, '996，科研压力大', '3个月，100%薪资', '商汤AI技术前沿，挑战大', 1, 1);

-- ============================================
-- 初始化示例公司评价数据
-- ============================================
INSERT INTO `company_review` (`user_id`, `company_id`, `title`, `recruit_type`, `job_category`, `city`, `tenure_years`, `is_currently_employed`, `pros_text`, `cons_text`, `rating_total`, `rating_salary`, `rating_culture`, `rating_overtime`, `rating_promotion`, `rating_management`, `overtime_frequency`, `overtime_salary`, `tags`, `audit_status`, `is_anonymous`) VALUES
(1, 1, '腾讯工作体验：大厂福利与成长', 1, '前端', '深圳', 2.5, 1, '福利好，食堂免费，健身房齐全，技术氛围浓厚，成长空间大', '晋升竞争激烈，大公司流程繁琐', 4.5, 4.3, 4.6, 3.5, 4.0, 4.2, '偶尔', '调休', '大厂,福利好,技术氛围', 1, 1),
(2, 2, '字节跳动：快速成长与高压挑战', 2, '后端', '北京', 1.5, 1, '发展快，机会多，薪资高，技术栈先进', '工作强度大，996常态化，压力大', 4.3, 4.8, 4.2, 2.0, 4.5, 4.0, '经常', '调休', '大厂,发展快,压力大', 1, 1),
(3, 3, '阿里巴巴：平台大但内卷严重', 3, '算法', '杭州', 0.5, 0, '平台大，资源丰富，技术氛围好', '内卷严重，996，PIP压力大', 4.0, 4.5, 4.3, 2.5, 4.2, 3.8, '经常', '调休', '大厂,平台大,内卷', 1, 1),
(4, 4, '美团：业务多样但加班多', 1, '产品', '北京', 2.0, 1, '业务多样，成长空间大，晋升机制透明', '加班多，996，周末经常需要值班', 4.1, 4.0, 4.1, 2.5, 3.8, 3.9, '经常', '调休', '大厂,业务多样,加班多', 1, 1),
(5, 5, '京东：福利不错但管理严格', 2, '测试', '北京', 3.0, 1, '福利不错，有住房补贴，食堂便宜', '管理严格，流程繁琐，加班较多', 3.8, 4.2, 3.8, 2.8, 3.5, 3.6, '经常', '调休', '大厂,福利好,管理严格', 1, 1),
(1, 6, '华为：技术强但工作强度大', 2, '硬件', '深圳', 4.0, 0, '技术强，平台好，薪资高，培训体系完善', '工作强度极大，007，压力大', 4.2, 4.8, 3.8, 1.5, 4.0, 3.9, '强制', '给薪', '大厂,技术强,狼性文化', 1, 1),
(2, 7, '百度：相对轻松但增长放缓', 1, 'AI', '北京', 2.0, 1, '工作相对轻松，965，技术氛围好', '公司增长放缓，晋升机会减少', 4.0, 4.0, 4.0, 3.5, 3.8, 4.0, '偶尔', '调休', '大厂,轻松,AI', 1, 1),
(3, 8, '网易：福利好但晋升慢', 3, '游戏', '杭州', 0.8, 0, '福利好，食堂好吃，工作氛围轻松', '晋升慢，游戏项目周期长', 4.3, 4.3, 4.5, 3.0, 3.5, 4.1, '偶尔', '调休', '大厂,福利好,游戏', 1, 1),
(4, 9, '小米：扁平化管理但压力大', 1, '设计', '北京', 1.5, 1, '扁平化管理，氛围轻松，决策快', '压力大，加班多，薪资一般', 4.1, 4.0, 4.3, 2.8, 3.8, 3.9, '经常', '调休', '大厂,扁平化,压力大', 1, 1),
(5, 10, '快手：年轻有活力但不稳定', 2, '运营', '北京', 2.0, 1, '年轻有活力，氛围好，机会多', '业务不稳定，经常调整，压力大', 4.2, 4.5, 4.0, 2.8, 4.0, 3.8, '经常', '调休', '大厂,年轻,不稳定', 1, 1),
(1, 11, '拼多多：发展快但内卷严重', 1, '后端', '上海', 1.0, 1, '发展快，晋升机会多，薪资高', '内卷严重，996，压力极大', 4.0, 4.3, 3.9, 2.0, 4.2, 3.7, '强制', '调休', '大厂,发展快,内卷', 1, 1),
(2, 12, '滴滴：业务稳定但创新不足', 2, '数据分析', '北京', 3.0, 1, '业务稳定，福利不错，工作相对轻松', '创新不足，增长放缓，晋升机会少', 3.9, 4.0, 3.8, 3.0, 3.6, 3.7, '偶尔', '调休', '大厂,稳定,创新不足', 1, 1),
(3, 13, '腾讯音乐：福利好但增长有限', 1, '运营', '深圳', 2.0, 1, '福利好，工作氛围轻松，压力小', '业务增长有限，晋升机会少', 4.2, 4.1, 4.3, 3.5, 3.5, 4.0, '偶尔', '调休', '大厂,福利好,轻松', 1, 1),
(4, 14, '爱奇艺：内容丰富但盈利困难', 2, '前端', '北京', 2.5, 1, '内容丰富，工作有趣，氛围好', '盈利困难，薪资一般，加班多', 3.9, 3.9, 4.0, 2.8, 3.7, 3.8, '经常', '调休', '大厂,内容,盈利困难', 1, 1),
(5, 15, '哔哩哔哩：年轻文化但压力大', 1, '游戏', '上海', 1.5, 1, '年轻文化，氛围好，内容有趣', '压力大，加班多，薪资一般', 4.3, 4.2, 4.6, 2.8, 4.0, 3.9, '经常', '调休', '大厂,年轻,压力大', 1, 1),
(1, 16, '蚂蚁集团：金融科技领先但监管严', 2, '后端', '杭州', 3.0, 1, '金融科技领先，技术挑战大，薪资高', '监管严格，压力大，加班多', 4.4, 4.6, 4.2, 2.5, 4.1, 4.0, '经常', '调休', '大厂,金融科技,监管严', 1, 1),
(2, 17, '字节电商：增长快但竞争激烈', 1, '产品', '北京', 1.0, 1, '增长快，机会多，技术先进', '竞争激烈，压力大，加班多', 4.2, 4.4, 4.1, 2.8, 4.2, 3.9, '经常', '调休', '大厂,增长快,竞争激烈', 1, 1),
(3, 18, '腾讯云：技术强但销售压力大', 2, '云计算', '深圳', 2.5, 1, '技术强，平台好，福利不错', '销售压力大，加班多，指标重', 4.1, 4.3, 4.0, 2.8, 3.9, 3.8, '经常', '调休', '大厂,技术强,销售压力', 1, 1),
(4, 19, '阿里云：技术领先但内卷严重', 1, '算法', '杭州', 1.5, 1, '技术领先，平台大，学习机会多', '内卷严重，996，压力大', 4.2, 4.4, 4.1, 2.5, 4.0, 3.9, '经常', '调休', '大厂,技术领先,内卷', 1, 1),
(5, 20, '商汤科技：AI前沿但稳定性不足', 2, 'AI', '上海', 2.0, 1, 'AI技术前沿，挑战大，学习机会多', '稳定性不足，融资压力大，加班多', 4.4, 4.6, 4.3, 2.5, 4.2, 3.8, '经常', '调休', 'AI,前沿,不稳定', 1, 1);

-- ============================================
-- 初始化示例面经数据
-- ============================================
INSERT INTO `interview_experience` (`user_id`, `company_id`, `title`, `recruit_type`, `job_category`, `city`, `status`, `difficulty`, `experience_text`, `tags`, `audit_status`, `is_anonymous`) VALUES
(1, 1, '腾讯2026年前端开发校招面经', 1, '前端', '深圳', '已通过', 4, '一面：HTML/CSS基础，JavaScript闭包，React原理\n二面：算法题（链表反转），项目经验\n三面：系统设计，职业规划\nHR面：个人情况，薪资期望', '大厂,校招,前端', 1, 1),
(2, 2, '字节跳动2026年后端开发社招面经', 2, '后端', '北京', 'Offer', 5, '一面：算法题（二叉树遍历），Java多线程\n二面：系统设计（秒杀系统），分布式架构\n三面：项目深度，技术选型\nHR面：过往经历，期望薪资', '大厂,社招,后端', 1, 1),
(3, 3, '阿里巴巴2026年算法工程师校招面经', 1, '算法', '杭州', '已通过', 5, '一面：算法题（动态规划），机器学习基础\n二面：模型调优，项目经验\n三面：论文解读，未来规划\nHR面：个人背景，职业期望', '大厂,校招,算法', 1, 1),
(4, 4, '美团2026年产品经理社招面经', 2, '产品', '北京', '已通过', 4, '一面：产品思维，过往项目\n二面：用户场景，数据分析\n三面：业务理解，战略思考\nHR面：职业规划，薪资期望', '大厂,社招,产品', 1, 1),
(5, 5, '京东2026年测试工程师校招面经', 1, '测试', '北京', '已通过', 3, '一面：测试基础，Bug分析\n二面：自动化测试，性能测试\n三面：项目经验，沟通能力\nHR面：个人情况，职业规划', '大厂,校招,测试', 1, 1),
(1, 6, '华为2026年硬件工程师社招面经', 2, '硬件', '深圳', 'Offer', 5, '一面：电路基础，PCB设计\n二面：硬件架构，信号完整性\n三面：项目经验，技术难题\nHR面：加班接受度，职业规划', '大厂,社招,硬件', 1, 1),
(2, 7, '百度2026年AI工程师校招面经', 1, 'AI', '北京', '已通过', 4, '一面：机器学习基础，算法题\n二面：模型设计，项目经验\n三面：论文讨论，未来方向\nHR面：个人背景，薪资期望', '大厂,校招,AI', 1, 1),
(3, 8, '网易2026年游戏策划社招面经', 2, '游戏', '杭州', 'Offer', 4, '一面：游戏理解，创意设计\n二面：用户研究，数据分析\n三面：项目管理，团队协作\nHR面：职业规划，薪资期望', '大厂,社招,游戏', 1, 1),
(4, 9, '小米2026年UI设计师校招面经', 1, '设计', '北京', '已通过', 3, '一面：设计基础，作品集\n二面：用户体验，设计思维\n三面：团队协作，沟通能力\nHR面：个人情况，职业规划', '大厂,校招,设计', 1, 1),
(5, 10, '快手2026年运营社招面经', 2, '运营', '北京', '已通过', 4, '一面：运营策略，数据分析\n二面：用户增长，活动策划\n三面：业务理解，团队管理\nHR面：职业规划，薪资期望', '大厂,社招,运营', 1, 1),
(1, 11, '拼多多2026年后端开发校招面经', 1, '后端', '上海', 'Offer', 4, '一面：算法题（排序），Java基础\n二面：系统设计，分布式架构\n三面：项目经验，技术选型\nHR面：个人情况，薪资期望', '大厂,校招,后端', 1, 1),
(2, 12, '滴滴2026年数据分析师社招面经', 2, '数据分析', '北京', '已通过', 4, '一面：SQL，统计学基础\n二面：数据分析案例，AB测试\n三面：业务理解，数据产品\nHR面：职业规划，薪资期望', '大厂,社招,数据分析', 1, 1),
(3, 13, '腾讯音乐2026年产品运营校招面经', 1, '运营', '深圳', '已通过', 3, '一面：产品理解，运营策略\n二面：数据分析，用户增长\n三面：项目经验，沟通能力\nHR面：个人情况，职业规划', '大厂,校招,运营', 1, 1),
(4, 14, '爱奇艺2026年前端开发社招面经', 2, '前端', '北京', 'Offer', 4, '一面：JavaScript，React\n二面：性能优化，工程化\n三面：项目经验，技术选型\nHR面：职业规划，薪资期望', '大厂,社招,前端', 1, 1),
(5, 15, '哔哩哔哩2026年游戏开发校招面经', 1, '游戏', '上海', '已通过', 4, '一面：C++基础，游戏引擎\n二面：算法题，项目经验\n三面：游戏设计，技术选型\nHR面：个人情况，职业规划', '大厂,校招,游戏', 1, 1),
(1, 16, '蚂蚁集团2026年Java开发社招面经', 2, '后端', '杭州', 'Offer', 5, '一面：Java并发，JVM\n二面：微服务，分布式事务\n三面：金融系统设计，安全\nHR面：职业规划，薪资期望', '大厂,社招,后端', 1, 1),
(2, 17, '字节电商2026年产品经理校招面经', 1, '产品', '北京', '已通过', 4, '一面：产品思维，用户场景\n二面：数据分析，商业理解\n三面：项目经验，沟通能力\nHR面：个人情况，职业规划', '大厂,校招,产品', 1, 1),
(3, 18, '腾讯云2026年云计算工程师社招面经', 2, '云计算', '深圳', 'Offer', 4, '一面：网络基础，Linux\n二面：云平台架构，容器技术\n三面：项目经验，技术选型\nHR面：职业规划，薪资期望', '大厂,社招,云计算', 1, 1),
(4, 19, '阿里云2026年算法工程师校招面经', 1, '算法', '杭州', '已通过', 5, '一面：算法题，机器学习基础\n二面：模型设计，项目经验\n三面：论文讨论，未来方向\nHR面：个人背景，薪资期望', '大厂,校招,算法', 1, 1),
(5, 20, '商汤科技2026年AI研究员社招面经', 2, 'AI', '上海', 'Offer', 5, '一面：深度学习基础，论文解读\n二面：模型创新，实验设计\n三面：项目经验，研究方向\nHR面：职业规划，薪资期望', 'AI,社招,研究', 1, 1);

-- ============================================
-- 初始化示例敏感词数据
-- ============================================
INSERT INTO `sensitive_word` (`word`, `word_type`, `level`, `replace_word`, `status`) VALUES
('敏感词1', 1, 1, '***', 1),
('敏感词2', 2, 2, '***', 1),
('敏感词3', 3, 3, '***', 1),
('敏感词4', 4, 2, '***', 1),
('敏感词5', 5, 1, '***', 1);

ALTER TABLE interview_experience ADD COLUMN work_experience VARCHAR(50) DEFAULT NULL COMMENT '工作年限';

SET sql_mode = '';
CREATE TABLE IF NOT EXISTS offertalk.`user_settings` (
                                                         `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                         `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                                         `anonymous_default` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '匿名发布默认开启',
    `show_collections` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '允许他人查看我的收藏',
    `show_posts` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '允许他人查看我的发布',
    `personalized_recommend` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '个性化推荐',
    `comment_notify` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '评论通知',
    `like_notify` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '点赞通知',
    `at_notify` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '@我通知',
    `system_notify` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '系统通知',
    `weekly_report` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '周报推送',
    `bio` VARCHAR(255) DEFAULT '' COMMENT '个人简介',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户设置表';

SET sql_mode = '';
CREATE TABLE IF NOT EXISTS offertalk.`feedback` (
                                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                    `user_id` BIGINT NOT NULL,
                                                    `type` TINYINT NOT NULL,
                                                    `title` VARCHAR(255) DEFAULT NULL,
    `content` TEXT NOT NULL,
    `images` VARCHAR(1000) DEFAULT NULL,
    `contact` VARCHAR(100) DEFAULT NULL,
    `status` TINYINT NOT NULL DEFAULT 0,
    `reply_content` TEXT DEFAULT NULL,
    `reply_time` DATETIME DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE offertalk.sys_user ADD COLUMN bio VARCHAR(255) DEFAULT NULL COMMENT '个人简介';

ALTER TABLE offertalk.user_settings
    ADD COLUMN `delete_flag` TINYINT(1) NOT NULL DEFAULT 0
COMMENT '删除标识：0-未删除，1-已删除'
AFTER `update_time`;