package com.jingdianjichi.practice.server.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class PracticeDetailPO {


    private Long id;

    private Long practiceId;

    private Long subjectId;

    private Long subjectType;

    private Long answerStatus;

    private String answerContent;

    private String createdBy;

    private Date createdTime;

    private String updateBy;

    private Date updateTime;

    private Long isDeleted;

}
