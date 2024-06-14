package com.jingdianjichi.wx.handler;

import com.jingdianjichi.wx.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class WxTextHandler implements WxChatMsgHandler{
    @Override
    public WxChatMsgTypeEnum getMsgType() {
        return WxChatMsgTypeEnum.TEXT_MSG;
    }

    @Resource
    private RedisUtil redisUtil;
    private static final String LOGIN_PREFIX = "loginCode";

    @Override
    public String handleMsg(Map<String, String> map) {
        String toUserName = map.get("ToUserName");
        String fromUserName = map.get("FromUserName");
        Random random = new Random();
        int randomNum = random.nextInt(1000);

        String numKey = redisUtil.buildKey(LOGIN_PREFIX, String.valueOf(randomNum));
        redisUtil.setNx(numKey, fromUserName, 5L, TimeUnit.MINUTES);
        String content = "验证码为："+randomNum+" (5分钟后失效)";

        String msg = "<xml>\n" +
                "  <ToUserName><![CDATA["+fromUserName+"]]></ToUserName>\n" +
                "  <FromUserName><![CDATA["+toUserName+"]]></FromUserName>\n" +
                "  <CreateTime>12345678</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA["+content+"]]></Content>\n" +
                "</xml>";
        return msg;
    }
}
