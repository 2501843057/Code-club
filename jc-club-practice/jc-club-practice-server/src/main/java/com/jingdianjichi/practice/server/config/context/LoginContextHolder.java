package com.jingdianjichi.practice.server.config.context;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录上下文对象
 */
public class LoginContextHolder {

    private static final InheritableThreadLocal<Map<String, Object>> THREAD_LOCAL = new InheritableThreadLocal<>();
    public static void set(String key, Object value) {
        Map<String, Object> map = getThreadLocal();
        map.put(key,value);
    }

    public static Object get(String key){
        Map<String, Object> threadLocal = getThreadLocal();
        return threadLocal.get(key);
    }

    public static String getLoginId(){
        return (String) getThreadLocal().get("loginId");
    }

    public static void remove(){
        THREAD_LOCAL.remove();
    }


    public static Map<String, Object> getThreadLocal() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if(Objects.isNull(map)){
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

}
