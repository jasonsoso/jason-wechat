package com.jason.wechat.application.message.constant;

/**
 * 消息类型
 * @author Jason
 * @data 2014-3-30 下午10:01:06
 */
/**
 * @author Jason
 * @data 2014-3-30 下午10:06:53
 */
public enum MessageType {

    /**请求消息类型：文本*/
    REQ_MESSAGE_TYPE_TEXT("text"),
    /** 请求消息类型:图片 */
    REQ_MESSAGE_TYPE_IMG("image"),
    /** 请求消息类型:语音 */
    REQ_MESSAGE_TYPE_VOICE("voice"),
    /** 请求消息类型:视频 */
    REQ_MESSAGE_TYPE_VIDEO("video"),
    /** 请求消息类型:地理位置信息 */
    REQ_MESSAGE_TYPE_LOCATION("location"),
    /** 请求消息类型:链接 */
    REQ_MESSAGE_TYPE_LINK("link"),
    
    /** 请求消息类型:事件 */
    REQ_MESSAGE_TYPE_EVENT("event"),
    
    
    /** 返回消息类型：文本 */
    RESP_MESSAGE_TYPE_TEXT("text"),
    /** 返回消息类型：图片 */
    RESP_MESSAGE_TYPE_IMAGE("image"),
    /** 返回消息类型：语音 */
    RESP_MESSAGE_TYPE_VOICE("voice"),
    /** 返回消息类型：视频 */
    RESP_MESSAGE_TYPE_VIDEO("video"),
    /** 返回消息类型：音乐 */
    RESP_MESSAGE_TYPE_MUSIC("music"),
    /** 返回消息类型：图文 */
    RESP_MESSAGE_TYPE_NEWS("news"),
    
    
   
    
    
    /** 事件类型：订阅  */
    EVENT_TYPE_SUBSCRIBE("subscribe"),
    /** 事件类型：取消订阅  */
    EVENT_TYPE_UNSUBSCRIBE("unsubscribe"),
    ;

    private String value;

    MessageType(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}
