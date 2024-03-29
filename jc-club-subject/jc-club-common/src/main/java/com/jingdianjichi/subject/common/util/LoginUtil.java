package com.jingdianjichi.subject.common.util;


import com.jingdianjichi.subject.common.context.LoginContextHolder;

/**
 * 登录工具类
 */
public class LoginUtil {

    public static String getLoginId() {
        return LoginContextHolder.getLoginId();
    }
}
