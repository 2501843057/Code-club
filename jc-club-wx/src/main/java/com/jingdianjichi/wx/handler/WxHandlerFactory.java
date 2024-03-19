package com.jingdianjichi.wx.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class WxHandlerFactory implements InitializingBean {

    @Resource
    private List<WxChatMsgHandler> handlerList = new ArrayList<>();

    private HashMap<WxChatMsgTypeEnum, WxChatMsgHandler> map = new HashMap<>();

    public WxChatMsgHandler getHandler(String msgType){
        return map.get(WxChatMsgTypeEnum.getMsgType(msgType));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (WxChatMsgHandler handler : handlerList) {
            map.put(handler.getMsgType(), handler);
        }
    }
}
