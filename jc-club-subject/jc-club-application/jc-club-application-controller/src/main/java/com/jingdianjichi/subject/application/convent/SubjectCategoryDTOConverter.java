package com.jingdianjichi.subject.application.convent;

import com.jingdianjichi.domain.entity.SubjectCategoryBo;
import com.jingdianjichi.subject.application.dto.SubjectCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface SubjectCategoryDTOConverter {

    SubjectCategoryDTOConverter INSTANCE = Mappers.getMapper(SubjectCategoryDTOConverter.class);

    SubjectCategoryBo subjectCategoryDTOToBo(SubjectCategoryDTO subjectCategoryDTO);

    List<SubjectCategoryDTO> BOListToDtoList(List<SubjectCategoryBo> SubjectCategoryBolist);

    SubjectCategoryDTO BOtoDto(SubjectCategoryBo bo);
}
