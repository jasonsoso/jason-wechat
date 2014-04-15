package com.jason.wechat.domain.message.req;


/**
 * 请求文本消息类
 * @author Jason
 * @data 2014-3-30 下午11:07:08
 */
public class ReqTextMessage extends ReqMessage {

    /** 文本消息内容 */
    private  String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";ReqTextMessage{")
	    	.append("Content:").append(Content)
			.append("}");
	    return sb.toString();
    }
}
