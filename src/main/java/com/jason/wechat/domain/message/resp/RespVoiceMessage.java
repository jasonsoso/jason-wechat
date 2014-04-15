package com.jason.wechat.domain.message.resp;

import com.jason.wechat.domain.message.resp.model.Voice;

/**
 * 回复声音消息类
 * @author Jason
 * @data 2014-3-30 下午03:55:01
 */
public class RespVoiceMessage extends RespMessage {

    /** 声音 */
    private Voice Voice;

    
    public Voice getVoice() {
        return Voice;
    }

    public void setVoice(Voice voice) {
        Voice = voice;
    }
    
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";RespVoiceMessage{")
	    	.append("Voice:").append(Voice)
			.append("}");
	    return sb.toString();
    }
}
