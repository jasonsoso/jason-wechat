package com.jason.wechat.domain.message.req;

/**
 * 请求语音消息类
 * @author Jason
 * @data 2014-3-30 下午11:07:42
 */
public class ReqVoiceMessage extends ReqMessage {

    /** 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。  */
    private String MediaId;
    
    /** 语音格式，如amr，speex等  */
    private String Format;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";ReqVoiceMessage{")
	    	.append("MediaId:").append(MediaId).append(",")
	    	.append("Format:").append(Format)
			.append("}");
	    return sb.toString();
    }
}
