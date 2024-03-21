package com.jingdianjichi.oss.controller;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.jingdianjichi.oss.service.FileService;
import io.minio.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class FileController {

    @Resource
    private FileService fileService;

    @NacosValue(value = "${storage.service.type}", autoRefreshed = true)
    private String storageType;

    /**
     * 上传文件
     * @return
     */
    @RequestMapping("/uploadFile")
    public String uploadFile( MultipartFile uploadFile, String bucket, String objectName) throws Exception{
        return fileService.uploadFile(uploadFile, bucket, objectName);
    }

    @RequestMapping("/getUrl")
    public String getUrl(String bucketName, String objectName) throws Exception {
        return fileService.getUrl(bucketName, objectName);
    }

    @RequestMapping("/test")
    public String testGetAllBuckets(){
        return fileService.getAllBucket().get(0);
    }

    @RequestMapping("/testNacos")
    public String testNacos(){
        return storageType;
    }

}
