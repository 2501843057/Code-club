package com.jingdianjichi.practice.server.dao;


import com.jingdianjichi.practice.server.entity.po.SubjectLabelPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectLabelDao {


    SubjectLabelPO queryById(Long labelId);
}
