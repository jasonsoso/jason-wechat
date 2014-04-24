package com.jason.wechat.infrastruture.util;


import java.util.List;

import com.jason.wechat.application.message.constant.MessageType;
import com.jason.wechat.domain.message.Message;
import com.jason.wechat.domain.message.resp.RespMusicMessage;
import com.jason.wechat.domain.message.resp.RespNewsMessage;
import com.jason.wechat.domain.message.resp.RespTextMessage;
import com.jason.wechat.domain.message.resp.model.Article;
import com.jason.wechat.domain.message.resp.model.Music;

/**
 * Message 响应 
 * @author Jason
 * @data 2014-4-24 上午10:35:30
 */
public class RespMessageUtils {

    /**
     * 组装回复图文消息对象
     * @param articles 文章
     * @param message 消息基类
     * @return RespNewsMessage
     */
    public static RespNewsMessage writeNewsMessage(List<Article> articles,Message message) {
    	RespNewsMessage newsMessage = new RespNewsMessage();
		newsMessage.setArticleCount(articles.size());
		newsMessage.setArticles(articles);
		newsMessage.setCreateTime(DateUtils.currentTime());
		newsMessage.setFromUserName(message.getToUserName());
		newsMessage.setMsgType(MessageType.RESP_MESSAGE_TYPE_NEWS.toString());
		newsMessage.setToUserName(message.getFromUserName());
        return newsMessage;
    }
    
    /**
     * 组装回复文本消息对象
     * @param content 文本
     * @param message 消息基类
     * @return RespTextMessage
     */
    public static RespTextMessage writeTextMessage(String content,Message message) {
    	RespTextMessage textMessage = new RespTextMessage();
    	textMessage.setContent(content);
    	textMessage.setCreateTime(DateUtils.currentTime());
    	textMessage.setFromUserName(message.getToUserName());
    	textMessage.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT.toString());
    	textMessage.setToUserName(message.getFromUserName());
        return textMessage;
    }
    /**
     * 组装回复音乐消息对象
     * @param music 音乐
     * @param message 消息基类
     * @return
     */
    public static RespMusicMessage writeMusicMessage(Music music,Message message) {
    	RespMusicMessage musicMessage = new RespMusicMessage();  
        musicMessage.setToUserName(message.getFromUserName());  
        musicMessage.setFromUserName(message.getToUserName());  
        musicMessage.setCreateTime(DateUtils.currentTime());  
        musicMessage.setMsgType(MessageType.RESP_MESSAGE_TYPE_MUSIC.toString());  
        musicMessage.setMusic(music);
        return musicMessage;
    }
}
