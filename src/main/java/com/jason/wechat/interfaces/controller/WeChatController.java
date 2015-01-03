package com.jason.wechat.interfaces.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.jason.framework.util.Collections3;
import com.jason.framework.util.ExceptionUtils;
import com.jason.framework.web.support.ControllerSupport;
import com.jason.wechat.application.article.ArticleService;
import com.jason.wechat.application.chat.ChatService;
import com.jason.wechat.application.local.LocalService;
import com.jason.wechat.application.message.constant.MessageType;
import com.jason.wechat.application.message.handle.ReqMessageHandler;
import com.jason.wechat.application.music.MusicService;
import com.jason.wechat.application.transit.TransitService;
import com.jason.wechat.application.translate.TranslateService;
import com.jason.wechat.application.weather.WeatherService;
import com.jason.wechat.domain.message.Message;
import com.jason.wechat.domain.message.req.ReqEventMessage;
import com.jason.wechat.domain.message.req.ReqLocationMessage;
import com.jason.wechat.domain.message.req.ReqTextMessage;
import com.jason.wechat.domain.message.resp.RespMusicMessage;
import com.jason.wechat.domain.message.resp.RespNewsMessage;
import com.jason.wechat.domain.message.resp.RespTextMessage;
import com.jason.wechat.domain.message.resp.model.Article;
import com.jason.wechat.domain.message.resp.model.Music;
import com.jason.wechat.infrastruture.util.HttpUtils;
import com.jason.wechat.infrastruture.util.MessageUtils;
import com.jason.wechat.infrastruture.util.RespMessageUtils;
import com.jason.wechat.infrastruture.verify.VerifyToken;

/**
 * 微信 控制层
 * @author Jason
 * @data 2014-4-15 下午03:44:05
 */
@Controller
@RequestMapping(value = "/wechat")
public class WeChatController extends ControllerSupport {
	
	@Autowired
	private ReqMessageHandler reqMessageHandler ;
	@Autowired
	private ChatService chatService ;
	@Autowired
	private MusicService musicService ;
	@Autowired
	private ArticleService articleService ;
	@Autowired
	private TranslateService translateService ;
	@Autowired
	private WeatherService weatherService ;
	@Autowired
	private LocalService localService ;
	@Autowired
	private TransitService transitService ;
	
    /**
     * get请求 验证消息真实性 
     * 
     * @param request
     * @param response
     * @param signature 微信加密签名
     * @param timestamp 时间戳 
     * @param nonce 随机数 
     * @param echostr 随机字符串 
     * @throws Exception
     */
    @RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
    public void get(HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestParam(value = "signature") String signature,
                           @RequestParam(value = "timestamp") String timestamp,
                           @RequestParam(value = "nonce") String nonce,
                           @RequestParam(value = "echostr") String echostr) throws Exception {

    	boolean  flag = VerifyToken.checkSignature(timestamp, nonce, echostr, signature);
    	String outPut = "error";
        if (flag) {
        	outPut = echostr;
        }
        super.getLogger().info(
        		String.format("check Signature Result：%s,signature:%s,timestamp:%s,nonce:%s,echostr:%s", 
        				flag,signature,timestamp,nonce,echostr));
        response.getWriter().print(outPut);
        response.getWriter().close();
    }
    

    /**
     * post 微信唯一重要的入口
     * @param request
     * @param response
     */
    @RequestMapping(value = { "/", "" }, method = RequestMethod.POST)
    public void post(HttpServletRequest request, HttpServletResponse response) {
    	try {
			ServletInputStream in = request.getInputStream();
			String str = HttpUtils.inputStream2String(in);
	    	super.getLogger().info("inputStream2String method，str is:"+str);
	    	
	    	Message message  = reqMessageHandler.reqMessageHandle(str);
	    	String msgType = message.getMsgType();
	    	
	    	String respContent = "请求处理异常，请稍候尝试！";
	    	// 文本消息
	    	if (StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_TEXT.toString(), msgType)) {	
	    		ReqTextMessage textMessage = (ReqTextMessage) message;
	    		executeMessageTypeText(textMessage,response);
	    	}
	    	//图片消息
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_IMG.toString(), msgType)){	
				//ReqImageMessage imageMessage = (ReqImageMessage) message;
	    		respContent = "您发送的是图片消息！";
	    		
	    	}
	    	//地理位置消息.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_LOCATION.toString(), msgType)){	
	    		ReqLocationMessage locationMessage = (ReqLocationMessage) message;
	    		executeMessageTypeLocation(locationMessage,response);
	    	}
	    	//链接消息.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_LINK.toString(), msgType)){	
	    		//ReqLinkMessage linkMessage = (ReqLinkMessage) message;
	    		respContent = "您发送的是链接消息！";
	    		
	    	}
	    	//音频消息.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_VOICE.toString(), msgType)){	
	    		//ReqVoiceMessage voiceMessage = (ReqVoiceMessage) message;
	    		respContent = "您发送的是音频消息！";
	    		
	    	}
	    	//视频消息.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_VIDEO.toString(), msgType)){	
	    		//ReqVideoMessage videoMessage = (ReqVideoMessage) message;
	    		respContent = "您发送的是视频消息！";
	    		
	    	}
	    	//事件推送.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_EVENT.toString(), msgType)){	
	    		ReqEventMessage eventMessage = (ReqEventMessage) message;
	    		//事件类型
	    		String event = eventMessage.getEvent();	
	    		// 订阅
                if (StringUtils.equalsIgnoreCase(MessageType.EVENT_TYPE_SUBSCRIBE.toString(), event)) {
                    ReqEventMessage reqEventMessage = (ReqEventMessage) message;
                    executeMenu(response, reqEventMessage);
                }
                // 取消订阅
                else if (StringUtils.equalsIgnoreCase(MessageType.EVENT_TYPE_UNSUBSCRIBE.toString(), event)) {
                    //取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
	    	}else{
		    	//以上都不执行 ，则最后执行这里
		    	RespTextMessage textMessage = RespMessageUtils.writeTextMessage(respContent, message);
				writeXmlResult(response, MessageUtils.textMessageToXml(textMessage));
	    	}
		} catch (Exception e) {
			super.getLogger().error("post error！",e);
		}
    }
    
    /**
     * 接收地理位置消息类的请求，进行回复
     * @param locationMessage
     * @param response
     */
    private void executeMessageTypeLocation(ReqLocationMessage locationMessage,HttpServletResponse response){
    	String x = locationMessage.getLocation_X();	//纬度
    	String y = locationMessage.getLocation_Y();	//经度
    	List<Article> articles = localService.queryLocal(locationMessage.getFromUserName(),y, x);
    	if(Collections3.isNotEmpty(articles)){
    		RespNewsMessage newsMessage = RespMessageUtils.writeNewsMessage(articles, locationMessage);
    		writeXmlResult(response, MessageUtils.newsMessageToXml(newsMessage));
    	}else{
    		RespTextMessage textMessage = RespMessageUtils.writeTextMessage("暂时找不到资料！", locationMessage);
    		writeXmlResult(response, MessageUtils.textMessageToXml(textMessage));
    	}
		
    }
    /**
     * 接收文本消息类的请求，进行回复
     * @param textMessage 请求文本消息类
     * @param response
     */
    private void executeMessageTypeText(ReqTextMessage textMessage,HttpServletResponse response){
    	String content = StringUtils.trim(textMessage.getContent());
    	if(StringUtils.equalsIgnoreCase(content, "0") ||StringUtils.equalsIgnoreCase(content, "?") || StringUtils.equalsIgnoreCase(content, "？")){
    		executeMenu(response, textMessage);					//? 功能菜单
    		
    	}else if(StringUtils.equalsIgnoreCase(content, "1")){	//1 歌曲点播 
    		executeMusicMenu(response, textMessage);	
    		
    	}else if(StringUtils.equalsIgnoreCase(content, "2")){	//2 杰森轻博
    		executeArticle(response, textMessage);	
			
    	}else if(StringUtils.equalsIgnoreCase(content, "3")){	//3 翻译在线
    		executeTranslateMenu(response, textMessage);	
			
    	}else if(StringUtils.equalsIgnoreCase(content, "4")){	//4 天气预报
    		executeWeatherMenu(response, textMessage);
			
    	}else if(StringUtils.equalsIgnoreCase(content, "5")){	//5 附近搜索
    		executeLocalMenu(response, textMessage);
			
    	}else if(StringUtils.equalsIgnoreCase(content, "6")){	//6 聊天唠叨
    		executeChatMenu(response, textMessage);
			
    	}else if(StringUtils.equalsIgnoreCase(content, "7")){	//6 公交查询
    		executeTransitMenu(response, textMessage);
			
    	}else if(StringUtils.equalsIgnoreCase(content, "8")){	//8 网站导航
    		executeSite(response, textMessage);
			
    	}else if(StringUtils.startsWith(content, "歌曲")){
    		//executeMusic(response, textMessage, content);
    		
    	}else if(StringUtils.startsWith(content, "翻译")){
    		executeTranslate(response, textMessage, content);
    		
    	}else if(StringUtils.startsWith(content, "天气")){
    		executeWeather(response, textMessage, content);
    		
    	}else if(StringUtils.startsWith(content, "附近")){
    		executeLocal(response, textMessage, content);
    		
    	}else if(StringUtils.startsWith(content, "公交")){
    		executeTransit(response, textMessage, content);
    		
    	}else if(StringUtils.equalsIgnoreCase(content, "网站导航")||StringUtils.equalsIgnoreCase(content, "导航")||StringUtils.equalsIgnoreCase(content, "网站")){
    		executeSite(response, textMessage);
    		
    	}else {
    		//如果以上都不执行，那就让小九来吧！
    		//executeChat(response, textMessage);
    	}
    }
    /**
     * 机器人聊天
     * @param response
     * @param textMessage
     */
    private void executeChat(HttpServletResponse response, ReqTextMessage textMessage){

		String chatStr = chatService.chat(textMessage.getContent(), textMessage.getFromUserName(), textMessage.getToUserName());
		
    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(chatStr, textMessage);
		writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
    }
    /**
     * 执行图文 回复
     * @param response
     * @param message
     */
    private void executeArticle(HttpServletResponse response, ReqTextMessage textMessage){
		List<Article> articles = articleService.queryArticle();
		if(Collections3.isNotEmpty(articles)){
			RespNewsMessage newsMessage = RespMessageUtils.writeNewsMessage(articles, textMessage);
			writeXmlResult(response, MessageUtils.newsMessageToXml(newsMessage));
    	}else{
    		RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage("暂时找不到资料！", textMessage);
    		writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
    	}
    }
    private void executeSite(HttpServletResponse response, ReqTextMessage textMessage){
		List<Article> articles = new ArrayList<Article>();
		Article hao123 = new Article();
		hao123.setDescription("hao123网站导航");
		hao123.setPicUrl("http://m.hao123.com/static/img/logo/logo1202p.gif");
		hao123.setTitle("hao123");
		hao123.setUrl("http://m.hao123.com/");
		articles.add(hao123);
		
		Article google = new Article();
		google.setDescription("谷歌搜索引擎");
		google.setPicUrl("https://www.google.com.hk//images/srpr/logo11w.png");
		google.setTitle("谷歌");
		google.setUrl("https://www.google.com.hk");
		articles.add(google);
		
		Article baidu = new Article();
		baidu.setDescription("百度搜索引擎");
		baidu.setPicUrl("http://m.baidu.com/static/index/l.gif");
		baidu.setTitle("百度");
		baidu.setUrl("http://m.baidu.com");
		articles.add(baidu);
		
		Article kugou = new Article();
		kugou.setDescription("酷狗音乐引擎");
		kugou.setPicUrl("http://www.kugou.com/favicon.ico");
		kugou.setTitle("酷狗");
		kugou.setUrl("http://m.kugou.com/");
		articles.add(kugou);
		
		RespNewsMessage newsMessage = RespMessageUtils.writeNewsMessage(articles, textMessage);
		writeXmlResult(response, MessageUtils.newsMessageToXml(newsMessage));
    }
    
    /**
     * 执行附近搜索 回复
     * @param response
     * @param message
     * @param content
     */
    private void executeTransit(HttpServletResponse response,ReqTextMessage message,String content){
    	// 将公交2个字及公交后面的+、空格、-等特殊符号去掉  
        String keyWord = content.replaceAll("^公交[\\+ !@#%^_=]?", "");
        this.getLogger().info("content:"+content+" keyWord:"+keyWord);
        
        if(StringUtils.isBlank(keyWord)){
        	executeTransitMenu(response, message);
        }else{
        	String result = "木有結果！";
    		String[] key = StringUtils.split(keyWord, "，|,");
    		String city = key[0];
    		String q = key[1];
    		if(StringUtils.indexOf(q, "-")>0){//如果有 -
    			String[]  place = StringUtils.split(q, "-");
    			String from = place[0];
    			String to = place[1];
    			this.getLogger().info("city:"+city+" from:"+from+" to:"+to);
    			result = transitService.queryTransit(city, from, to);
    		}else{
    			this.getLogger().info("city:"+city+" q:"+q);
    			result = transitService.queryTransit(city, q);
    		}
    		
			RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(result, message);
 			writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
        }
    }
    /**
     * 执行附近搜索 回复
     * @param response
     * @param message
     * @param content
     */
    private void executeLocal(HttpServletResponse response,ReqTextMessage message,String content){
    	// 将附近2个字及附近后面的+、空格、-等特殊符号去掉  
        String keyWord = content.replaceAll("^附近[\\+ ~!@#%^-_=]?", "");
        if(StringUtils.isBlank(keyWord)){
        	executeLocalMenu(response, message);
        }else{
        	String result = "木有結果！";
        	try {
        		List<Article> articles = localService.queryLocal(message.getFromUserName(), keyWord);
            	if(Collections3.isNotEmpty(articles)){
            		RespNewsMessage newsMessage = RespMessageUtils.writeNewsMessage(articles, message);
            		writeXmlResult(response, MessageUtils.newsMessageToXml(newsMessage));
            	}else{
            		result = "木有結果！";
            	}
			} catch (InvalidCacheLoadException e) {
				result = "请先发送地理位置！\n 回复“？”显示主菜单";
				this.getLogger().error("Invalid Cache Load Exception...",e);
			}
			RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(result, message);
 			writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
        }
    }
    /**
     * 执行 天气预报 回复
     * @param response
     * @param message
     * @param content
     */
    private void executeWeather(HttpServletResponse response,ReqTextMessage message,String content){
    	// 将歌曲2个字及歌曲后面的+、空格、-等特殊符号去掉  
        String keyWord = content.replaceAll("^天气[\\+ ~!@#%^-_=]?", "");
        if(StringUtils.isBlank(keyWord)){
        	executeWeatherMenu(response, message);
        }else{
        	String str = weatherService.queryWeather(keyWord);
        	String returnStr = null;
        	if(StringUtils.isBlank(str)){
        		returnStr = "对不起，你输入的关键字查询不了！";
        	}else{
        		returnStr = str;
        	}
	    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(returnStr, message);
			writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
        }
    }
    /**
     * 翻译在线 回复
     * @param response
     * @param message
     * @param content
     */
    private void executeTranslate(HttpServletResponse response,ReqTextMessage message,String content){
    	// 将歌曲2个字及歌曲后面的+、空格、-等特殊符号去掉  
        String keyWord = content.replaceAll("^翻译[\\+ ~!@#%^-_=]?", "");
        if(StringUtils.isBlank(keyWord)){
        	executeTranslateMenu(response, message);
        }else{
        	String str = translateService.translate(keyWord);
        	String returnStr = null;
        	if(StringUtils.isBlank(str)){
        		returnStr = "对不起，你输入的关键字翻译不了！";
        	}else{
        		returnStr = str;
        	}
	    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(returnStr, message);
			writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
        }
    }
    
	/**
	 * 执行音乐回复
	 * @param response
	 * @param message  请求文本消息类
	 * @param content 关键字
	 */
	private void executeMusic(HttpServletResponse response,ReqTextMessage message,String content){
    	// 将歌曲2个字及歌曲后面的+、空格、-等特殊符号去掉  
        String keyWord = content.replaceAll("^歌曲[\\+ ~!@#%^-_=]?", "");
        if(StringUtils.isBlank(keyWord)){
        	executeMusicMenu(response, message);
        }else{
            // 搜索音乐  
            Music music = musicService.searchMusic(keyWord);  
            if (null == music) {  
    	    	//相应找不到歌曲的文本消息
    	    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage("对不起，没有找到你想听的歌曲<" + keyWord + ">。", message);
    			writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
            } else {  
                // 响应音乐消息  
            	RespMusicMessage musicMessage = RespMessageUtils.writeMusicMessage(music, message);
                writeXmlResult(response, MessageUtils.musicMessageToXml(musicMessage));
            }  
        }
    }
	
	
	/**
	 * xml 响应
	 * @param response
	 * @param message
	 */
	private  void writeXmlResult(HttpServletResponse response, Object message) {
		try {
			response.setContentType("text/xml");
			String result = String.format("%s", message);
			super.getLogger().info("writeXmlResult is : "+ result);
			response.getWriter().write(result);
		} catch (Exception e) {
			throw ExceptionUtils.toUnchecked(e);
		}
	}
	

	
	
	
	//-------------------------------华丽的分隔     下面的是菜单回复----------------------------------
	
	
	/**
     * 执行菜单 回复
     * @param response
     * @param message 请求文本消息类
     */
    private void executeMenu(HttpServletResponse response, Message message) {
    	StringBuffer buffer = new StringBuffer()
        	.append("您好，我是小杰森，请回复数字选择服务：").append("\n\n")
        	.append("1 歌曲点播").append(" ").append("\ue03e").append("\n")
        	.append("2 杰森轻博").append("\n")
        	.append("3 翻译在线").append("\n")
        	.append("4 天气预报").append(" ").append("\ue04a").append("\n")
        	.append("5 附近搜索").append(" ").append("\ue114").append("\n")
        	.append("6 聊天唠叨").append("\n")
        	.append("7 公交查詢").append(" ").append("\ue159").append("\n")
        	.append("8 网站导航").append("\n\n")
        	.append("回复“?”显示此帮助菜单");
    	
    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(buffer.toString(), message);
		writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
	}
    /**
     * 执行“7 公交查詢”菜单回复
     * @param response
     * @param message
     */
    private void executeTransitMenu(HttpServletResponse response, ReqTextMessage message) {
    	StringBuffer buffer = new StringBuffer()
	    	.append("公交查詢操作指南").append("\n\n")
	    	.append("查詢城市公交路线").append("\n")
	    	.append("回复：公交城市,路线名称").append("\n")
	    	.append("例如：公交珠海,11路").append("\n\n")
	    	.append("查詢城市公交驾乘").append("\n")
	    	.append("回复：公交城市,起点-终点").append("\n")
	    	.append("例如：公交珠海,凤凰北-湾仔沙").append("\n\n")
	    	.append("回复“?”显示主菜单");
    	
    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(buffer.toString(), message);
		writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
	}
    /**
     * 执行“6 聊天唠叨”菜单回复
     * @param response
     * @param message
     */
    private void executeChatMenu(HttpServletResponse response, ReqTextMessage message) {
    	//StringBuffer buffer = new StringBuffer()
	    //	.append("聊天唠叨操作指南").append("\n\n")
	    //	.append("无聊？来找杰森机器人聊天唠叨一下吧！有问必答").append("\n")
	    //	.append("例如：我顶你、无聊").append("\n\n")
	    //	.append("回复“?”显示主菜单");
    	
    	StringBuffer buffer = new StringBuffer()
    		.append("聊天唠叨 功能有待开发！");
    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(buffer.toString(), message);
		writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
	}

    /**
     * 执行“1 歌曲点播”菜单回复
     * @param response
     * @param message 请求文本消息类
     */
	private void executeMusicMenu(HttpServletResponse response,
			ReqTextMessage message) {
		//StringBuffer buffer = new StringBuffer()
		//	.append("歌曲点播操作指南").append("\n\n")
		//	.append("回复：歌曲+歌曲关键字").append("\n")
		//	.append("例如：歌曲存在").append("\n\n")
		//	.append("回复“?”显示主菜单");
		StringBuffer buffer = new StringBuffer()
			.append("歌曲点播 功能有待开发！");
		
    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(buffer.toString(), message);
		writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
	}
    /**
     * 执行“5 附近搜索”菜单回复
     * @param response
     * @param message
     */
    private void executeLocalMenu(HttpServletResponse response, ReqTextMessage message) {
    	StringBuffer buffer = new StringBuffer()
	    	.append("附近搜索操作指南").append("\n\n")
	    	.append("1)发送地理位置").append("\n")
	    	.append("点击窗口底部的“+”按钮，选择“位置”，点击“发送”").append("\n")
	    	.append("默认搜索周边的“美食”").append("\n\n")
	    	
	    	.append("2)指定关键词搜索").append("\n")
	    	.append("回复：附近+关键词").append("\n")
	    	.append("例如：附近ATM、附近酒店").append("\n\n")
	    	
	    	.append("回复“?”显示主菜单");
    	
    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(buffer.toString(), message);
		writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
	}
    
    /**
     * 执行“4 天气预报”菜单回复
     * @param response
     * @param message 请求文本消息类
     */
    private void executeWeatherMenu(HttpServletResponse response, ReqTextMessage message) {
    	StringBuffer buffer = new StringBuffer()
	    	.append("天气预报操作指南").append("\n\n")
	    	.append("回复：天气+城市").append("\n")
	    	.append("例如：天气北京").append("\n\n")
	    	.append("回复“?”显示主菜单");

    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(buffer.toString(), message);
		writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
	}
    /**
     * 执行“3 翻译在线”菜单回复
     * @param response
     * @param message 请求文本消息类
     */
    private void executeTranslateMenu(HttpServletResponse response, ReqTextMessage message) {
    	StringBuffer buffer = new StringBuffer()
	    	.append("翻译在线操作指南").append("\n\n")
	    	.append("回复：翻译+关键字").append("\n")
	    	.append("例如：翻译我爱你、翻译I love you").append("\n\n")
	    	.append("回复“?”显示主菜单");
    	
    	RespTextMessage respTextMessage = RespMessageUtils.writeTextMessage(buffer.toString(), message);
		writeXmlResult(response, MessageUtils.textMessageToXml(respTextMessage));
	}
    
}

