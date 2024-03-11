package com.jingdianjichi.domain.handler;

import com.jingdianjichi.domain.convent.SubjectMultipleBoConverter;
import com.jingdianjichi.domain.entity.SubjectAnswerBo;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.infra.batic.entity.SubjectMultiple;
import com.jingdianjichi.infra.batic.entity.SubjectRadio;
import com.jingdianjichi.infra.batic.service.SubjectMultipleService;
import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import com.jingdianjichi.subject.common.enums.SubjectTypeEnums;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 多选题策略
 */
@Component
public class MultipleTypeHandler implements SubjectInfoHandler {
    @Resource
    private SubjectMultipleService subjectMultipleService;

    public SubjectTypeEnums getHandlerType() {
        return SubjectTypeEnums.MULTIPLE;
    }

    public void add(SubjectInfoBo subjectInfoBo) {
        // 单选添加
        List<SubjectMultiple> multipleList = new ArrayList<>();
        subjectInfoBo.getOptionList().forEach( option ->{
            SubjectMultiple multiple = SubjectMultipleBoConverter.INSTANCE.subjectInfoTOEntity(option);
            multiple.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
            multiple.setSubjectId(subjectInfoBo.getId());
            multipleList.add(multiple);
        });
        subjectMultipleService.batchInsert(multipleList);
    }

    @Override
    public SubjectOptionBO query(Long id) {
        List<SubjectMultiple> subjectMultiples = subjectMultipleService.queryBySubjectId(id);
        List<SubjectAnswerBo> subjectAnswerBos = new ArrayList<>();
        String subjectAnswer = "";
        for (SubjectMultiple subjectMultiple : subjectMultiples){
            SubjectAnswerBo subjectAnswerBo = new SubjectAnswerBo();
            subjectAnswerBo.setOptionContent(subjectMultiple.getOptionContent());
            subjectAnswerBo.setOptionType(subjectMultiple.getOptionType().intValue());
            subjectAnswerBo.setIsCorrect(subjectMultiple.getIsCorrect());
            subjectAnswerBos.add(subjectAnswerBo);
        }
        SubjectOptionBO subjectOptionBO = new SubjectOptionBO();
        subjectOptionBO.setSubjectAnswer(subjectAnswer);
        subjectOptionBO.setOptionList(subjectAnswerBos);
        return subjectOptionBO;
    }
}
