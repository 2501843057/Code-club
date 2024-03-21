package com.jingdianjichi.subject.application.dto;

import lombok.Data;

import java.util.List;


/**
 * 题目分类(SubjectCategory)DTO
 *
 * @author makejava
 * @since 2024-03-06 14:47:59
 */
@Data
public class SubjectCategoryDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 分类类型
     */
    private Integer categoryType;
    /**
     * 图标连接
     */
    private String imageUrl;
    /**
     * 父级id
     */
    private Long parentId;

    private Integer count;

    private List<SubjectLabelDTO> labelDTOList;


}

