package com.jingdianjichi.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jingdianjichi.domain.convent.SubjectInfoBoConverter;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.entity.SubjectOptionBO;
import com.jingdianjichi.domain.redis.RedisUtil;
import com.jingdianjichi.domain.service.SubjectInfoDomainService;
import com.jingdianjichi.domain.handler.SubjectInfoHandler;
import com.jingdianjichi.domain.handler.SubjectTypeHandlerFactory;
import com.jingdianjichi.domain.service.SubjectLikedDomainService;
import com.jingdianjichi.infra.batic.entity.SubjectInfo;
import com.jingdianjichi.infra.batic.entity.SubjectInfoEs;
import com.jingdianjichi.infra.batic.entity.SubjectLabel;
import com.jingdianjichi.infra.batic.entity.SubjectMapping;
import com.jingdianjichi.infra.batic.service.SubjectEsService;
import com.jingdianjichi.infra.batic.service.SubjectInfoService;
import com.jingdianjichi.infra.batic.service.SubjectLikedService;
import com.jingdianjichi.infra.batic.service.SubjectMappingService;
import com.jingdianjichi.infra.entity.UserInfo;
import com.jingdianjichi.infra.rpc.UserRpc;
import com.jingdianjichi.subject.common.entity.PageResult;
import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import com.jingdianjichi.subject.common.util.IdWorkerUtil;
import com.jingdianjichi.subject.common.util.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    private SubjectLikedDomainService subjectLikedDomainService;

    @Resource
    private SubjectEsService subjectEsService;

    @Resource
    private UserRpc userRpc;

    @Resource
    private RedisUtil redisUtil;

    private static final String RANK_KEY = "subject_rank";

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
        subjectInfoBo.getCategoryIds().forEach(categoryId -> {
            subjectInfoBo.getLabelIds().forEach(labelId -> {
                SubjectMapping mapping = new SubjectMapping();
                mapping.setSubjectId(subjectInfoBo.getId());
                mapping.setCategoryId(categoryId);
                mapping.setLabelId(labelId);
                mapping.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
                mappingList.add(mapping);
            });
        });
        subjectMappingService.batchInsert(mappingList);

        // 插入es
        SubjectInfoEs subjectInfoEs = new SubjectInfoEs();
        subjectInfoEs.setDocId(new IdWorkerUtil(1, 1, 1).nextId());
        subjectInfoEs.setSubjectId(subjectInfo.getId());
        subjectInfoEs.setSubjectAnswer(subjectInfoBo.getSubjectAnswer());
        subjectInfoEs.setCreateTime(new Date().getTime());
        subjectInfoEs.setSubjectName(subjectInfo.getSubjectName());
        subjectInfoEs.setCreateUser("鸡翅");
        subjectInfoEs.setSubjectType(subjectInfo.getSubjectType());
        subjectEsService.insert(subjectInfoEs);

        // 用zadd存储到redis中
        redisUtil.addScore(RANK_KEY, LoginUtil.getLoginId(), 1);

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

        int count = subjectInfoService.countByCondition(subjectInfo, subjectInfoBo.getCategoryId(), subjectInfoBo.getLabelId());
        if (count == 0) {
            return pageResult;
        }

        List<SubjectInfo> infoList = subjectInfoService.queryPage(subjectInfo, subjectInfoBo.getCategoryId()
                , subjectInfoBo.getLabelId(), start, subjectInfoBo.getPageSize());
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

        // 去查询当前用户是否点赞和题目点赞数
        bo.setLiked(subjectLikedDomainService.isLiked(bo.getId().toString(), LoginUtil.getLoginId()));
        bo.setLikedCount(subjectLikedDomainService.getLikedCount(bo.getId().toString()));

        // 添加上一题/下一题
        assembleSubjectCursor(subjectInfoBO,bo);

        return bo;
    }

    private void assembleSubjectCursor(SubjectInfoBo subjectInfoBO, SubjectInfoBo bo) {
        Long categoryId = subjectInfoBO.getCategoryId();
        Long labelId = subjectInfoBO.getLabelId();
        Long subjectId = subjectInfoBO.getId();
        if (Objects.isNull(categoryId) || Objects.isNull(labelId)) {
            return;
        }
        Long nextSubjectId = subjectInfoService.querySubjectIdCursor(subjectId, categoryId, labelId, 1);
        bo.setNextSubjectId(nextSubjectId);
        Long lastSubjectId = subjectInfoService.querySubjectIdCursor(subjectId, categoryId, labelId, 0);
        bo.setLastSubjectId(lastSubjectId);
    }

    @Override
    public PageResult<SubjectInfoEs> getSubjectPageBySearch(SubjectInfoBo subjectInfoBo) {
        SubjectInfoEs subjectInfoEs = new SubjectInfoEs();
        subjectInfoEs.setKeyWord(subjectInfoBo.getKeyWord());
        subjectInfoEs.setPageNo(subjectInfoBo.getPageNo());
        subjectInfoEs.setPageSize(subjectInfoBo.getPageSize());
        return subjectEsService.querySubjectList(subjectInfoEs);
    }

    @Override
    public List<SubjectInfoBo> getRankings() {

        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisUtil.rankWithScore(RANK_KEY, 0, 5);

        if(CollectionUtils.isEmpty(typedTuples)){
            return Collections.emptyList();
        }

        List<SubjectInfoBo> infoBos = new ArrayList<>();
        typedTuples.forEach(subjectInfo -> {
            SubjectInfoBo subjectInfoBo = new SubjectInfoBo();
            UserInfo info = userRpc.getUserInfo(subjectInfo.getValue());
            subjectInfoBo.setCreateUser(info.getNickName());
            subjectInfoBo.setCreateUserAvatar(info.getAvatar());
            subjectInfoBo.setSubjectCount(subjectInfo.getScore().intValue());
            infoBos.add(subjectInfoBo);
        });

        return infoBos;
    }
}
