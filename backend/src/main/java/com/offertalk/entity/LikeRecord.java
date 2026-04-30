package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("like_record")
public class LikeRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Integer contentType;
    private Long contentId;
    private Integer status;
}
