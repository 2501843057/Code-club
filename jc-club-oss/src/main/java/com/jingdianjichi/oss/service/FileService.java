package com.jingdianjichi.oss.service;

import com.jingdianjichi.oss.adapter.StorageAdapter;
import io.minio.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService {

    private final StorageAdapter storageAdapter;

    public FileService(StorageAdapter storageService) {
        this.storageAdapter = storageService;
    }

    public List<String> getAllBucket(){
       return storageAdapter.getAllBucket();
    }

    public String uploadFile(MultipartFile uploadFile, String bucket, String objectName) {
        storageAdapter.uploadFile(uploadFile,bucket,objectName);
        objectName = objectName + "/" + uploadFile.getOriginalFilename();
        return storageAdapter.getUrl(bucket, objectName);
    }

    public String getUrl(String bucketName, String objectName) {
        return storageAdapter.getUrl(bucketName, objectName);
    }
}
