package com.jingdianjichi.domain.handler;

import com.jingdianjichi.domain.convent.SubjectBriefBoConverter;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.infra.batic.entity.SubjectBrief;
import com.jingdianjichi.infra.batic.service.SubjectBriefService;
import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import com.jingdianjichi.subject.common.enums.SubjectTypeEnums;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 简答题策略
 */

@Component
public class BriefTypeHandler implements SubjectInfoHandler {

    @Resource
    private SubjectBriefService subjectBriefService;

    public SubjectTypeEnums getHandlerType() {
        return SubjectTypeEnums.BRIEF;
    }

    public void add(SubjectInfoBo subjectInfoBo) {
        SubjectBrief brief = SubjectBriefBoConverter.INSTANCE.subjectInfoTOEntity(subjectInfoBo);
        brief.setSubjectId(subjectInfoBo.getId());
        brief.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        subjectBriefService.insert(brief);
    }

    @Override
    public SubjectOptionBO query(Long id) {
       SubjectBrief subjectBrief = subjectBriefService.queryBySubjectId(id);
        SubjectOptionBO subjectOptionBO = new SubjectOptionBO();
        subjectOptionBO.setSubjectAnswer(subjectBrief.getSubjectAnswer());
        return subjectOptionBO;
    }
}
