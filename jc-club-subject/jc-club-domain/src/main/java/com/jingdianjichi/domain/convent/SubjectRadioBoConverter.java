package com.jingdianjichi.domain.convent;

import com.jingdianjichi.domain.entity.SubjectAnswerBo;
import com.jingdianjichi.domain.entity.SubjectCategoryBo;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.infra.batic.entity.SubjectCategory;
import com.jingdianjichi.infra.batic.entity.SubjectRadio;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectRadioBoConverter {

    SubjectRadioBoConverter INSTANCE = Mappers.getMapper(SubjectRadioBoConverter.class);

    SubjectRadio subjectInfoTOEntity(SubjectAnswerBo subjectAnswerBo);

}
