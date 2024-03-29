package com.jingdianjichi.subject.application.convent;


import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.infra.batic.entity.SubjectInfo;
import com.jingdianjichi.subject.application.dto.SubjectInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectInfoDTOConverter {

    SubjectInfoDTOConverter INSTANCE = Mappers.getMapper(SubjectInfoDTOConverter.class);

    SubjectInfoBo dtoToBo(SubjectInfoDTO subjectInfoDTO);

    SubjectInfoDTO boToDTO(SubjectInfoBo subjectInfoBo);

   List<SubjectInfoDTO> boListToDTOList(List<SubjectInfoBo> subjectInfoBo);

}
