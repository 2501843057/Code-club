package com.jingdianjichi.subject.application.convent;

import com.jingdianjichi.domain.entity.SubjectLikedBO;
import com.jingdianjichi.subject.application.dto.SubjectLikedDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 题目点赞表 dto转换器
 *
 * @author jingdianjichi
 * @since 2024-01-07 23:08:45
 */
@Mapper
public interface SubjectLikedDTOConverter {

    SubjectLikedDTOConverter INSTANCE = Mappers.getMapper(SubjectLikedDTOConverter.class);

    SubjectLikedBO convertDTOToBO(SubjectLikedDTO subjectLikedDTO);

    List<SubjectLikedDTO> convertBoToDTO(List<SubjectLikedBO>  subjectLikedBO);

}
