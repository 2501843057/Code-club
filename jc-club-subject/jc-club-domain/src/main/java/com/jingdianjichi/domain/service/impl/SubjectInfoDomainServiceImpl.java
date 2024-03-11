package com.jingdianjichi.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.jingdianjichi.domain.convent.SubjectInfoBoConverter;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.domain.service.SubjectInfoDomainService;
import com.jingdianjichi.domain.handler.SubjectInfoHandler;
import com.jingdianjichi.domain.handler.SubjectTypeHandlerFactory;
import com.jingdianjichi.infra.batic.entity.SubjectInfo;
import com.jingdianjichi.infra.batic.entity.SubjectLabel;
import com.jingdianjichi.infra.batic.entity.SubjectMapping;
import com.jingdianjichi.infra.batic.service.SubjectInfoService;
import com.jingdianjichi.infra.batic.service.SubjectMappingService;
import com.jingdianjichi.subject.common.entity.PageResult;
import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SubjectInfoDomainServiceImpl implements SubjectInfoDomainService {

    @Resource
    private SubjectInfoService subjectInfoService;

    @Resource
    private SubjectTypeHandlerFactory subjectTypeHandlerFactory;

    @Resource
    private SubjectMappingService subjectMappingService;


    @Override
    public void add(SubjectInfoBo subjectInfoBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectInfoDomainServiceImpl.add.bo:{}", JSON.toJSONString(subjectInfoBo));
        }
        SubjectInfo subjectInfo = SubjectInfoBoConverter.INSTANCE.BoToInfo(subjectInfoBo);
        subjectInfo.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        SubjectInfo info = subjectInfoService.insert(subjectInfo);
        subjectInfoBo.setId(info.getId());

        //  采用工厂 + 策略模式
        SubjectInfoHandler handle = subjectTypeHandlerFactory.getHandle(subjectInfoBo.getSubjectType());
        handle.add(subjectInfoBo);

        // 添加题目/分类/标签关系表
        List<SubjectMapping> mappingList = new ArrayList<>();
        subjectInfoBo.getCategoryIds().forEach( categoryId ->{
            subjectInfoBo.getLabelIds().forEach( labelId ->{
                SubjectMapping mapping = new SubjectMapping();
                mapping.setSubjectId(subjectInfoBo.getId());
                mapping.setCategoryId(categoryId);
                mapping.setLabelId(labelId);
                mapping.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
                mappingList.add(mapping);
            });
        });
        subjectMappingService.batchInsert(mappingList);
    }

    @Override
    public PageResult<SubjectInfoBo> getSubjectPage(SubjectInfoBo subjectInfoBo) {
        if (log.isInfoEnabled()) {
            log.info("SubjectInfoDomainServiceImpl.getSubjectPage.bo:{}", JSON.toJSONString(subjectInfoBo));
        }

        PageResult<SubjectInfoBo> pageResult = new PageResult<>();
        pageResult.setPageNo(subjectInfoBo.getPageNo());
        pageResult.setPageSize(subjectInfoBo.getPageSize());
        int start = (subjectInfoBo.getPageNo() - 1) * subjectInfoBo.getPageSize();
        SubjectInfo subjectInfo = SubjectInfoBoConverter.INSTANCE.BoToInfo(subjectInfoBo);

        int count = subjectInfoService.countByCondition(subjectInfo,subjectInfoBo.getCategoryId(),subjectInfoBo.getLabelId());
        if(count == 0){
            return pageResult;
        }

        List<SubjectInfo> infoList= subjectInfoService.queryPage(subjectInfo,subjectInfoBo.getCategoryId()
                ,subjectInfoBo.getLabelId(),start,subjectInfoBo.getPageSize());
        List<SubjectInfoBo> infoBosList = SubjectInfoBoConverter.INSTANCE.InfoListToBOList(infoList);
        pageResult.setRecords(infoBosList);
        pageResult.setTotal(count);
        return pageResult;
    }

    @Override
    public SubjectInfoBo querySubjectInfo(SubjectInfoBo subjectInfoBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectInfoDomainServiceImpl.querySubjectInfo.bo:{}", JSON.toJSONString(subjectInfoBO));
        }

        // 查询各自题目数据
        SubjectInfo subjectInfo = subjectInfoService.queryById(subjectInfoBO.getId());
        SubjectInfoHandler handler = subjectTypeHandlerFactory.getHandle(subjectInfo.getSubjectType());
        SubjectOptionBO optionBO = handler.query(subjectInfo.getId());
        SubjectInfoBo bo = SubjectInfoBoConverter.INSTANCE.convertOptionAndInfoToBo(optionBO, subjectInfo);

        // 查询Mapping数据
        SubjectMapping subjectMapping = new SubjectMapping();
        subjectMapping.setSubjectId(subjectInfo.getId());
        subjectMapping.setIsDeleted(IsDeleteEnums.UN_DELETE.getCode());
        List<SubjectMapping> mappingList = subjectMappingService.queryLabelId(subjectMapping);

        // 拿着labelIdList 去查labelNameList
        List<Long> labelIdList = mappingList.stream().map(SubjectMapping::getLabelId).collect(Collectors.toList());
        List<SubjectLabel> labelList = subjectInfoService.batchQueryById(labelIdList);
        List<String> labelNameList = labelList.stream().map(SubjectLabel::getLabelName).collect(Collectors.toList());
        bo.setLabelName(labelNameList);
        return bo;
    }
}
