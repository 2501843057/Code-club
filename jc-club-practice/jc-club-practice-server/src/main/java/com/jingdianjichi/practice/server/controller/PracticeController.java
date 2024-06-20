package com.jingdianjichi.practice.server.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.jingdianjichi.practice.api.common.Result;
import com.jingdianjichi.practice.api.req.GetPracticeSubjectListReq;
import com.jingdianjichi.practice.server.entity.dto.PracticeSubjectDTO;
import com.jingdianjichi.practice.server.service.PracticeSetService;
import com.jingdianjichi.practice.server.vo.PracticeSetVO;
import com.jingdianjichi.practice.server.vo.SpecialPracticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 练习套卷
 */

@RestController
@Slf4j
@RequestMapping("/practice/set")
public class PracticeController {

    @Resource
    private PracticeSetService practiceService;

    // 获取练习套卷相关内容
    @RequestMapping("/getSpecialPracticeContent")
    public Result<List<SpecialPracticeVO>> getSpecialPracticeContent(){
        try{
            List<SpecialPracticeVO> result = practiceService.getSpecialPracticeContent();
            if(log.isInfoEnabled()){
                log.info("result:{}", JSON.toJSON(result));
            }
            return Result.ok(result);
        }catch (Exception e){
            log.error("getSpecialPracticeContent error:{}",e.getMessage(),e);
            return Result.fail("获取专项练习失败");
        }

    }


    /**
     * 开始练习
     */
    @PostMapping(value = "/addPractice")
    public Result<PracticeSetVO> addPractice(@RequestBody GetPracticeSubjectListReq req) {
        if (log.isInfoEnabled()) {
            log.info("获取练习题入参{}", JSON.toJSONString(req));
        }
        try {
            //参数校验
            Preconditions.checkArgument(!Objects.isNull(req), "参数不能为空！");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(req.getAssembleIds()), "标签ids不能为空！");
            PracticeSubjectDTO dto = new PracticeSubjectDTO();
            dto.setAssembleIds(req.getAssembleIds());
            PracticeSetVO practiceSetVO = practiceService.addPractice(dto);
            if (log.isInfoEnabled()) {
                log.info("获取练习题目列表出参{}", JSON.toJSONString(practiceSetVO));
            }
            return Result.ok(practiceSetVO);
        } catch (IllegalArgumentException e) {
            log.error("参数异常！错误原因{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            log.error("获取练习题目列表异常！错误原因{}", e.getMessage(), e);
            return Result.fail("获取练习题目列表异常！");
        }
    }


}
