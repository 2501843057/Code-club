package com.jingdianjichi.practice.server.dao;

import com.jingdianjichi.practice.server.entity.dto.PracticeSubjectDTO;
import com.jingdianjichi.practice.server.entity.po.SubjectPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectDao {
    List<SubjectPO> getPracticeSubject(PracticeSubjectDTO dto);
}
