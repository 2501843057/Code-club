package com.jingdianjichi.domain.convent;

import com.jingdianjichi.domain.entity.SubjectAnswerBo;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.infra.batic.entity.SubjectMultiple;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectMultipleBoConverter {

    SubjectMultipleBoConverter INSTANCE = Mappers.getMapper(SubjectMultipleBoConverter.class);

    SubjectMultiple subjectInfoTOEntity(SubjectAnswerBo subjectAnswerBo);

}
