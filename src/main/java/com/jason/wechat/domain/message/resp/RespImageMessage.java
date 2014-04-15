package com.jason.wechat.domain.message.resp;

import com.jason.wechat.domain.message.resp.model.Image;

/**
 * 回复图片消息类
 * @author Jason
 * @data 2014-3-30 下午11:46:33
 */
public class RespImageMessage extends RespMessage {

    /** 图片  */
    private Image Image;

    public Image getImage() {
        return Image;
    }

    public void setImage(Image image) {
        Image = image;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";RespImageMessage{")
	    	.append("Image:").append(Image)
			.append("}");
	    return sb.toString();
    }
}
