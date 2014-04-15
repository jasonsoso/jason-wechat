package com.jason.wechat.domain.message.resp;

import java.util.Date;

import org.junit.Test;

import com.jason.wechat.AbstractTestBase;
import com.jason.wechat.application.message.constant.MessageType;
import com.jason.wechat.infrastruture.util.MessageUtil;



public class RespTextMessageTest extends AbstractTestBase {

	@Test
	public void testSendGet() throws Exception {
		RespTextMessage text = new RespTextMessage();
    	text.setContent("对方在说：111");
    	Date now = new Date();
    	text.setCreateTime(now.getTime());
    	text.setFromUserName("username");
    	text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
    	text.setToUserName("touserName");
    	
    	System.out.println(MessageUtil.textMessageToXml(text));
    	
    	
	}
	
}
