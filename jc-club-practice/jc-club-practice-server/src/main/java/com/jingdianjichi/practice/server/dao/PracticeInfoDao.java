package com.jingdianjichi.practice.server.dao;

import com.jingdianjichi.practice.server.entity.po.PracticeInfoPO;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeInfoDao {
    PracticeInfoPO selectById(Long practiceId);

    void insert(PracticeInfoPO practicePO);

    void update(PracticeInfoPO practicePO);
}
