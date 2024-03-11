package com.jingdianjichi.domain.handler;

import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.subject.common.enums.SubjectTypeEnums;

public interface SubjectInfoHandler {

    /**
     *  枚举身份的识别
     */
    SubjectTypeEnums getHandlerType();

    /**
     *  实际的添加
     */
    void add(SubjectInfoBo subjectInfoBo);

    /**
     *  通过题目id查询
     */
    SubjectOptionBO query(Long id);
}
