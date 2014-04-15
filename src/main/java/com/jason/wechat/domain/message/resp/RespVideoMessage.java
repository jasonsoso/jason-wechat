package com.jason.wechat.domain.message.resp;

import com.jason.wechat.domain.message.resp.model.Video;

/**
 * 回复视频消息类
 * @author Jason
 * @data 2014-3-30 下午11:49:42
 */
public class RespVideoMessage extends RespMessage {

    /** 视频 */
    private Video Video;

    public Video getVideo() {
        return Video;
    }

    public void setVideo(Video video) {
        Video = video;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";RespVideoMessage{")
	    	.append("Video:").append(Video)
			.append("}");
	    return sb.toString();
    }
}
