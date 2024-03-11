package com.jingdianjichi.domain.service;


import com.jingdianjichi.domain.entity.SubjectLabelBo;

import java.util.List;

public interface SubjectLabelDomainService {

    Boolean add(SubjectLabelBo subjectLabelBo);

    Boolean update(SubjectLabelBo subjectLabelBo);

    Boolean delete(SubjectLabelBo subjectLabelBo);

    List<SubjectLabelBo> queryLabelByCategoryId(SubjectLabelBo subjectLabelBo);
}
