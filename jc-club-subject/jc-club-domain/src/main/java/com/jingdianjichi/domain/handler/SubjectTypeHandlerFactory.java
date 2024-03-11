package com.jingdianjichi.domain.handler;

import com.jingdianjichi.subject.common.enums.SubjectTypeEnums;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 题目类型工厂
 */
@Component
public class SubjectTypeHandlerFactory implements InitializingBean {

    @Resource
    private List<SubjectInfoHandler> infoHandlerList;

    private HashMap<SubjectTypeEnums,SubjectInfoHandler> handlerMap = new HashMap<>();

    public SubjectInfoHandler getHandle(int subjectType){
        SubjectTypeEnums typeEnum = SubjectTypeEnums.getByCode(subjectType);
        return handlerMap.get(typeEnum);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (SubjectInfoHandler handler : infoHandlerList){
            handlerMap.put(handler.getHandlerType(),handler);
        }
    }
}
