package com.jingdianjichi.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.jingdianjichi.domain.entity.SubjectLabelBo;
import com.jingdianjichi.domain.service.SubjectLabelDomainService;
import com.jingdianjichi.subject.application.convent.SubjectLabelDTOConverter;
import com.jingdianjichi.subject.application.dto.SubjectLabelDTO;
import com.jingdianjichi.subject.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/subject/label")
@Slf4j
public class SubjectLabelController {

    @Resource
    private SubjectLabelDomainService subjectLabelDomainService;

    /**
     * 增加标签
     */
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody SubjectLabelDTO subjectLabelDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectLabelController.add.dto:{}", JSON.toJSONString(subjectLabelDTO));
            }

            Preconditions.checkNotNull(subjectLabelDTO.getCategoryId(),"分类id不能为空");
            Preconditions.checkArgument(!StringUtils.isBlank(subjectLabelDTO.getLabelName()),"标签名称不能为空");
            Preconditions.checkNotNull(subjectLabelDTO.getSortNum(),"排序不能为空");

            SubjectLabelBo subjectLabelBo = SubjectLabelDTOConverter.INSTANCE.subjectDTOToBo(subjectLabelDTO);
            Boolean isAdd = subjectLabelDomainService.add(subjectLabelBo);
            return Result.ok(isAdd);
        }catch (Exception e){
            log.error("SubjectLabelController.add.dto:{}",e.getMessage(),e);
            return Result.fail(false);
        }
    }

    /**
     * 更新标签
     */
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody SubjectLabelDTO subjectLabelDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectLabelController.update.dto:{}", JSON.toJSONString(subjectLabelDTO));
            }

            Preconditions.checkNotNull(subjectLabelDTO.getId(),"id不能为空");

            SubjectLabelBo subjectLabelBo = SubjectLabelDTOConverter.INSTANCE.subjectDTOToBo(subjectLabelDTO);
            Boolean isUpdate = subjectLabelDomainService.update(subjectLabelBo);
            return Result.ok(isUpdate);
        }catch (Exception e){
            log.error("SubjectLabelController.add.update:{}",e.getMessage(),e);
            return Result.fail(false);
        }
    }

    /**
     * 删除标签
     */
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody SubjectLabelDTO subjectLabelDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectLabelController.update.dto:{}", JSON.toJSONString(subjectLabelDTO));
            }

            Preconditions.checkNotNull(subjectLabelDTO.getId(),"id不能为空");

            SubjectLabelBo subjectLabelBo = SubjectLabelDTOConverter.INSTANCE.subjectDTOToBo(subjectLabelDTO);
            Boolean isDelete = subjectLabelDomainService.delete(subjectLabelBo);
            return Result.ok(isDelete);
        }catch (Exception e){
            log.error("SubjectLabelController.add.update:{}",e.getMessage(),e);
            return Result.fail(false);
        }
    }

    /**
     * 根据分类查询标签
     */
    @PostMapping("/queryLabelByCategoryId")
    public Result<List<SubjectLabelDTO>> queryLabelByCategoryId(@RequestBody SubjectLabelDTO subjectLabelDTO){
        try{
            if(log.isInfoEnabled()){
                log.info("SubjectLabelController.update.dto:{}", JSON.toJSONString(subjectLabelDTO));
            }

            Preconditions.checkNotNull(subjectLabelDTO.getCategoryId(),"分类id不能为空");

            SubjectLabelBo subjectLabelBo = SubjectLabelDTOConverter.INSTANCE.subjectDTOToBo(subjectLabelDTO);
            List<SubjectLabelBo> boList = subjectLabelDomainService.queryLabelByCategoryId(subjectLabelBo);
            List<SubjectLabelDTO> dtoList = SubjectLabelDTOConverter.INSTANCE.subjectBoOListToDTOList(boList);

            return Result.ok(dtoList);
        }catch (Exception e){
            log.error("SubjectLabelController.queryLabelByCategoryId.error:{}",e.getMessage(),e);
            return Result.fail(false);
        }
    }
}
