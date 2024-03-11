package com.jingdianjichi.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.jingdianjichi.domain.convent.SubjectCategoryBoConverter;
import com.jingdianjichi.domain.entity.SubjectCategoryBo;
import com.jingdianjichi.domain.service.SubjectCategoryDomainService;
import com.jingdianjichi.infra.batic.entity.SubjectCategory;
import com.jingdianjichi.infra.batic.service.SubjectCategoryService;
import com.jingdianjichi.subject.common.enums.CategoryTypeEnums;
import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SubjectCategoryDomainServiceImpl implements SubjectCategoryDomainService {

    @Resource
    private SubjectCategoryService subjectCategoryService;

    public void add(SubjectCategoryBo subjectCategoryBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectCategoryDomainService.add.bo:{}", JSON.toJSONString(subjectCategoryBo));
        }
        SubjectCategory subjectCategory = SubjectCategoryBoConverter
                .INSTANCE.BoToSubjectCategory(subjectCategoryBo);
        subjectCategory.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        subjectCategoryService.insert(subjectCategory);
    }

    public Boolean update(SubjectCategoryBo subjectCategoryBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectCategoryDomainService.update.bo:{}", JSON.toJSONString(subjectCategoryBo));
        }
        SubjectCategory subjectCategory = SubjectCategoryBoConverter
                .INSTANCE.BoToSubjectCategory(subjectCategoryBo);
        int count = subjectCategoryService.update(subjectCategory);
        return count > 0;
    }

    public List<SubjectCategoryBo> queryPrimaryCategory(SubjectCategoryBo subjectCategoryBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectCategoryDomainService.queryPrimaryCategory.bo:{}", JSON.toJSONString(subjectCategoryBo));
        }
        SubjectCategory subjectCategory = SubjectCategoryBoConverter
                .INSTANCE.BoToSubjectCategory(subjectCategoryBo);
        subjectCategory.setIsDeleted(IsDeleteEnums.UN_DELETE.code);

        List<SubjectCategory> subjectCategoryList = subjectCategoryService.queryCategory(subjectCategory);
        List<SubjectCategoryBo> boList = SubjectCategoryBoConverter
                .INSTANCE.ListToBoList(subjectCategoryList);
        return boList;
    }

    public List<SubjectCategoryBo> queryCategory(SubjectCategoryBo subjectCategoryBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectCategoryDomainService.queryCategory.bo:{}", JSON.toJSONString(subjectCategoryBo));
        }
        SubjectCategory subjectCategory = SubjectCategoryBoConverter.INSTANCE.BoToSubjectCategory(subjectCategoryBo);
        subjectCategory.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        subjectCategory.setCategoryType(CategoryTypeEnums.SECOND.code);

        List<SubjectCategory> categoryList = subjectCategoryService.queryCategory(subjectCategory);
        return SubjectCategoryBoConverter.INSTANCE.ListToBoList(categoryList);
    }

    public Boolean delete(SubjectCategoryBo subjectCategoryBo) {
        if(log.isInfoEnabled()){
            log.info("SubjectCategoryDomainService.delete.bo:{}", JSON.toJSONString(subjectCategoryBo));
        }
        SubjectCategory subjectCategory = SubjectCategoryBoConverter.INSTANCE.BoToSubjectCategory(subjectCategoryBo);
        subjectCategory.setIsDeleted(1);
        int count = subjectCategoryService.delete(subjectCategory);
        return count > 0;
    }
}
