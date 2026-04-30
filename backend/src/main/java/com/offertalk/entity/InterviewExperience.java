package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("interview_experience")
public class InterviewExperience extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long companyId;
    private Long positionId;
    private String title;
    private Integer recruitType;
    private String jobCategory;
    private String city;
    private String workExperience;
    private String status;
    private Integer difficulty;
    private String experienceText;
    private String timelineData;
    private String tags;
    private Integer viewCount;
    private Integer likeCount;
    private Integer collectCount;
    private Integer commentCount;
    private Integer reportCount;
    private Integer shareCount;
    private BigDecimal truthScore;
    private Integer truthScoreCount;
    private Integer auditStatus;
    private String auditRemark;
    private LocalDateTime auditTime;
    private Integer isAnonymous;
    private Integer isTop;
    private Integer isHot;
    private Integer isDeleted;
    private String deleteReason;
    @TableField(exist = false)
    private String companyName;
    @TableField(exist = false)
    private String companyLogo;
}
