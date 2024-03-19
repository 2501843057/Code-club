package com.jingdianjichi.wx.handler;

public enum WxChatMsgTypeEnum {


    SUBSCRIBE("event.subscribe", "用户关注事件"),

    TEXT_MSG("text", "接收用户文本消息");

    private final String msgType;

    private final String desc;

    private WxChatMsgTypeEnum(String msgType, String desc) {
        this.msgType = msgType;
        this.desc = desc;
    }

    public static WxChatMsgTypeEnum getMsgType(String msgType) {
        for (WxChatMsgTypeEnum type : WxChatMsgTypeEnum.values()) {
            if (msgType.equals(type.msgType)) {
                return type;
            }
        }
        return null;
    }


}
