package com.jingdianjichi.wx.controller;


import com.jingdianjichi.wx.handler.WxChatMsgHandler;
import com.jingdianjichi.wx.handler.WxHandlerFactory;
import com.jingdianjichi.wx.utils.MessageUtil;
import com.jingdianjichi.wx.utils.SHA1;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


@RestController
@Slf4j
public class CallBackController {

    private String token = "oanivobuaoNCAEWJfsf";

    @Resource
    private WxHandlerFactory wxHandlerFactory;


    @GetMapping("/callback")
    public String test(@RequestParam("signature")String signature,
                           @RequestParam("timestamp")String timestamp,
                           @RequestParam("nonce")String nonce,
                           @RequestParam("echostr")String echostr) {
       log.info("get验签请求参数：signature:{} ,timestamp:{} ,nonce:{} ,echostr:{} ",signature,timestamp,nonce,echostr);
        String shaStr = SHA1.getSHA1(token, timestamp, nonce, "");
        if (signature.equals(shaStr)) {
            return echostr;
        }
       return "unknown";
    }


    @PostMapping(value = "/callback",produces = "application/xml;charset=UTF-8")
    public String callback(@RequestBody String requestBody,
                           @RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam(value = "msg_signature",required = false) String msgSignature) {
        log.info("接收到微信的请求：requestBody:{} ,signature:{} ,timestamp:{} ,nonce:{} ,msgSignature:{}"
                ,requestBody,signature,timestamp,nonce,msgSignature);
        Map<String, String> map = MessageUtil.parseXml(requestBody);
        String msgType = map.get("MsgType");
        String event = map.get("Event") == null ? "" : map.get("Event");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(msgType);

        if(Strings.isNotBlank(event)){
            stringBuilder.append(".");
            stringBuilder.append(event);
        }

        WxChatMsgHandler handler = wxHandlerFactory.getHandler(stringBuilder.toString());
        String msg = handler.handleMsg(map);
        return msg;
    }
}