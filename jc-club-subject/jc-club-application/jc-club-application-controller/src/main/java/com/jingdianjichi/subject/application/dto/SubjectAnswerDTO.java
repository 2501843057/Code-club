package com.jingdianjichi.subject.application.dto;

import lombok.Data;

@Data
public class SubjectAnswerDTO {

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
