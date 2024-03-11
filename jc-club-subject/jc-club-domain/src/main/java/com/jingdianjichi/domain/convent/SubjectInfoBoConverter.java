package com.jingdianjichi.domain.convent;


import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.infra.batic.entity.SubjectInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface SubjectInfoBoConverter {
    SubjectInfoBoConverter INSTANCE = Mappers.getMapper(SubjectInfoBoConverter.class);

    SubjectInfo BoToInfo(SubjectInfoBo subjectInfoBo);

    List<SubjectInfoBo> InfoListToBOList(List<SubjectInfo> subjectInfos);

    SubjectInfoBo convertOptionAndInfoToBo(SubjectOptionBO subjectOptionBO,SubjectInfo subjectInfo);
}
