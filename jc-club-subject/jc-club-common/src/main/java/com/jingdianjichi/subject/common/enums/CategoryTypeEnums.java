package com.jingdianjichi.subject.common.enums;

import lombok.Getter;

/**
 * 删除状态枚举
 */
@Getter
public enum CategoryTypeEnums {

    PRIMARY(1,"岗位大类"),

    SECOND(2,"二级分类");

    public int code;

    public String desc;

    CategoryTypeEnums(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static CategoryTypeEnums getByCode(int codeValue){
        for(CategoryTypeEnums resultEnums: CategoryTypeEnums.values()){
            if(resultEnums.getCode() == codeValue){
                return resultEnums;
            }
        }
        return null;
    }

}
