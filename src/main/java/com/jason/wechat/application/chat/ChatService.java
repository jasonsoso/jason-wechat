package com.jason.wechat.application.chat;



/**
 * 小九 聊天唠叨 
 * @author Jason
 * @data 2014-4-24 下午12:00:05
 */
public interface ChatService {

	/**
	 * 聊天唠叨
	 * @param key 关键字
	 * @param from FromUserName
	 * @param to ToUserName
	 * @return String
	 */
	String chat(String key,String from,String to);
}