package com.jason.wechat.application.message.handle.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.jason.wechat.application.message.handle.ReqMessageHandler;
import com.jason.wechat.domain.message.req.ReqEventMessage;
import com.jason.wechat.domain.message.req.ReqImageMessage;
import com.jason.wechat.domain.message.req.ReqLinkMessage;
import com.jason.wechat.domain.message.req.ReqLocationMessage;
import com.jason.wechat.domain.message.req.ReqTextMessage;
import com.jason.wechat.domain.message.req.ReqVideoMessage;
import com.jason.wechat.domain.message.req.ReqVoiceMessage;
import com.jason.wechat.infrastruture.util.ClassUtils;

@Service
public class ReqMessageHandlerImpl extends ReqMessageHandler{


    @Override
    public ReqTextMessage textMessageHandle(Map<String, Object> messageMap)  {
       return ClassUtils.map2Message(messageMap,ReqTextMessage.class);
    }

    @Override
    public ReqImageMessage imageMessageHandle(Map<String, Object> messageMap) {
        return ClassUtils.map2Message(messageMap,ReqImageMessage.class);
    }

    @Override
    public ReqVoiceMessage voiceMessageHandle(Map<String, Object> messageMap) {
        return ClassUtils.map2Message(messageMap,ReqVoiceMessage.class);
    }

    @Override
    public ReqVideoMessage videoMessageHandle(Map<String, Object> messageMap) {
        return ClassUtils.map2Message(messageMap,ReqVideoMessage.class);
    }

    @Override
    public ReqLocationMessage locationMessageHandle(Map<String, Object> messageMap) {
        return ClassUtils.map2Message(messageMap,ReqLocationMessage.class);
    }

    @Override
    public ReqLinkMessage linkMessageHandle(Map<String, Object> messageMap) {
        return ClassUtils.map2Message(messageMap,ReqLinkMessage.class);
    }

    @Override
    public ReqEventMessage eventMessageHandle(Map<String, Object> messageMap) {
        return ClassUtils.map2Message(messageMap,ReqEventMessage.class);
    }
}
