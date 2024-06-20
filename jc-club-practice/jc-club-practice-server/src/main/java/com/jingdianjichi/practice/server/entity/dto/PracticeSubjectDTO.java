package com.jingdianjichi.practice.server.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class PracticeSubjectDTO {

    /**
     * 组合id
     */
    private List<String> assembleIds;
    /**
     * 题目类型
     */
    private Integer subjectType;
    /**
     * 题目数量
     */
    private Integer subjectCount;

    /**
     * 要排除的题目id
     */
    private List<Long> excludeSubjectIds;


}
