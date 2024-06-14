package com.jingdianjichi.domain.service.impl;


import com.alibaba.fastjson.JSON;
import com.jingdianjichi.domain.convent.SubjectLabelBoConverter;
import com.jingdianjichi.domain.entity.SubjectLabelBo;
import com.jingdianjichi.domain.service.SubjectLabelDomainService;
import com.jingdianjichi.infra.batic.entity.SubjectCategory;
import com.jingdianjichi.infra.batic.entity.SubjectLabel;
import com.jingdianjichi.infra.batic.entity.SubjectMapping;
import com.jingdianjichi.infra.batic.service.SubjectCategoryService;
import com.jingdianjichi.infra.batic.service.SubjectLabelService;
import com.jingdianjichi.infra.batic.service.SubjectMappingService;
import com.jingdianjichi.subject.common.enums.CategoryTypeEnums;
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

    @Resource
    private SubjectCategoryService subjectCategoryService;

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

        //如果当前分类是1级分类，则查询所有标签
        SubjectCategory subjectCategory = subjectCategoryService.queryById(subjectLabelBo.getCategoryId());
        if(CategoryTypeEnums.PRIMARY.getCode() == subjectCategory.getCategoryType()){
            SubjectLabel subjectLabel = new SubjectLabel();
            subjectLabel.setCategoryId(subjectLabelBo.getCategoryId());
            List<SubjectLabel> labelList = subjectLabelService.queryByCondition(subjectLabel);
            List<SubjectLabelBo> labelResultList = SubjectLabelBoConverter.INSTANCE.LabelListTOBoList(labelList);
            return labelResultList;
        }

        SubjectLabel subjectLabel = SubjectLabelBoConverter.INSTANCE.subjectBoTOLabel(subjectLabelBo);
        subjectLabel.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        // 二级分类去关联表中取对应的标签id
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
