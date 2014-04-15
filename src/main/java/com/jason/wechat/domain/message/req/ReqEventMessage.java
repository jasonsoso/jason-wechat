package com.jason.wechat.domain.message.req;

import com.jason.wechat.domain.message.Message;

/**
 * 请求事件消息类
 * @author Jason
 * @data 2014-3-30 下午11:18:10
 */
public class ReqEventMessage extends Message {

    /** 事件类型 */
    private String Event;

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";ReqEventMessage{")
	    	.append("Event:").append(Event)
			.append("}");
	    return sb.toString();
    }
}
