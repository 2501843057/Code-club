package com.jingdianjichi.domain.entity;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 题目分类(SubjectCategoryBO
 *
 * @author makejava
 * @since 2024-03-06 14:47:59
 */
@Data
public class SubjectCategoryBo implements Serializable {

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

    private List<SubjectLabelBo> labelBoList;


}

