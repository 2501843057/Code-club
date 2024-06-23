package com.jingdianjichi.practice.server.dao;

import com.jingdianjichi.practice.server.entity.po.PracticeSetDetailPO;
import com.jingdianjichi.practice.server.vo.PracticeSubjectDetailVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeSetDetailDao {
    void insertBatch(@Param("practiceSetDetailPOS") List<PracticeSetDetailPO> practiceSetDetailPOS);

    List<PracticeSetDetailPO> getSubjects(Long titleId);
}
