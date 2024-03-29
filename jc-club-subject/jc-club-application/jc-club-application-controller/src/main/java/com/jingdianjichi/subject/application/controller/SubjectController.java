package com.jingdianjichi.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.base.Preconditions;
import com.jingdianjichi.domain.entity.SubjectAnswerBo;
import com.jingdianjichi.domain.entity.SubjectInfoBo;
import com.jingdianjichi.domain.service.SubjectInfoDomainService;
import com.jingdianjichi.infra.batic.entity.SubjectInfo;
import com.jingdianjichi.infra.batic.entity.SubjectInfoEs;
import com.jingdianjichi.subject.application.convent.SubjectAnswerDTOConverter;
import com.jingdianjichi.subject.application.convent.SubjectCategoryDTOConverter;
import com.jingdianjichi.subject.application.convent.SubjectInfoDTOConverter;
import com.jingdianjichi.subject.application.dto.SubjectInfoDTO;
import com.jingdianjichi.subject.common.entity.PageResult;
import com.jingdianjichi.subject.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 刷题分类Category
 *
 */
@RestController
@RequestMapping("/subject")
@Slf4j
public class SubjectController {

    @Resource
    private SubjectInfoDomainService subjectInfoDomainService;

    /**
     * 新增题目
     */
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody SubjectInfoDTO subjectInfoDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectController.add.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }

            // 参数校验
            Preconditions.checkArgument(!StringUtils.isBlank(subjectInfoDTO.getSubjectName()),
                    "题目名称不能为空");
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectDifficult(), "题目难度不能为空");
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectType(), "题目类型不能为空");
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectScore(), "题目分数不能为空");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(subjectInfoDTO.getCategoryIds())
                    , "分类id不能为空");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(subjectInfoDTO.getLabelIds())
                    , "标签id不能为空");

            // DTO -> BO
            SubjectInfoBo subjectInfoBo = SubjectInfoDTOConverter.INSTANCE.dtoToBo(subjectInfoDTO);
            List<SubjectAnswerBo> options = SubjectAnswerDTOConverter
                    .INSTANCE.dtoListToBoList(subjectInfoDTO.getOptionList());
            subjectInfoBo.setOptionList(options);

            // 添加
            subjectInfoDomainService.add(subjectInfoBo);

            return Result.ok(true);
        }catch (Exception e){
            log.error("SubjectInfoController.add.error:{}", e.getMessage(),e);
            return Result.fail();
        }
    }


    /**
     * 查询题目列表
     */
    @PostMapping("/getSubjectPage")
    public Result<PageResult<SubjectInfoDTO>> getSubjectPage(@RequestBody SubjectInfoDTO subjectInfoDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectController.getSubjectPage.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }

            // 参数校验
            Preconditions.checkNotNull(subjectInfoDTO.getCategoryId(), "分类id不能为空");
            Preconditions.checkNotNull(subjectInfoDTO.getLabelId(), "标签id不能为空");
            
            // DTO -> BO
            SubjectInfoBo subjectInfoBo = SubjectInfoDTOConverter.INSTANCE.dtoToBo(subjectInfoDTO);
            PageResult<SubjectInfoBo> boPageResult = subjectInfoDomainService.getSubjectPage(subjectInfoBo);

            // List<Bo> -> List<DTO>
            List<SubjectInfoDTO> dtoList = SubjectInfoDTOConverter.INSTANCE.boListToDTOList(boPageResult.getResult());
            PageResult<SubjectInfoDTO> pageResult = new PageResult<SubjectInfoDTO>();
            BeanUtils.copyProperties(boPageResult,pageResult);
            pageResult.setResult(dtoList);
            return Result.ok(pageResult);

        }catch (Exception e){
            log.error("SubjectInfoController.getSubjectPage.error:{}", e.getMessage(),e);
            return Result.fail("查询题目列表失败");
        }
    }

    /**
     * 查询题目详情
     */
    @PostMapping("/querySubjectInfo")
    public Result<SubjectInfoDTO> querySubjectInfo(@RequestBody SubjectInfoDTO subjectInfoDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectController.querySubjectInfo.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }

            // 参数校验
            Preconditions.checkNotNull(subjectInfoDTO.getId(), "题目id不能为空");
//            Preconditions.checkNotNull(subjectInfoDTO.getCategoryId(), "分类id不能为空");
//            Preconditions.checkNotNull(subjectInfoDTO.getLabelId(), "标签id不能为空");

            // DTO -> BO
            SubjectInfoBo subjectInfoBo = SubjectInfoDTOConverter.INSTANCE.dtoToBo(subjectInfoDTO);
            SubjectInfoBo bo = subjectInfoDomainService.querySubjectInfo(subjectInfoBo);

            SubjectInfoDTO infoDTO = SubjectInfoDTOConverter.INSTANCE.boToDTO(bo);
            return Result.ok(infoDTO);
        }catch (Exception e){
            log.error("SubjectInfoController.querySubjectInfo.error:{}", e.getMessage(),e);
            return Result.fail("查询题目详情失败");
        }
    }


    /**
     * 全文检索
     */
    @PostMapping("/getSubjectPageBySearch")
    public Result<PageResult<SubjectInfoEs>> getSubjectPageBySearch(@RequestBody SubjectInfoDTO subjectInfoDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectController.getSubjectPage.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }

            // 参数校验
            Preconditions.checkArgument(Strings.isNotBlank(subjectInfoDTO.getKeyWord()), "关键词不能为空");

            // DTO -> BO
            SubjectInfoBo subjectInfoBo = SubjectInfoDTOConverter.INSTANCE.dtoToBo(subjectInfoDTO);
            subjectInfoBo.setPageNo(subjectInfoDTO.getPageNo());
            subjectInfoBo.setPageSize(subjectInfoBo.getPageSize());
            PageResult<SubjectInfoEs> result = subjectInfoDomainService.getSubjectPageBySearch(subjectInfoBo);

            return Result.ok(result);
        }catch (Exception e){
            log.error("SubjectInfoController.getSubjectPageBySearch.error:{}", e.getMessage(),e);
            return Result.fail("全文检索失败");
        }
    }


    /**
     * 排行榜（数据库版）
     */
    @PostMapping("/getRankings")
    public Result<SubjectInfoDTO> getRankings(){
        try{
            List<SubjectInfoBo> subjectInfoBos =  subjectInfoDomainService.getRankings();
            // DTO -> BO
            List<SubjectInfoDTO> subjectInfoBo = SubjectInfoDTOConverter.INSTANCE.boListToDTOList(subjectInfoBos);
            return Result.ok(subjectInfoBo);
        }catch (Exception e){
            log.error("SubjectInfoController.getRankings.error:{}", e.getMessage(),e);
            return Result.fail("排行榜（数据库版）失败");
        }
    }

}
