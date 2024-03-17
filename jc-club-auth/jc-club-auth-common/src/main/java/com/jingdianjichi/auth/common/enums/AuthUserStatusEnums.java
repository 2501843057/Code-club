package com.jingdianjichi.auth.common.enums;

import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
public enum AuthUserStatusEnums {

    OPEN(0,"启用"),

    CLOSE(0,"禁用");

    public int code;

    public String desc;

    AuthUserStatusEnums(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static AuthUserStatusEnums getByCode(int codeValue){
        for(AuthUserStatusEnums resultEnums: AuthUserStatusEnums.values()){
            if(resultEnums.getCode() == codeValue){
                return resultEnums;
            }
        }
        return null;
    }

}
