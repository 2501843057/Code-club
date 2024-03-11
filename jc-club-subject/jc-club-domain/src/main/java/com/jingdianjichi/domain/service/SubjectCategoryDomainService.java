package com.jingdianjichi.domain.service;

import com.jingdianjichi.domain.entity.SubjectCategoryBo;

import java.util.List;


public interface SubjectCategoryDomainService {
    /**
     * 新增分类
     */
    void add(SubjectCategoryBo subjectCategoryBo);
    /**
     * 更新分类
     */
    Boolean update(SubjectCategoryBo subjectCategoryBo);
    /**
     * 查询岗位
     */
    List<SubjectCategoryBo> queryPrimaryCategory(SubjectCategoryBo subjectCategoryBo);
    /**
     * 查询分类下的大类
     */
    List<SubjectCategoryBo> queryCategory(SubjectCategoryBo subjectCategoryBo);
    /**
     * 删除分类
     */
    Boolean delete(SubjectCategoryBo subjectCategoryBo);
}
