package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("company_review")
public class CompanyReview extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long companyId;
    private String title;
    private Integer recruitType;
    private String jobCategory;
    private String city;
    private BigDecimal tenureYears;
    private Integer isCurrentlyEmployed;
    private String prosText;
    private String consText;
    private BigDecimal ratingTotal;
    private BigDecimal ratingSalary;
    private BigDecimal ratingCulture;
    private BigDecimal ratingOvertime;
    private BigDecimal ratingPromotion;
    private BigDecimal ratingManagement;
    private String overtimeFrequency;
    private String overtimeSalary;
    private String layoffSituation;
    private String internshipExp;
    private String adviceToCompany;
    private String tags;
    private Integer viewCount;
    private Integer likeCount;
    private Integer collectCount;
    private Integer commentCount;
    private Integer reportCount;
    private BigDecimal truthScore;
    private Integer truthScoreCount;
    private Integer auditStatus;
    private Integer isAnonymous;
    private Integer isTop;
    private Integer isHot;
    private Integer isDeleted;
    @TableField(exist = false)
    private String companyName;
    @TableField(exist = false)
    private String companyLogo;
}
