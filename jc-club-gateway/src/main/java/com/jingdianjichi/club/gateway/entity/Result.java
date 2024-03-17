package com.jingdianjichi.club.gateway.entity;

import com.jingdianjichi.club.gateway.enums.ResultEnums;
import lombok.Data;

@Data
public class Result<T> {

    private String msg;

    private Integer code;

    private Boolean success;

    private T data;

    public static Result ok(){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultEnums.SUCCESS.getCode());
        result.setMsg(ResultEnums.SUCCESS.getDesc());
        return result;
    }

    public static <T>Result ok(T data){
        Result<T> result = new Result<T>();
        result.setSuccess(true);
        result.setCode(ResultEnums.SUCCESS.getCode());
        result.setMsg(ResultEnums.SUCCESS.getDesc());
        result.setData(data);
        return result;
    }

    public static Result fail(){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(ResultEnums.FAIL.getCode());
        result.setMsg(ResultEnums.FAIL.getDesc());
        return result;
    }

    public static <T>Result fail(T data){
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setCode(ResultEnums.FAIL.getCode());
        result.setMsg(ResultEnums.FAIL.getDesc());
        result.setData(data);
        return result;
    }

    public static Result fail(Integer code,String msg){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
