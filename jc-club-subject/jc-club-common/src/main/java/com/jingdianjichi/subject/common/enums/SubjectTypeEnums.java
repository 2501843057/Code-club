package com.jingdianjichi.subject.common.enums;

import lombok.Getter;

/**
 * 题目类型枚举
 */
@Getter
public enum SubjectTypeEnums {

    RADIO(1,"单选"),
    MULTIPLE(2,"多选"),
    JUDGE(3,"判断"),
    BRIEF(4,"简答"),
    ;

    public int code;

    public String desc;

    SubjectTypeEnums(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static SubjectTypeEnums getByCode(int codeValue){
        for(SubjectTypeEnums resultEnums: SubjectTypeEnums.values()){
            if(resultEnums.getCode() == codeValue){
                return resultEnums;
            }
        }
        return null;
    }

}
