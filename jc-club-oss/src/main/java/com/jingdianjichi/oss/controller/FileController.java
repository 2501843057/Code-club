package com.jingdianjichi.oss.controller;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.jingdianjichi.oss.service.FileService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
public class FileController {

    @Resource
    private FileService fileService;

    @NacosValue(value = "${storage.service.type}", autoRefreshed = true)
    private String storageType;

    @RequestMapping("/test")
    public String testGetAllBuckets(){
        return fileService.getAllBucket().get(0);
    }

    @RequestMapping("/testNacos")
    public String testNacos(){
        return storageType;
    }

}
