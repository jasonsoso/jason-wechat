package com.jason.wechat.domain.message.req;

import com.jason.wechat.domain.message.Message;

/**
 * 请求消息基类
 * @author Jason
 * @data 2014-3-30 下午03:49:53
 */
public class ReqMessage extends Message{

    /** 消息id，64位整型 */
    private Long MsgId ;

    public Long getMsgId() {
        return MsgId;
    }

    public void setMsgId(Long msgId) {
        MsgId = msgId;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
    		.append(";ReqMessage{")
	    	.append("MsgId:").append(MsgId)
			.append("}");
        return sb.toString();
    }
}
