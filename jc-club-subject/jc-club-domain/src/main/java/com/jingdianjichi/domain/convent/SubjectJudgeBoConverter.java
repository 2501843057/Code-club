package com.jingdianjichi.domain.convent;

import com.jingdianjichi.domain.entity.SubjectAnswerBo;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.infra.batic.entity.SubjectJudge;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.security.auth.Subject;
import java.util.List;

@Mapper
public interface SubjectJudgeBoConverter {

    SubjectJudgeBoConverter INSTANCE = Mappers.getMapper(SubjectJudgeBoConverter.class);

    SubjectJudge subjectInfoTOEntity(SubjectAnswerBo subjectAnswerBo);

    List<SubjectAnswerBo> AnswerBOToEntityList(List<SubjectJudge> judgeslist);

}
