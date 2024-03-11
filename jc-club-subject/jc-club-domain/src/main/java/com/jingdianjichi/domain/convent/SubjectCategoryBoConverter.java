package com.jingdianjichi.domain.convent;

import com.jingdianjichi.domain.entity.SubjectCategoryBo;
import com.jingdianjichi.infra.batic.entity.SubjectCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectCategoryBoConverter {
    SubjectCategoryBoConverter INSTANCE = Mappers.getMapper(SubjectCategoryBoConverter.class);

    SubjectCategory BoToSubjectCategory(SubjectCategoryBo SubjectCategoryBo);


    List<SubjectCategoryBo>  ListToBoList(List<SubjectCategory> subjectCategoryList);
}
