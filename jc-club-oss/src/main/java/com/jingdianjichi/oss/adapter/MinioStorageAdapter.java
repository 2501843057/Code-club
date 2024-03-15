package com.jingdianjichi.oss.adapter;


import com.jingdianjichi.oss.entity.FileInfo;
import com.jingdianjichi.oss.util.MinioUtil;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

public class MinioStorageAdapter implements StorageAdapter {

    @Resource
    private MinioUtil minioUtil;

    @Override
    @SneakyThrows
    public void createBucket(String bucket) {
        minioUtil.createBucket(bucket);
    }

    @Override
    @SneakyThrows
    public void uploadFile(MultipartFile uploadFile, String bucket, String objectName) {
        minioUtil.createBucket(bucket);
        if(objectName != null){
            minioUtil.uploadFile(uploadFile.getInputStream(),bucket,objectName+"/"+uploadFile.getName());
        }else{
            minioUtil.uploadFile(uploadFile.getInputStream(),bucket,uploadFile.getName());
        }
    }

    @Override
    @SneakyThrows
    public List<String> getAllBucket() {
        return minioUtil.getAllBucket();
    }

    @Override
    @SneakyThrows
    public List<FileInfo> getAllFile(String bucket) {
        return minioUtil.getAllFile(bucket);
    }

    @Override
    @SneakyThrows
    public InputStream download(String bucket, String objectName) {
        return minioUtil.download(bucket,objectName);
    }

    @Override
    @SneakyThrows
    public void deleteBucket(String bucket) {
        minioUtil.deleteBucket(bucket);
    }

    @Override
    @SneakyThrows
    public void deleteObject(String bucket, String objectName) {
        minioUtil.deleteObject(bucket,objectName);
    }
}
