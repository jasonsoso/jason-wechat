package com.jason.wechat.application.chat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jason.wechat.AbstractTestBase;

public class ChatServiceTest extends AbstractTestBase {
	
	@Autowired
	private ChatService chatService;
	@Test
	public void testChat() throws Exception {
		String reply = chatService.chat("澳门天气", "wx370d7ef0ba4885f3", "oIRE1uMiR7o5FniEDiaB7MVT3wA4");
		System.out.println(reply);
	}
}
