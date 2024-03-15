package com.jingdianjichi.oss.util;


import com.jingdianjichi.oss.entity.FileInfo;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Minio文件操作工具
 */
@Component
public class MinioUtil {

    @Resource
    private MinioClient minioClient;

    /**
     * 创建桶Bucket
     */
    public void createBucket(String bucket) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if(!exists){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }


    /**
     * 上传文件
     */
    public void uploadFile(InputStream inputStream, String bucket, String objectName) throws Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(objectName)
                .stream(inputStream,-1,Integer.MAX_VALUE).build());
    }

    /**
     * 列出所有桶
     */
    public List<String> getAllBucket() throws Exception {
        List<Bucket> buckets = minioClient.listBuckets();
        return buckets.stream().map(Bucket::name).collect(Collectors.toList());
    }

    /**
     * 列出当前桶及文件
     */
    public List<FileInfo> getAllFile(String bucket) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucket).build());
        ArrayList<FileInfo> fileInfos = new ArrayList<>();
        for(Result<Item> result : results){
            FileInfo fileInfo = new FileInfo();
            Item item = result.get();
            fileInfo.setFileName(item.objectName());
            fileInfo.setEtag(item.etag());
            fileInfo.setDirectoryFlag(item.isDir());
            fileInfos.add(fileInfo);
        }
        return fileInfos;
    }

    /**
     * 下载文件
     */
    public InputStream download(String bucket,String objectName) throws Exception{
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(objectName).build());
    }


    /**
     * 删除桶
     */
    public void deleteBucket(String bucket) throws Exception{
       minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
    }


    /**
     * 删除文件
     */
    public void deleteObject(String bucket,String objectName) throws Exception{
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
    }

}
