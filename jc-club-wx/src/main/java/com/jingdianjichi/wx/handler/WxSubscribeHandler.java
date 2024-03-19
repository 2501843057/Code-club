package com.jingdianjichi.wx.handler;


import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WxSubscribeHandler implements WxChatMsgHandler {

    @Override
    public WxChatMsgTypeEnum getMsgType() {
        return WxChatMsgTypeEnum.SUBSCRIBE;
    }

    @Override
    public String handleMsg(Map<String, String> map) {
        String toUserName = map.get("ToUserName");
        String fromUserName = map.get("FromUserName");
        String msg = "<xml>\n" +
                "  <ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[" + toUserName + "]]></FromUserName>\n" +
                "  <CreateTime>12345678</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA[" + "少年驰骋的风，比黄金还珍贵" + "]]></Content>\n" +
                "</xml>";
        return msg;
    }
}
