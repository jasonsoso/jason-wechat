package com.jason.wechat.application.chat.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jason.framework.util.EncodeUtils;
import com.jason.wechat.application.chat.ChatService;
import com.jason.wechat.infrastruture.http.HttpRequester;
import com.jason.wechat.infrastruture.http.HttpRespons;

@Service
public class ChatServiceImpl implements ChatService {
	
	@Override
	public String chat(String key, String from, String to) {
		String k = EncodeUtils.urlEncode(key);
		String f = EncodeUtils.urlEncode(from);
		String t = EncodeUtils.urlEncode(to);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("chat", k);
		map.put("db", EncodeUtils.urlEncode("tanjianna"));
		map.put("pw", EncodeUtils.urlEncode("na5714665"));
		map.put("from", f);
		map.put("to", t);
		HttpRespons httpRespons = HttpRequester.sendGet("http://www.xiaojo.com/api5.php", map);
		String contentStr = httpRespons.getContent();
		String content = EncodeUtils.urlDecode(contentStr);
		return content;
	}

}
