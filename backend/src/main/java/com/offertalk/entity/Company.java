package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("company")
public class Company extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String name;
    private String shortName;
    private String logoUrl;
    private String industry;
    private String scale;
    private String stage;
    private String website;
    private String city;
    private String address;
    private String description;
    private String tags;
    private Integer status;
    private Integer viewCount;
    private Integer postCount;
    private Integer interviewCount;
    private Integer salaryCount;
    private Integer reviewCount;
    private BigDecimal ratingTotal;
    private BigDecimal ratingSalary;
    private BigDecimal ratingCulture;
    private BigDecimal ratingOvertime;
    private BigDecimal ratingPromotion;
    private BigDecimal ratingInterview;
    private String difficultyLevel;
    private String overtimeLevel;
    private Integer blacklistFlag;
    private Integer sortOrder;
}
