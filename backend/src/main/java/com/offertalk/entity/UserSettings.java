package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_settings")
public class UserSettings extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Boolean anonymousDefault;

    private Boolean showCollections;

    private Boolean showPosts;

    private Boolean personalizedRecommend;

    private Boolean commentNotify;

    private Boolean likeNotify;

    private Boolean atNotify;

    private Boolean systemNotify;

    private Boolean weeklyReport;

    private String bio;

    private Integer deleteFlag;
}