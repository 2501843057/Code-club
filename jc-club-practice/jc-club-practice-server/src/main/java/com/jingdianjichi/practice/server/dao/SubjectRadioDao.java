package com.jingdianjichi.practice.server.dao;

import com.jingdianjichi.practice.server.entity.po.SubjectRadioPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRadioDao {

    List<SubjectRadioPo> selectBySubjectId(Long subjectId);
}
