package com.jingdianjichi.practice.server.vo;

import lombok.Data;

@Data
public class PracticeSubjectOptionVO {
    /**
     * 题目内容
     */
    private String optionContent;
    /**
     * a,b,c,d
     */
    private Integer optionType;

    /**
     * 是否正确
     */
    private Integer isCorrect;


}
