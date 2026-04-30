package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("salary_disclosure")
public class SalaryDisclosure extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long companyId;
    private Long positionId;
    private String title;
    private Integer recruitType;
    private String jobCategory;
    private String city;
    private String level;
    private Integer workingYears;
    private LocalDate entryDate;
    private BigDecimal monthlyBase;
    private BigDecimal annualBase;
    private BigDecimal totalPackage;
    private BigDecimal signingBonus;
    private BigDecimal annualBonus;
    private String stockOptions;
    private BigDecimal stockValue;
    private BigDecimal mealAllowance;
    private BigDecimal housingAllowance;
    private BigDecimal transportAllowance;
    private BigDecimal otherAllowance;
    private String socialInsuranceRatio;
    private String housingFundRatio;
    private String overtimeSituation;
    private String probationPeriod;
    private BigDecimal probationSalaryRatio;
    private String wageLevel;
    private String addText;
    private Integer viewCount;
    private Integer likeCount;
    private Integer collectCount;
    private Integer commentCount;
    private Integer reportCount;
    private BigDecimal truthScore;
    private Integer truthScoreCount;
    private Integer auditStatus;
    private String auditRemark;
    private Integer isAnonymous;
    private Integer isTop;
    private Integer isHot;
    private Integer isDeleted;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String companyName;
    @TableField(exist = false)
    private String companyLogo;
}
