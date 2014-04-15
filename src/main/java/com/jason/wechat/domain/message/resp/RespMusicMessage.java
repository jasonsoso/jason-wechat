package com.jason.wechat.domain.message.resp;

import com.jason.wechat.domain.message.resp.model.Music;

/**
 * 回复音乐消息类
 * @author Jason
 * @data 2014-3-30 下午11:47:57
 */
public class RespMusicMessage extends RespMessage{

    /** 音乐 */
    private Music Music;

    
    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";RespMusicMessage{")
	    	.append("Music:").append(Music)
			.append("}");
	    return sb.toString();
    }
}
