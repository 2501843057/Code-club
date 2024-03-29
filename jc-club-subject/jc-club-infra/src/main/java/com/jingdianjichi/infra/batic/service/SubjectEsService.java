package com.jingdianjichi.infra.batic.service;

import com.jingdianjichi.infra.batic.entity.SubjectInfo;
import com.jingdianjichi.infra.batic.entity.SubjectInfoEs;
import com.jingdianjichi.subject.common.entity.PageResult;

import java.util.List;

public interface SubjectEsService {

    boolean insert(SubjectInfoEs subjectInfoEs);

    PageResult<SubjectInfoEs> querySubjectList(SubjectInfoEs subjectInfoEs);

}
