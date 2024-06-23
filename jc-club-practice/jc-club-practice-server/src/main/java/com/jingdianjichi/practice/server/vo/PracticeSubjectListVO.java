package com.jingdianjichi.practice.server.vo;

import lombok.Data;

import java.util.List;

@Data
public class PracticeSubjectListVO {

    /**
     * 套卷名称
     */
    private String title;
    /**
     * 题目
     */
    private List<PracticeSubjectDetailVO> practiceSubjectDetailVOS;

    /**
     * 练习id
     */
    private Long practiceId;
    /**
     * 用时
     */
    private String timeUse;

}
