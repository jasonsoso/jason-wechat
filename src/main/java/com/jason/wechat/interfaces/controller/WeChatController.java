package com.jason.wechat.interfaces.controller;

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

import com.jason.framework.util.ExceptionUtils;
import com.jason.framework.web.support.ControllerSupport;
import com.jason.wechat.application.article.ArticleService;
import com.jason.wechat.application.chat.ChatService;
import com.jason.wechat.application.message.constant.MessageType;
import com.jason.wechat.application.message.handle.ReqMessageHandler;
import com.jason.wechat.application.music.MusicService;
import com.jason.wechat.application.translate.TranslateService;
import com.jason.wechat.application.weather.WeatherService;
import com.jason.wechat.domain.message.Message;
import com.jason.wechat.domain.message.req.ReqEventMessage;
import com.jason.wechat.domain.message.req.ReqImageMessage;
import com.jason.wechat.domain.message.req.ReqLinkMessage;
import com.jason.wechat.domain.message.req.ReqLocationMessage;
import com.jason.wechat.domain.message.req.ReqTextMessage;
import com.jason.wechat.domain.message.req.ReqVideoMessage;
import com.jason.wechat.domain.message.req.ReqVoiceMessage;
import com.jason.wechat.domain.message.resp.RespMusicMessage;
import com.jason.wechat.domain.message.resp.RespNewsMessage;
import com.jason.wechat.domain.message.resp.RespTextMessage;
import com.jason.wechat.domain.message.resp.model.Article;
import com.jason.wechat.domain.message.resp.model.Music;
import com.jason.wechat.infrastruture.util.DateUtils;
import com.jason.wechat.infrastruture.util.HttpUtils;
import com.jason.wechat.infrastruture.util.MessageUtils;
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
    /**
     * get请求
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
    

    @RequestMapping(value = { "/", "" }, method = RequestMethod.POST)
    public void post(HttpServletRequest request, HttpServletResponse response) {
    	super.getLogger().info("post controller -------------");
    	try {
			ServletInputStream in = request.getInputStream();
			String str = HttpUtils.inputStream2String(in);
	    	super.getLogger().info("inputStream2String method，str is:"+str);
	    	
	    	Message message  = reqMessageHandler.reqMessageHandle(str);
	    	String msgType = message.getMsgType();
	    	super.getLogger().info("message is : "+message.toString());
	    	
	    	String respContent = "请求处理异常，请稍候尝试！";
	    	// 文本消息
	    	if (StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_TEXT.toString(), msgType)) {	
	    		ReqTextMessage textMessage = (ReqTextMessage) message;
	    		executeMessageTypeText(textMessage,response);
	    	}
	    	//图片消息
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_IMG.toString(), msgType)){	
	    		ReqImageMessage imageMessage = (ReqImageMessage) message;
	    		respContent = "您发送的是图片消息！";
	    		
	    	}
	    	//地理位置消息.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_LOCATION.toString(), msgType)){	
	    		ReqLocationMessage imageMessage = (ReqLocationMessage) message;
	    		respContent = "您发送的是地理位置消息！";
	    		
	    	}
	    	//链接消息.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_LINK.toString(), msgType)){	
	    		ReqLinkMessage linkMessage = (ReqLinkMessage) message;
	    		respContent = "您发送的是链接消息！";
	    		
	    	}
	    	//音频消息.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_VOICE.toString(), msgType)){	
	    		ReqVoiceMessage voiceMessage = (ReqVoiceMessage) message;
	    		respContent = "您发送的是音频消息！";
	    		
	    	}
	    	//视频消息.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_VIDEO.toString(), msgType)){	
	    		ReqVideoMessage videoMessage = (ReqVideoMessage) message;
	    		respContent = "您发送的是视频消息！";
	    		
	    	}
	    	//事件推送.
	    	else if(StringUtils.equalsIgnoreCase(MessageType.REQ_MESSAGE_TYPE_EVENT.toString(), msgType)){	
	    		ReqEventMessage eventMessage = (ReqEventMessage) message;
	    		
	    		//事件类型
	    		String event = eventMessage.getEvent();	
	    		
	    		// 订阅
                if (StringUtils.equalsIgnoreCase(MessageType.EVENT_TYPE_SUBSCRIBE.toString(), event)) {
                    respContent = "订阅事件";
                }
                // 取消订阅
                else if (StringUtils.equalsIgnoreCase(MessageType.EVENT_TYPE_UNSUBSCRIBE.toString(), event)) {
                    //取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
	    	}
	    	
	    	//以上都不执行 ，则最后执行这里
            RespTextMessage text = new RespTextMessage();
	    	text.setContent(respContent);
	    	text.setCreateTime(DateUtils.currentTime());
	    	text.setFromUserName(message.getToUserName());
	    	text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
	    	text.setToUserName(message.getFromUserName());
	    	
			writeXmlResult(response, MessageUtils.textMessageToXml(text));
			
			
		} catch (Exception e) {
			super.getLogger().error("post error",e);
		}
    }
    
    /**
     * 接收文本消息类的请求，进行回复
     * @param textMessage 请求文本消息类
     * @param response
     */
    private void executeMessageTypeText(ReqTextMessage textMessage,HttpServletResponse response){
    	String content = StringUtils.trim(textMessage.getContent());
    	if(StringUtils.equalsIgnoreCase(content, "?") || StringUtils.equalsIgnoreCase(content, "？")){
    		super.getLogger().info("textMessage ? -------------");
    		executeMenu(response, textMessage);
    	}else if(StringUtils.equalsIgnoreCase(content, "1")){
    		super.getLogger().info("textMessage 1 -------------");
    		executeMusicMenu(response, textMessage);
			
    	}else if(StringUtils.equalsIgnoreCase(content, "2")){
    		super.getLogger().info("textMessage 2 -------------");
    		executeArticle(response, textMessage);
			
    	}else if(StringUtils.equalsIgnoreCase(content, "3")){
    		super.getLogger().info("textMessage 3 -------------");
    		executeTranslateMenu(response, textMessage);
			
    	}else if(StringUtils.equalsIgnoreCase(content, "4")){
    		super.getLogger().info("textMessage 4 -------------");
    		executeWeatherMenu(response, textMessage);
			
    	}else if(StringUtils.startsWith(content, "歌曲")){
    		super.getLogger().info("textMessage song -------------");
    		executeMusic(response, textMessage, content);
    		
    	}else if(StringUtils.startsWith(content, "翻译")){
    		super.getLogger().info("textMessage translate -------------");
    		executeTranslate(response, textMessage, content);
    		
    	}else if(StringUtils.startsWith(content, "天气")){
    		super.getLogger().info("textMessage translate -------------");
    		executeWeather(response, textMessage, content);
    		
    	}else {
    		super.getLogger().info("textMessage xiaojo -------------");
    		executeChat(response, textMessage);
    	}
    }
    /**
     * 机器人聊天
     * @param response
     * @param textMessage
     */
    private void executeChat(HttpServletResponse response, ReqTextMessage textMessage){

		String chatStr = chatService.chat(textMessage.getContent(), textMessage.getFromUserName(), textMessage.getToUserName());
		
		RespTextMessage text = new RespTextMessage();
    	text.setContent(chatStr);
    	text.setCreateTime(DateUtils.currentTime());
    	text.setFromUserName(textMessage.getToUserName());
    	text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
    	text.setToUserName(textMessage.getFromUserName());
    	
		writeXmlResult(response, MessageUtils.textMessageToXml(text));
    }
    /**
     * 执行图文 回复
     * @param response
     * @param message
     */
    private void executeArticle(HttpServletResponse response, ReqTextMessage textMessage){
		List<Article> articles = articleService.queryArticle();
		
		RespNewsMessage newsMessage = new RespNewsMessage();
		newsMessage.setArticleCount(articles.size());
		newsMessage.setArticles(articles);
		newsMessage.setCreateTime(DateUtils.currentTime());
		newsMessage.setFromUserName(textMessage.getToUserName());
		newsMessage.setMsgType(MessageType.RESP_MESSAGE_TYPE_NEWS.toString());
		newsMessage.setToUserName(textMessage.getFromUserName());
		
		writeXmlResult(response, MessageUtils.newsMessageToXml(newsMessage));
    }
    /**
     * 执行菜单 回复
     * @param response
     * @param message 请求文本消息类
     */
    private void executeMenu(HttpServletResponse response, ReqTextMessage message) {
    	StringBuffer buffer = new StringBuffer()
        	.append("您好，我是小杰森，请回复数字选择服务：").append("\n\n")
        	.append("1 歌曲点播").append("\n")
        	.append("2 杰森轻博").append("\n")
        	.append("3 在线翻译").append("\n")
        	.append("4 天气预报").append("\n\n")
        	.append("回复“?”显示此帮助菜单");
    	RespTextMessage text = new RespTextMessage();
    	text.setContent(buffer.toString());
    	text.setCreateTime(DateUtils.currentTime());
    	text.setFromUserName(message.getToUserName());
    	text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
    	text.setToUserName(message.getFromUserName());
    	
		writeXmlResult(response, MessageUtils.textMessageToXml(text));
	}
    /**
     * 执行天气预报菜单 回复
     * @param response
     * @param message 请求文本消息类
     */
    private void executeWeatherMenu(HttpServletResponse response, ReqTextMessage message) {
    	StringBuffer buffer = new StringBuffer()
	    	.append("天气预报操作指南").append("\n\n")
	    	.append("回复：天气+城市").append("\n")
	    	.append("例如：天气北京").append("\n\n")
	    	.append("回复“?”显示主菜单");
    	RespTextMessage text = new RespTextMessage();
    	text.setContent(buffer.toString());
    	text.setCreateTime(DateUtils.currentTime());
    	text.setFromUserName(message.getToUserName());
    	text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
    	text.setToUserName(message.getFromUserName());
    	
		writeXmlResult(response, MessageUtils.textMessageToXml(text));
	}
    /**
     * 执行翻译菜单 回复
     * @param response
     * @param message 请求文本消息类
     */
    private void executeTranslateMenu(HttpServletResponse response, ReqTextMessage message) {
    	StringBuffer buffer = new StringBuffer()
	    	.append("在线翻译操作指南").append("\n\n")
	    	.append("回复：翻译+关键字").append("\n")
	    	.append("例如：翻译我爱你、翻译I love you").append("\n\n")
	    	.append("回复“?”显示主菜单");
    	RespTextMessage text = new RespTextMessage();
    	text.setContent(buffer.toString());
    	text.setCreateTime(DateUtils.currentTime());
    	text.setFromUserName(message.getToUserName());
    	text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
    	text.setToUserName(message.getFromUserName());
    	
		writeXmlResult(response, MessageUtils.textMessageToXml(text));
	}

    
    /**
     * 天气预报 回复
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
        	String str = weatherService.weather(keyWord);
        	String returnStr = null;
        	if(StringUtils.isBlank(str)){
        		returnStr = "对不起，你输入的关键字查询不了！";
        	}else{
        		returnStr = str;
        	}
            RespTextMessage text = new RespTextMessage();
	    	text.setContent(returnStr);
	    	text.setCreateTime(DateUtils.currentTime());
	    	text.setFromUserName(message.getToUserName());
	    	text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
	    	text.setToUserName(message.getFromUserName());
	    	
			writeXmlResult(response, MessageUtils.textMessageToXml(text));
        }
    }
    /**
     * 在线翻译 回复
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
            RespTextMessage text = new RespTextMessage();
	    	text.setContent(returnStr);
	    	text.setCreateTime(DateUtils.currentTime());
	    	text.setFromUserName(message.getToUserName());
	    	text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
	    	text.setToUserName(message.getFromUserName());
	    	
			writeXmlResult(response, MessageUtils.textMessageToXml(text));
        }
    }
    /**
     * 执行音乐菜单 回复
     * @param response
     * @param message 请求文本消息类
     */
	private void executeMusicMenu(HttpServletResponse response,
			ReqTextMessage message) {
		StringBuffer buffer = new StringBuffer()
			.append("歌曲点播操作指南").append("\n\n")
			.append("回复：歌曲+歌曲关键字").append("\n")
			.append("例如：歌曲存在").append("\n\n")
			.append("回复“?”显示主菜单");

		RespTextMessage text = new RespTextMessage();
		text.setContent(buffer.toString());
		text.setCreateTime(DateUtils.currentTime());
		text.setFromUserName(message.getToUserName());
		text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
		text.setToUserName(message.getFromUserName());

		writeXmlResult(response, MessageUtils.textMessageToXml(text));
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
            // 未搜索到音乐  
            if (null == music) {  
                RespTextMessage text = new RespTextMessage();
    	    	text.setContent("对不起，没有找到你想听的歌曲<" + keyWord + ">。");
    	    	text.setCreateTime(DateUtils.currentTime());
    	    	text.setFromUserName(message.getToUserName());
    	    	text.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
    	    	text.setToUserName(message.getFromUserName());
    	    	
    			writeXmlResult(response, MessageUtils.textMessageToXml(text));
            } else {  
                // 音乐消息  
            	RespMusicMessage musicMessage = new RespMusicMessage();  
                musicMessage.setToUserName(message.getFromUserName());  
                musicMessage.setFromUserName(message.getToUserName());  
                musicMessage.setCreateTime(DateUtils.currentTime());  
                musicMessage.setMsgType(MessageType.RESP_MESSAGE_TYPE_MUSIC.toString());  
                musicMessage.setMusic(music);
                
                writeXmlResult(response, MessageUtils.musicMessageToXml(musicMessage));
            }  
        }
    }
	/**
	 * xml 相应
	 * @param response
	 * @param message
	 */
	private static void writeXmlResult(HttpServletResponse response, Object message) {
		try {
			response.setContentType("text/xml");
			response.getWriter().write(String.format("%s", message));
		} catch (Exception e) {
			throw ExceptionUtils.toUnchecked(e);
		}
	}

}

