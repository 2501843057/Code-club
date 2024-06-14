package com.jingdianjichi.practice.api.common;

import lombok.Getter;

@Getter
public enum ResultEnums {

    SUCCESS(200,"成功"),

    FAIL(500,"失败");

    public int code;

    public String desc;

    ResultEnums(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public static ResultEnums getByCode(int codeValue){
        for(ResultEnums resultEnums:ResultEnums.values()){
            if(resultEnums.getCode() == codeValue){
                return resultEnums;
            }
        }
        return null;
    }

}
