package com.jingdianjichi.domain.handler;

import com.jingdianjichi.domain.convent.SubjectRadioBoConverter;
import com.jingdianjichi.domain.entity.SubjectAnswerBo;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.infra.batic.entity.SubjectRadio;
import com.jingdianjichi.infra.batic.service.SubjectRadioService;
import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import com.jingdianjichi.subject.common.enums.SubjectTypeEnums;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *  单选题策略
 */
@Component
public class RadioTypeHandler implements SubjectInfoHandler {

    @Resource
    private SubjectRadioService subjectRadioService;

    public SubjectTypeEnums getHandlerType() {
        return SubjectTypeEnums.RADIO;
    }

    public void add(SubjectInfoBo subjectInfoBo) {
        // 单选添加
        List<SubjectRadio> radioList = new ArrayList<>();
        subjectInfoBo.getOptionList().forEach( option ->{
            SubjectRadio radio = SubjectRadioBoConverter.INSTANCE.subjectInfoTOEntity(option);
            radio.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
            radio.setSubjectId(subjectInfoBo.getId());
            radioList.add(radio);
        });
        subjectRadioService.batchInsert(radioList);
    }

    @Override
    public SubjectOptionBO query(Long id) {
        List<SubjectRadio> subjectRadios = subjectRadioService.queryBySubjectId(id);
        List<SubjectAnswerBo> subjectAnswerBos = new ArrayList<>();
        String subjectAnswer = "";
        for (SubjectRadio subjectRadio : subjectRadios){
            subjectAnswer = subjectRadio.getIsCorrect() == 1 ? subjectRadio.getOptionContent() : subjectAnswer;
            SubjectAnswerBo subjectAnswerBo = new SubjectAnswerBo();
            subjectAnswerBo.setOptionContent(subjectRadio.getOptionContent());
            subjectAnswerBo.setOptionType(subjectRadio.getOptionType());
            subjectAnswerBo.setIsCorrect(subjectRadio.getIsCorrect());
            subjectAnswerBos.add(subjectAnswerBo);
        }
        SubjectOptionBO subjectOptionBO = new SubjectOptionBO();
        subjectOptionBO.setSubjectAnswer(subjectAnswer);
        subjectOptionBO.setOptionList(subjectAnswerBos);
        return subjectOptionBO;
    }
}
