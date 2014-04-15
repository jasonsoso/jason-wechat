package com.jason.wechat.application.chat;



public interface ChatService {

	/**
	 * 聊天
	 * @param key 关键字
	 * @param from FromUserName
	 * @param to ToUserName
	 * @return String
	 */
	String chat(String key,String from,String to);
}