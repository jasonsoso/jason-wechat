package com.jason.wechat.application.message.handle;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jason.wechat.application.message.constant.MessageType;
import com.jason.wechat.domain.message.Message;
import com.jason.wechat.domain.message.req.ReqEventMessage;
import com.jason.wechat.domain.message.req.ReqImageMessage;
import com.jason.wechat.domain.message.req.ReqLinkMessage;
import com.jason.wechat.domain.message.req.ReqLocationMessage;
import com.jason.wechat.domain.message.req.ReqTextMessage;
import com.jason.wechat.domain.message.req.ReqVideoMessage;
import com.jason.wechat.domain.message.req.ReqVoiceMessage;


/**
 * 处理短信信息接口
 * @author Jason
 * @data 2014-3-31 下午08:10:46
 */
public abstract class ReqMessageHandler{
	private final Logger logger = LoggerFactory.getLogger(ReqMessageHandler.class);

	/**
	 * 根据字符串 组装成 Message对象
	 * @param xml xml字符串
	 * @return Message
	 * @throws Exception
	 */
	public  Message reqMessageHandle(String xml)throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();

        Document doucument = DocumentHelper.parseText(xml);
        // 得到xml根元素
        Element root =   doucument.getRootElement();
        // 得到根元素的所有子节点
        @SuppressWarnings("unchecked")
		List<Element> elementList = root.elements();
        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
            logger.info(e.getName()+":"+e.getText());
        }
        MessageType messageType[] =  MessageType.values();

        for(MessageType type : messageType){
            if(type.toString().equals(map.get("MsgType"))){
            	//反射机制
                Method method =  this.getClass().getMethod(map.get("MsgType")+"MessageHandle",Map.class);
                return (Message)method.invoke(this,map);
            }
        }
        return null;
	}

    /**
     * 处理文本消息
     * @param messageMap
     * @return
     */
    public abstract ReqTextMessage textMessageHandle(Map<String, Object> messageMap);

    /**
     * 处理图片消息
     * @param messageMap
     * @return
     */
    public abstract ReqImageMessage imageMessageHandle(Map<String, Object> messageMap);

    /**
     * 处理语音消息
     * @param messageMap
     * @return
     */
    public abstract ReqVoiceMessage voiceMessageHandle(Map<String, Object> messageMap);

    /**
     * 处理视频消息
     * @param messageMap
     * @return
     */
    public abstract ReqVideoMessage videoMessageHandle(Map<String, Object> messageMap);

    /**
     * 处理地理位置消息
     * @param messageMap
     * @return
     */
    public abstract ReqLocationMessage locationMessageHandle(Map<String, Object> messageMap);

    /**
     * 处理链接消息
     * @param messageMap
     * @return
     */
    public abstract ReqLinkMessage linkMessageHandle(Map<String, Object> messageMap);

    /**
     * 处理事件信息
     * @param messageMap
     * @return
     */
    public abstract ReqEventMessage eventMessageHandle(Map<String, Object> messageMap);
}
