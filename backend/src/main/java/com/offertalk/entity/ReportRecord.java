package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("report_record")
public class ReportRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Integer contentType;
    private Long contentId;
    private Integer reportType;
    private String reportReason;
    private String evidenceUrls;
    private String contactInfo;
    private Integer status;
    private String handleRemark;
    private Long handleUserId;
    private LocalDateTime handleTime;
}
