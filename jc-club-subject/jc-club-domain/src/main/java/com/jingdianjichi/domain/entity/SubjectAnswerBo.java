package com.jingdianjichi.domain.entity;

import lombok.Data;

/**
 * 题目Bo
 */
@Data
public class SubjectAnswerBo {

    /**
     * 答案选项标识
     */
    private Integer optionType;

    /**
     * 答案
     */
    private String optionContent;

    /**
     * 是否正确
     */
    private Integer isCorrect;

}
