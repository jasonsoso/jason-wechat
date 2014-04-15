package com.jason.wechat.domain.message.req;

/**
 * 请求视频消息类
 * @author Jason
 * @data 2014-3-30 下午11:10:35
 */
public class ReqVideoMessage extends ReqMessage {

    /** 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。  */
    private String MediaId;
    
    /** 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。  */
    private String ThumbMediaId;

    
    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        ThumbMediaId = thumbMediaId;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";ReqVoiceMessage{")
	    	.append("MediaId:").append(MediaId).append(",")
	    	.append("ThumbMediaId:").append(ThumbMediaId)
			.append("}");
	    return sb.toString();
    }
}
