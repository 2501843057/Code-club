package com.jingdianjichi.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class SubjectOptionBO {

    /**
     * 题目答案
     */
    private String subjectAnswer;


    /**
     * 答案选项
     */
    private List<SubjectAnswerBo> optionList;

}
