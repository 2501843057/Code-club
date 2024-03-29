package com.jingdianjichi.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.jingdianjichi.infra.batic.entity.SubjectInfoEs;
import com.jingdianjichi.infra.batic.service.SubjectEsService;
import com.jingdianjichi.infra.batic.service.SubjectInfoService;
import com.jingdianjichi.infra.entity.UserInfo;
import com.jingdianjichi.infra.rpc.UserRpc;
import com.jingdianjichi.subject.common.entity.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test/")
@Slf4j
public class TestFeignController {
    @Resource
    private UserRpc userRpc;

    @Resource
    private SubjectEsService subjectEsService;



}
