package com.jingdianjichi.domain.util;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


/**
 * 本地缓存工具类
 * @param <V>
 */
@Component
public class CacheUtil<K,V> {

    private Cache<String,String> localCache =
            CacheBuilder.newBuilder()
                    .maximumSize(5000)
                    .expireAfterWrite(10, TimeUnit.SECONDS)
                    .build();

    public List<V> getResult(String cacheKey, Class<V> clazz,
                             Function<String, List<V>> function){
        List<V> reslutList = new ArrayList<>();
        String content = localCache.getIfPresent(cacheKey);
        if(StringUtils.isNotBlank(content)){
            reslutList = JSON.parseArray(content,clazz);
        }else{
            reslutList = function.apply(cacheKey);
            if(CollectionUtils.isNotEmpty(reslutList)){
                localCache.put(cacheKey,JSON.toJSONString(reslutList));
            }
        }
        return reslutList;
    }
    public Map<K, V> getMapResult(String cacheKey, Class<V> clazz,
                                  Function<String, Map<K, V>> function) {
        Map<K, V> map = new HashMap<>();
        String content = localCache.getIfPresent(cacheKey);
        if(StringUtils.isNotBlank(content)){
            map = (Map<K, V>) JSON.parseObject(content,clazz);
        }else{
            map = function.apply(cacheKey);
            if(CollectionUtils.isNotEmpty(map)){
                localCache.put(cacheKey,JSON.toJSONString(map));
            }
        }
        return map;
    }
}
