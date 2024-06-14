package com.jingdianjichi.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jingdianjichi.domain.convent.SubjectCategoryBoConverter;
import com.jingdianjichi.domain.entity.SubjectCategoryBo;
import com.jingdianjichi.domain.entity.SubjectLabelBo;
import com.jingdianjichi.domain.service.SubjectCategoryDomainService;
import com.jingdianjichi.domain.util.CacheUtil;
import com.jingdianjichi.infra.batic.entity.SubjectCategory;
import com.jingdianjichi.infra.batic.entity.SubjectLabel;
import com.jingdianjichi.infra.batic.entity.SubjectMapping;
import com.jingdianjichi.infra.batic.service.SubjectCategoryService;
import com.jingdianjichi.infra.batic.service.SubjectLabelService;
import com.jingdianjichi.infra.batic.service.SubjectMappingService;
import com.jingdianjichi.subject.common.enums.CategoryTypeEnums;
import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectCategoryDomainServiceImpl implements SubjectCategoryDomainService {

    @Resource
    private SubjectCategoryService subjectCategoryService;

    @Resource
    private SubjectMappingService subjectMappingService;

    @Resource
    private SubjectLabelService subjectLabelService;

    @Resource
    private ThreadPoolExecutor labelThreadPool;

    @Resource
    private CacheUtil cacheUtil;

    public void add(SubjectCategoryBo subjectCategoryBo) {
   ;
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

        boList.forEach(bo ->{
            bo.setCount(subjectMappingService.getCount(bo.getId()));
        });

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

    @SneakyThrows
    @Override
    public List<SubjectCategoryBo> queryCategoryAndLabel(SubjectCategoryBo subjectCategoryBo) {
        if(log.isInfoEnabled()){
            log.info("SubjectCategoryDomainService.queryCategoryAndLabel.bo:{}", JSON.toJSONString(subjectCategoryBo));
        }
        Long id = subjectCategoryBo.getId();
        String cacheKey = "CategoryAndLabel." + id;
        List<SubjectCategoryBo> subjectCategoryBoList = cacheUtil.getResult(cacheKey, SubjectCategoryBo.class,
                (key) -> getSubjectCategoryBos(id));
        return subjectCategoryBoList;
    }

    private List<SubjectCategoryBo> getSubjectCategoryBos(Long id) {
        // 查询大类下的所有小类
        SubjectCategory subjectCategory = new SubjectCategory();
        subjectCategory.setParentId(id);
        subjectCategory.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        List<SubjectCategory> subjectCategoryList = subjectCategoryService.queryCategory(subjectCategory);
        List<SubjectCategoryBo> subjectCategoryBoList = SubjectCategoryBoConverter.INSTANCE.ListToBoList(subjectCategoryList);

        /**
         *  多线程处理方法 CompletableFuture
         */
        HashMap<Long, List<SubjectLabelBo>> map = new HashMap<>();
        List<CompletableFuture<Map<Long, List<SubjectLabelBo>>>> completableFutureList = subjectCategoryBoList.stream().map(category ->
                CompletableFuture.supplyAsync(() ->  getLabelList(category), labelThreadPool)).collect(Collectors.toList());
        completableFutureList.forEach(future->{
            Map<Long,List<SubjectLabelBo>> resultMap = null;
            try {
                resultMap = future.get();
                if(CollectionUtils.isEmpty(resultMap)){
                    return;
                }
                map.putAll(resultMap);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        subjectCategoryBoList.forEach(subjectCategoryBo1 -> {
            subjectCategoryBo1.setLabelBoList(map.get(subjectCategoryBo1.getId()));
        });
        return subjectCategoryBoList;
    }

    private Map<Long,List<SubjectLabelBo>> getLabelList(SubjectCategoryBo categoryBo) {
        HashMap<Long, List<SubjectLabelBo>> map = new HashMap<>();
        SubjectMapping mapping = new SubjectMapping();
        mapping.setCategoryId(categoryBo.getId());
        List<SubjectMapping> mappingList = subjectMappingService.queryLabelId(mapping);
        // 获取标签id集合
        List<Long> labelIdList = mappingList.stream().map(SubjectMapping::getLabelId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(labelIdList)){
            return map;
        }
        List<SubjectLabel> labelList = subjectLabelService.batchQueryByLabelId(labelIdList);
        ArrayList<SubjectLabelBo> subjectLabelBos = new ArrayList<>();
        // 组装标签信息
        labelList.forEach(label -> {
            SubjectLabelBo subjectLabelBo = new SubjectLabelBo();
            subjectLabelBo.setId(label.getId());
            subjectLabelBo.setLabelName(label.getLabelName());
            subjectLabelBo.setSortNum(label.getSortNum());
            subjectLabelBo.setCategoryId(label.getCategoryId());
            subjectLabelBos.add(subjectLabelBo);
        });
        map.put(categoryBo.getId(),subjectLabelBos);
        /**
         *  多线程处理方法 FutureTask
         */
//        // 一次获取标签信息
//        List<FutureTask<Map<Long,List<SubjectLabelBo>>>> futureTaskList = new ArrayList<>();
//        HashMap<Long, List<SubjectLabelBo>> map = new HashMap<>();
//
//        // 线程池并发调用
//        subjectCategoryBoList.forEach(category -> {
//            FutureTask futureTask = new FutureTask<>(()-> getLabelList(category));
//            futureTaskList.add(futureTask);
//            labelThreadPool.submit(futureTask);
//        });
//
//        for(FutureTask<Map<Long,List<SubjectLabelBo>>> futureTask:futureTaskList){
//            Map<Long, List<SubjectLabelBo>> resultMap = futureTask.get();
//            if(CollectionUtils.isEmpty(resultMap)){
//                continue;
//            }
//            map.putAll(resultMap);
//        }
//
//        subjectCategoryBoList.forEach(subjectCategoryBo1 -> {
//            subjectCategoryBo1.setLabelBoList(map.get(subjectCategoryBo1.getId()));
//        });

        return map;
    }
}
