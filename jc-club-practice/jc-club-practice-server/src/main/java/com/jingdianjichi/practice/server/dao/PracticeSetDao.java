package com.jingdianjichi.practice.server.dao;

import com.jingdianjichi.practice.server.entity.po.PracticeSetPO;
import com.jingdianjichi.practice.server.entity.po.SubjectLabelPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeSetDao {

    void add(PracticeSetPO practiceSetPO);
}
