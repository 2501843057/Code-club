package com.jingdianjichi.oss.adapter;


import com.jingdianjichi.oss.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;


public interface StorageAdapter {

    /**
     * 创建桶Bucket
     */
    public void createBucket(String bucket);


    /**
     * 上传文件
     */
    public void uploadFile(MultipartFile uploadFile, String bucket, String objectName) ;
    /**
     * 列出所有桶
     */
    public List<String> getAllBucket() ;
    /**
     * 列出当前桶及文件
     */
    public List<FileInfo> getAllFile(String bucket) ;
    /**
     * 下载文件
     */
    public InputStream download(String bucket,String objectName) ;

    /**
     * 删除桶
     */
    public void deleteBucket(String bucket) ;

    /**
     * 删除文件
     */
    public void deleteObject(String bucket,String objectName) ;

    public String getUrl(String bucket, String objectName);

}
