package com.jingdianjichi.domain.service.impl;


import com.alibaba.fastjson.JSON;
import com.jingdianjichi.domain.convent.SubjectLabelBoConverter;
import com.jingdianjichi.domain.entity.SubjectLabelBo;
import com.jingdianjichi.domain.service.SubjectLabelDomainService;
import com.jingdianjichi.infra.batic.entity.SubjectLabel;
import com.jingdianjichi.infra.batic.entity.SubjectMapping;
import com.jingdianjichi.infra.batic.service.SubjectLabelService;
import com.jingdianjichi.infra.batic.service.SubjectMappingService;
import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectLabelDomainServiceImpl implements SubjectLabelDomainService {

    @Resource
    private SubjectLabelService subjectLabelService;

    @Resource
    private SubjectMappingService subjectMappingService;

    public Boolean add(SubjectLabelBo subjectLabelBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.add.bo:{}", JSON.toJSONString(subjectLabelBo));
        }
        SubjectLabel subjectLabel = SubjectLabelBoConverter.INSTANCE.subjectBoTOLabel(subjectLabelBo);
        subjectLabel.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        int count = subjectLabelService.insert(subjectLabel);
        return count > 0;
    }

    public Boolean update(SubjectLabelBo subjectLabelBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.add.bo:{}", JSON.toJSONString(subjectLabelBo));
        }
        SubjectLabel subjectLabel = SubjectLabelBoConverter.INSTANCE.subjectBoTOLabel(subjectLabelBo);
        subjectLabel.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        int count = subjectLabelService.update(subjectLabel);
        return count > 0;
    }

    public Boolean delete(SubjectLabelBo subjectLabelBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.add.bo:{}", JSON.toJSONString(subjectLabelBo));
        }
        SubjectLabel subjectLabel = SubjectLabelBoConverter.INSTANCE.subjectBoTOLabel(subjectLabelBo);
        subjectLabel.setIsDeleted(IsDeleteEnums.DELETE.code);
        int count = subjectLabelService.delete(subjectLabel);
        return count > 0;
    }

    public List<SubjectLabelBo> queryLabelByCategoryId(SubjectLabelBo subjectLabelBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.queryLabelByCategoryId.bo:{}", JSON.toJSONString(subjectLabelBo));
        }
        SubjectLabel subjectLabel = SubjectLabelBoConverter.INSTANCE.subjectBoTOLabel(subjectLabelBo);
        subjectLabel.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        // 去subject_mapping表查标签id
        List<SubjectMapping> mappingList = subjectMappingService.queryLabel(subjectLabel);
        if(CollectionUtils.isEmpty(mappingList)){
            return Collections.emptyList();
        }
        List<Long> labelIdList = mappingList.stream().map(SubjectMapping::getLabelId).collect(Collectors.toList());
        // 拿labelId集合批量查询
        List<SubjectLabel> labelList = subjectLabelService.batchQueryByLabelId(labelIdList);
        List<SubjectLabelBo> boList = SubjectLabelBoConverter.INSTANCE.LabelListTOBoList(labelList);
        return boList;
    }

}
