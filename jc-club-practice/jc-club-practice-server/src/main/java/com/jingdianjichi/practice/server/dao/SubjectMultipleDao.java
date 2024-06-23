package com.jingdianjichi.practice.server.dao;

import com.jingdianjichi.practice.server.entity.po.SubjectMultiplePO;
import com.jingdianjichi.practice.server.entity.po.SubjectRadioPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectMultipleDao {

    List<SubjectMultiplePO> selectBySubjectId(Long subjectId);
}
