package com.jingdianjichi.domain.convent;

import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.infra.batic.entity.SubjectBrief;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectBriefBoConverter {

    SubjectBriefBoConverter INSTANCE = Mappers.getMapper(SubjectBriefBoConverter.class);

    SubjectBrief subjectInfoTOEntity(SubjectInfoBo subjectInfoBo);

    List<SubjectOptionBO> entityTOOption(List<SubjectBrief> subjectBrief);

}
