package com.jingdianjichi.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.jingdianjichi.domain.convent.SubjectLikedBOConverter;
import com.jingdianjichi.domain.entity.SubjectLikedBO;
import com.jingdianjichi.domain.redis.RedisUtil;
import com.jingdianjichi.domain.service.SubjectLikedDomainService;
import com.jingdianjichi.infra.batic.entity.SubjectInfo;
import com.jingdianjichi.infra.batic.entity.SubjectLabel;
import com.jingdianjichi.infra.batic.entity.SubjectLiked;
import com.jingdianjichi.infra.batic.service.SubjectInfoService;
import com.jingdianjichi.infra.batic.service.SubjectLikedService;
import com.jingdianjichi.subject.common.entity.PageResult;

import com.jingdianjichi.subject.common.enums.IsDeleteEnums;
import com.jingdianjichi.subject.common.enums.SubjectLikedStatusEnum;
import com.jingdianjichi.subject.common.util.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 题目点赞表 领域service实现了
 *
 * @author jingdianjichi
 * @since 2024-01-07 23:08:45
 */
@Service
@Slf4j
public class SubjectLikedDomainServiceImpl implements SubjectLikedDomainService {

    @Resource
    private SubjectLikedService subjectLikedService;


    @Resource
    private SubjectInfoService subjectInfoService;

    @Resource
    private RedisUtil redisUtil;

    private static final String SUBJECT_LIKED_KEY = "subject.liked";

    private static final String SUBJECT_LIKED_COUNT_KEY = "subject.liked.count";

    private static final String SUBJECT_LIKED_DETAIL_KEY = "subject.liked.detail";

    @Override
    public void add(SubjectLikedBO subjectLikedBO) {
        // 获取主题ID、点赞用户ID和点赞状态
        Long subjectId = subjectLikedBO.getSubjectId();
        String likeUserId = subjectLikedBO.getLikeUserId();
        Integer status = subjectLikedBO.getStatus();
        // 构建点赞信息的Redis哈希键
        String hashKey = buildSubjectLikedKey(subjectId.toString(), likeUserId);
        // 将点赞状态存储到Redis哈希中
        redisUtil.putHash(SUBJECT_LIKED_KEY, hashKey, status);
        // 构建点赞详情和计数的Redis键
        String detailKey = SUBJECT_LIKED_DETAIL_KEY + "." + subjectId + "." + likeUserId;
        String countKey = SUBJECT_LIKED_COUNT_KEY + "." + subjectId;
        // 根据点赞状态进行增减操作
        if (SubjectLikedStatusEnum.LIKED.getCode() == status) {
            // 如果是点赞，则增加点赞计数并设置点赞详情
            redisUtil.increment(countKey, 1);
            redisUtil.set(detailKey, "1");
        } else {
            // 如果是取消点赞，检查点赞计数是否存在且大于0，若满足则减少点赞计数并删除点赞详情
            Integer count = redisUtil.getInt(countKey);
            if (Objects.isNull(count) || count <= 0) {
                return;
            }
            redisUtil.increment(countKey, -1);
            redisUtil.del(detailKey);
        }
    }

    @Override
    public Boolean isLiked(String subjectId, String userId) {
        String detailKey = SUBJECT_LIKED_DETAIL_KEY + "." + subjectId + "." + userId;
        return redisUtil.exist(detailKey);
    }

    @Override
    public Integer getLikedCount(String subjectId) {
        String countKey = SUBJECT_LIKED_COUNT_KEY + "." + subjectId;
        Integer count = redisUtil.getInt(countKey);
        if (Objects.isNull(count) || count <= 0) {
            return 0;
        }
        return redisUtil.getInt(countKey);
    }

    private String buildSubjectLikedKey(String subjectId, String userId) {
        return subjectId + ":" + userId;
    }

    @Override
    public Boolean update(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        return subjectLikedService.update(subjectLiked) > 0;
    }

    @Override
    public Boolean delete(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = new SubjectLiked();
        subjectLiked.setId(subjectLikedBO.getId());
        subjectLiked.setIsDeleted(IsDeleteEnums.DELETE.getCode());
        return subjectLikedService.update(subjectLiked) > 0;
    }

    @Override
    public void syncLiked() {
        Map<Object, Object> subjectLikedMap = redisUtil.getHashAndDelete(SUBJECT_LIKED_KEY);
        if (log.isInfoEnabled()) {
            log.info("syncLiked.subjectLikedMap:{}", JSON.toJSONString(subjectLikedMap));
        }
        if (MapUtils.isEmpty(subjectLikedMap)) {
            return;
        }
        //批量同步到数据库
        List<SubjectLiked> subjectLikedList = new LinkedList<>();
        subjectLikedMap.forEach((key, val) -> {
            SubjectLiked subjectLiked = new SubjectLiked();
            String[] keyArr = key.toString().split(":");
            String subjectId = keyArr[0];
            String likedUser = keyArr[1];
            subjectLiked.setSubjectId(Long.valueOf(subjectId));
            subjectLiked.setLikeUserId(likedUser);
            subjectLiked.setStatus(Integer.valueOf(val.toString()));
            subjectLiked.setIsDeleted(IsDeleteEnums.UN_DELETE.getCode());
            subjectLikedList.add(subjectLiked);
        });
        subjectLikedService.batchInsertOrUpdate(subjectLikedList);
    }

    @Override
    public PageResult<SubjectLikedBO> getSubjectLikedPage(SubjectLikedBO subjectLikedBO) {
        PageResult<SubjectLikedBO> pageResult = new PageResult<>();
        pageResult.setPageNo(subjectLikedBO.getPageNo());
        pageResult.setPageSize(subjectLikedBO.getPageSize());
        int start = (subjectLikedBO.getPageNo() - 1) * subjectLikedBO.getPageSize();
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        subjectLiked.setLikeUserId(LoginUtil.getLoginId());
        int count = subjectLikedService.countByCondition(subjectLiked);
        if (count == 0) {
            return pageResult;
        }
        List<SubjectLiked> subjectLikedList = subjectLikedService.queryPage(subjectLiked, start,
                subjectLikedBO.getPageSize());
        List<SubjectLikedBO> subjectInfoBOS = SubjectLikedBOConverter.INSTANCE.convertListInfoToBO(subjectLikedList);



        pageResult.setRecords(subjectInfoBOS);
        pageResult.setTotal(count);
        return pageResult;
    }

    @Override
    public void syncLikedJob() {
        Map<Object, Object> map = redisUtil.getHashAndDelete(SUBJECT_LIKED_KEY);
        ArrayList<SubjectLiked> subjectLikes = new ArrayList<>();
        map.forEach((key, value) -> {
            SubjectLiked subjectLiked = new SubjectLiked();
            String [] list = key.toString().split(":");
            subjectLiked.setSubjectId(Long.valueOf(list[0]));
            subjectLiked.setLikeUserId(list[1]);
            subjectLiked.setStatus(Integer.valueOf(value.toString()));
            subjectLiked.setIsDeleted(IsDeleteEnums.UN_DELETE.getCode());
            subjectLikes.add(subjectLiked);
        });
        subjectLikedService.batchInsert(subjectLikes);
    }

    @Override
    public PageResult<SubjectLikedBO> pageLiked(SubjectLikedBO subjectLikedBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLikedDomainServiceImpl.pageLiked.dto:{}", JSON.toJSONString(subjectLikedBO));
        }
        PageResult<SubjectLikedBO> pageResult = new PageResult<>();
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        Integer pageNo = subjectLikedBO.getPageNo();
        Integer pageSize = subjectLikedBO.getPageSize();
        Integer start = (pageNo - 1) * pageSize;
        subjectLiked.setLikeUserId(LoginUtil.getLoginId());
        int count = subjectLikedService.countByCondition(subjectLiked);
        if(count == 0){
            return pageResult;
        }

        List<SubjectLiked> results = subjectLikedService.pageLiked(subjectLiked, start, pageSize);
        List<SubjectLikedBO> subjectLikedBOS = SubjectLikedBOConverter.INSTANCE.convertListInfoToBO(results);
        pageResult.setPageSize(pageSize);
        pageResult.setPageSize(pageNo);


        List<Long> subjectIdList = subjectLikedBOS.stream().map(SubjectLikedBO::getSubjectId).collect(Collectors.toList());
        List<SubjectInfo> infoList = subjectInfoService.batchQuery(subjectIdList);
        Map<Long, String> subjectNameMap= infoList.stream().collect(Collectors.toMap(SubjectInfo::getId, SubjectInfo::getSubjectName));

        subjectLikedBOS.forEach(item -> {
            item.setSubjectName(subjectNameMap.get(item.getSubjectId()));
        });

        pageResult.setResult(subjectLikedBOS);
        return pageResult;
    }

}
