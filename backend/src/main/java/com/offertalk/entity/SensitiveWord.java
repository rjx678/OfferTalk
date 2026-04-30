package com.offertalk.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sensitive_word")
public class SensitiveWord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String word;
    private Integer wordType;
    private Integer level;
    private String replaceWord;
    private Integer status;
}
