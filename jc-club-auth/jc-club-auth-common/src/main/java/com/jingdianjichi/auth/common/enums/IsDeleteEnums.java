package com.jingdianjichi.auth.common.enums;

import lombok.Getter;

/**
 * 删除状态枚举
 */
@Getter
public enum IsDeleteEnums {

    DELETE(1,"已删除"),

    UN_DELETE(0,"未删除");

    public int code;

    public String desc;

    IsDeleteEnums(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static IsDeleteEnums getByCode(int codeValue){
        for(IsDeleteEnums resultEnums: IsDeleteEnums.values()){
            if(resultEnums.getCode() == codeValue){
                return resultEnums;
            }
        }
        return null;
    }

}
