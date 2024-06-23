package com.jingdianjichi.practice.server.dao;

import com.jingdianjichi.practice.server.entity.po.PracticeDetailPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeDetailDao {
    PracticeDetailPO selectDetail(@Param("loginId") String loginId,
                                  @Param("practiceId") Long practiceId,
                                  @Param("subjectId") Long subjectId);
}
