package com.jingdianjichi.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.base.Preconditions;
import com.jingdianjichi.domain.entity.SubjectCategoryBo;
import com.jingdianjichi.domain.service.SubjectCategoryDomainService;
import com.jingdianjichi.subject.application.convent.SubjectCategoryDTOConverter;
import com.jingdianjichi.subject.application.dto.SubjectCategoryDTO;
import com.jingdianjichi.subject.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * 刷题分类Category
 *
 */
@RestController
@RequestMapping("/subject/category")
@Slf4j
public class SubjectCategoryController {

    @Resource
    private SubjectCategoryDomainService subjectCategoryDomainService;

    /**
     * 新增分类
     */
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody SubjectCategoryDTO subjectCategoryDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectCategoryController.add.dto:{}", JSON.toJSONString(subjectCategoryDTO));
            }
            Preconditions.checkNotNull(subjectCategoryDTO.getCategoryType(), "分类类型不能为空");
            Preconditions.checkArgument(!StringUtils.isBlank(subjectCategoryDTO.getCategoryName()), "分类名称不能为空");
            Preconditions.checkNotNull(subjectCategoryDTO.getParentId(), "分类父级id不能为空");

            SubjectCategoryBo subjectCategoryBo = SubjectCategoryDTOConverter.
                    INSTANCE.subjectCategoryDTOToBo(subjectCategoryDTO);
            subjectCategoryDomainService.add(subjectCategoryBo);
            return Result.ok(true);
        }catch (Exception e){
            log.error("SubjectCategoryController.add.error:{}", e.getMessage(),e);
            return Result.fail();
        }
    }

    /**
     * 查询岗位分类
     */
    @PostMapping("/queryPrimaryCategory")
    public Result<List<SubjectCategoryDTO>> queryPrimaryCategory(@RequestBody SubjectCategoryDTO subjectCategoryDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectCategoryController.add.dto:{}", JSON.toJSONString(subjectCategoryDTO));
            }
            Preconditions.checkNotNull(subjectCategoryDTO.getCategoryType(), "分类类型不能为空");
            SubjectCategoryBo subjectCategoryBo = SubjectCategoryDTOConverter.
                    INSTANCE.subjectCategoryDTOToBo(subjectCategoryDTO);
            List<SubjectCategoryBo> subjectCategoryBoList = subjectCategoryDomainService.queryPrimaryCategory(subjectCategoryBo);
            List<SubjectCategoryDTO> subjectCategoryDTOList = SubjectCategoryDTOConverter.INSTANCE.BOListToDtoList(subjectCategoryBoList);
            return Result.ok(subjectCategoryDTOList);
        }catch (Exception e){
            log.error("SubjectCategoryController.update.error:{}",e.getMessage(),e);
            return Result.fail();
        }
    }

    /**
     * 查询大类下的分类
     */
    @PostMapping("/queryCategoryByPrimary")
    public Result<List<SubjectCategoryDTO>> queryCategoryByPrimary(@RequestBody SubjectCategoryDTO subjectCategoryDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectCategoryController.queryCategoryByPrimary.dto:{}", JSON.toJSONString(subjectCategoryDTO));
            }
            Preconditions.checkNotNull(subjectCategoryDTO.getParentId(),"父级id不能为空");
            SubjectCategoryBo subjectCategoryBo = SubjectCategoryDTOConverter
                    .INSTANCE.subjectCategoryDTOToBo(subjectCategoryDTO);
            List<SubjectCategoryBo> subjectCategoryBOList = subjectCategoryDomainService.queryCategory(subjectCategoryBo);
            List<SubjectCategoryDTO> dtoList = SubjectCategoryDTOConverter.INSTANCE.BOListToDtoList(subjectCategoryBOList);
            return Result.ok(dtoList);
        }catch (Exception e){
            log.error("SubjectCategoryController.update.error:{}",e.getMessage(),e);
            return Result.fail();
        }
    }


    /**
     * 更新分类
     */
    @PostMapping ("/update")
    public Result<Boolean> update(@RequestBody SubjectCategoryDTO subjectCategoryDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectCategoryController.update.dto:{}", JSON.toJSONString(subjectCategoryDTO));
            }
            Preconditions.checkNotNull(subjectCategoryDTO.getId(), "分类id不能为空");
            SubjectCategoryBo subjectCategoryBo = SubjectCategoryDTOConverter.
                    INSTANCE.subjectCategoryDTOToBo(subjectCategoryDTO);
            Boolean aBoolean = subjectCategoryDomainService.update(subjectCategoryBo);
            return Result.ok(aBoolean);
        }catch (Exception e){
            log.error("SubjectCategoryController.update.error:{}", e.getMessage(),e);
            return Result.fail();
        }
    }

    /**
     * 删除分类
     */
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody SubjectCategoryDTO subjectCategoryDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectCategoryController.delete.dto:{}", JSON.toJSONString(subjectCategoryDTO));
            }
            Preconditions.checkNotNull(subjectCategoryDTO.getId(), "分类id不能为空");

            SubjectCategoryBo subjectCategoryBo = SubjectCategoryDTOConverter.
                    INSTANCE.subjectCategoryDTOToBo(subjectCategoryDTO);
            Boolean isDelete = subjectCategoryDomainService.delete(subjectCategoryBo);
            return Result.ok(isDelete);
        }catch (Exception e){
            log.error("SubjectCategoryController.update.error:{}", e.getMessage(),e);
            return Result.fail("删除分类失败");
        }
    }

}
