package com.jingdianjichi.practice.server.vo;

import lombok.Data;

import java.util.List;

@Data
public class PracticeSubjectVO {

    /**
     * 题目类型
     */
    private Integer subjectType;
    /**
     * 题目名称
     */
    private String subjectName;

    /**
     * 选项
     */
    private List<PracticeSubjectOptionVO> options;

}
