package com.jingdianjichi.domain.service;

import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.subject.common.entity.PageResult;

public interface SubjectInfoDomainService {
    /**
     * 添加
     */
    void add(SubjectInfoBo subjectInfoBo);

    /**
     * 分页查询
     */
    PageResult<SubjectInfoBo> getSubjectPage(SubjectInfoBo subjectInfoBo);

    /**
     * 查询详情
     */
    SubjectInfoBo querySubjectInfo(SubjectInfoBo subjectInfoBo);
}
