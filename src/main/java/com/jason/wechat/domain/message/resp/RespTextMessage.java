package com.jason.wechat.domain.message.resp;



/**
 * 回复文本消息类
 * @author Jason
 * @data 2014-3-30 下午03:54:34
 */
public class RespTextMessage extends RespMessage{

    /** 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）  */
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";RespTextMessage{")
	    	.append("Content:").append(Content)
			.append("}");
	    return sb.toString();
    }
}
