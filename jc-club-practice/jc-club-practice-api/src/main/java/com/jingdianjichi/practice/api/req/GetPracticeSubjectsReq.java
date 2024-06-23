package com.jingdianjichi.practice.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetPracticeSubjectsReq implements Serializable {

    /**
     * 套卷id
     */
    private Long titleId;

    /**
     * 练习id
     */
    private Long practiceId;

    /**
     * 题目id
     */
    private Long subjectId;

    /**
     * 题目类型
     */
    private Integer subjectType;
}
