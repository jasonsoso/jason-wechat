package com.jason.wechat.domain.message;

import com.jason.wechat.infrastruture.util.ClassUtils;

/**
 * 消息基类
 * @author Jason
 * @data 2014-3-30 下午03:44:08
 */
public class Message {

    /** 开发者微信号  或者  送方帐号（一个OpenID）  */
    private String ToUserName;

    /** 发送方帐号（一个OpenID） 或者 开发者微信号  */
    private String FromUserName;

    /** 消息创建时间 （整型）  */
    private Long CreateTime;

    /** 消息类型  */
    private String MsgType;


    /**
     * 通过set方法给对象赋值
     * @param <T>
     * @param fieldName
     * @param obj
     * @return <T extends Message> T
     */
    @SuppressWarnings("unchecked")
	public <T extends Message> T setField(String fieldName,Object obj){
        try {
            ClassUtils.parSetVal(fieldName,this,obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return (T)this;
    }


    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder()
    		.append("Message{")
    		.append("ToUserName:").append(ToUserName).append(",")
    		.append("FromUserName:").append(FromUserName).append(",")
    		.append("CreateTime:").append(CreateTime).append(",")
    		.append("MsgType:").append(MsgType)
    		.append("}");
        return sb.toString();
    }
}
