package com.jingdianjichi.subject.application.convent;



import com.jingdianjichi.domain.entity.SubjectAnswerBo;
import com.jingdianjichi.subject.application.dto.SubjectAnswerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectAnswerDTOConverter {

    SubjectAnswerDTOConverter INSTANCE = Mappers.getMapper(SubjectAnswerDTOConverter.class);

    SubjectAnswerBo dtoToBo(SubjectAnswerDTO subjectAnswerDTO);


    List<SubjectAnswerBo> dtoListToBoList(List<SubjectAnswerDTO> subjectAnswerDTOs);
}
