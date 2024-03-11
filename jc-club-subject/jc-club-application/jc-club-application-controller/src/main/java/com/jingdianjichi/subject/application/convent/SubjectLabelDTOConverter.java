package com.jingdianjichi.subject.application.convent;

import com.jingdianjichi.domain.entity.SubjectCategoryBo;
import com.jingdianjichi.domain.entity.SubjectLabelBo;
import com.jingdianjichi.subject.application.dto.SubjectCategoryDTO;
import com.jingdianjichi.subject.application.dto.SubjectLabelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubjectLabelDTOConverter {

    SubjectLabelDTOConverter INSTANCE = Mappers.getMapper(SubjectLabelDTOConverter.class);

    SubjectLabelBo subjectDTOToBo(SubjectLabelDTO subjectLabelDTO);

    List<SubjectLabelDTO> subjectBoOListToDTOList(List<SubjectLabelBo> subjectLabelBos);
}
