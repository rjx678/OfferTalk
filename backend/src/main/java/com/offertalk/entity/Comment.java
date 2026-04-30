package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("comment")
public class Comment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Integer contentType;
    private Long contentId;
    private Long parentId;
    private Long rootId;
    private String content;
    private Integer likeCount;
    private Integer reportCount;
    private Integer auditStatus;
    private Integer isAnonymous;
    private String ipAddress;
    private String deviceInfo;
    private Integer deleteFlag;
}
