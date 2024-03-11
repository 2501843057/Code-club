package com.jingdianjichi.domain.handler;

import com.jingdianjichi.domain.convent.SubjectJudgeBoConverter;
import com.jingdianjichi.domain.entity.SubjectAnswerBo;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.infra.batic.entity.SubjectJudge;
import com.jingdianjichi.infra.batic.entity.SubjectMultiple;
import com.jingdianjichi.infra.batic.service.SubjectJudgeService;
import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import com.jingdianjichi.subject.common.enums.SubjectTypeEnums;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 判断题策略
 */
@Component
public class JudgeTypeHandler implements SubjectInfoHandler {

    @Resource
    private SubjectJudgeService subjectJudgeService;

    public SubjectTypeEnums getHandlerType() {
        return SubjectTypeEnums.JUDGE;
    }

    public void add(SubjectInfoBo subjectInfoBo) {
        // 单选添加
        List<SubjectJudge> JudgeList = new ArrayList<>();
        subjectInfoBo.getOptionList().forEach( option ->{
            SubjectJudge judge = SubjectJudgeBoConverter.INSTANCE.subjectInfoTOEntity(option);
            judge.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
            judge.setSubjectId(subjectInfoBo.getId());
            JudgeList.add(judge);
        });
        subjectJudgeService.batchInsert(JudgeList);
    }

    @Override
    public SubjectOptionBO query(Long id) {

        List<SubjectJudge> subjectJudges = subjectJudgeService.queryBySubjectId(id);
        List<SubjectAnswerBo> subjectAnswerBos = SubjectJudgeBoConverter.INSTANCE.AnswerBOToEntityList(subjectJudges);
        SubjectOptionBO subjectOptionBO = new SubjectOptionBO();
        subjectOptionBO.setOptionList(subjectAnswerBos);
        return subjectOptionBO;
    }
}
