package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 意见反馈实体类
 */
@Data
@TableName("feedback")
public class Feedback {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 反馈类型（1-问题反馈，2-功能建议，3-Bug报告）
     */
    private Integer type;
    
    /**
     * 反馈标题
     */
    private String title;
    
    /**
     * 反馈内容
     */
    private String content;
    
    /**
     * 图片URL（多个用逗号分隔）
     */
    private String images;
    
    /**
     * 联系方式（邮箱/手机号）
     */
    private String contact;
    
    /**
     * 状态（0-待处理，1-处理中，2-已回复，3-已关闭）
     */
    private Integer status;
    
    /**
     * 回复内容
     */
    private String replyContent;
    
    /**
     * 回复时间
     */
    private LocalDateTime replyTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}