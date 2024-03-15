package com.jingdianjichi.oss.adapter;

import com.jingdianjichi.oss.entity.FileInfo;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AliStorageAdapter implements StorageAdapter {


    @Override
    @SneakyThrows
    public void createBucket(String bucket) {
    }

    @Override
    @SneakyThrows
    public void uploadFile(MultipartFile uploadFile, String bucket, String objectName) {
    }

    @Override
    @SneakyThrows
    public List<String> getAllBucket() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Aliyun");
        return list;
    }

    @Override
    @SneakyThrows
    public List<FileInfo> getAllFile(String bucket) {
        return null;
    }

    @Override
    @SneakyThrows
    public InputStream download(String bucket, String objectName) {
        return null;
    }

    @Override
    @SneakyThrows
    public void deleteBucket(String bucket) {

    }

    @Override
    @SneakyThrows
    public void deleteObject(String bucket, String objectName) {
    }
}
