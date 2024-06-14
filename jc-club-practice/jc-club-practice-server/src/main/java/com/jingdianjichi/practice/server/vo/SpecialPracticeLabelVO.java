package com.jingdianjichi.practice.server.vo;

import lombok.Data;

@Data
public class SpecialPracticeLabelVO {

    private Long id;

    private String labelName;

    /**
     * 分类id - 标签id
     */
    private String assembleId;

}
