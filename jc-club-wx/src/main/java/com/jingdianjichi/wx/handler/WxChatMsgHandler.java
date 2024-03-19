package com.jingdianjichi.wx.handler;

import java.util.Map;

public interface WxChatMsgHandler {

    WxChatMsgTypeEnum getMsgType();

    String handleMsg(Map<String, String> map);
}
