package com.jingdianjichi.domain.convent;
import com.jingdianjichi.domain.entity.SubjectLabelBo;
import com.jingdianjichi.infra.batic.entity.SubjectLabel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface SubjectLabelBoConverter {
    SubjectLabelBoConverter INSTANCE = Mappers.getMapper(SubjectLabelBoConverter.class);

    SubjectLabel subjectBoTOLabel(SubjectLabelBo subjectLabelBo);

    List<SubjectLabelBo> LabelListTOBoList(List<SubjectLabel> subjectLabel);
}
