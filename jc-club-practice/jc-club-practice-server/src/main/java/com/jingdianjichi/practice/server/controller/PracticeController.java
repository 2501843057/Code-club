package com.jingdianjichi.practice.server.controller;

import com.alibaba.fastjson.JSON;
import com.jingdianjichi.practice.api.common.Result;
import com.jingdianjichi.practice.server.service.PracticeSetService;
import com.jingdianjichi.practice.server.vo.SpecialPracticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

}
