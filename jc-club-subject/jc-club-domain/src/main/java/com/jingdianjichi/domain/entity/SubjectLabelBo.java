package com.jingdianjichi.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目标签表(SubjectLabel)BO
 *
 * @author makejava
 * @since 2024-03-07 17:05:50
 */
@Data
public class SubjectLabelBo implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 标签分类
     */
    private String labelName;
    /**
     * 排序
     */
    private Integer sortNum;

    /**
     * 分类id
     */
    private Long categoryId;

}

