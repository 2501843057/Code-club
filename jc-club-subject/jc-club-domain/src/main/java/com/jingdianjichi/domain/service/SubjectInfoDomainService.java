package com.jingdianjichi.domain.service;

import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.infra.batic.entity.SubjectInfoEs;
import com.jingdianjichi.subject.common.entity.PageResult;

import java.util.List;

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

    PageResult<SubjectInfoEs> getSubjectPageBySearch(SubjectInfoBo subjectInfoBo);

    List<SubjectInfoBo> getRankings();
}
