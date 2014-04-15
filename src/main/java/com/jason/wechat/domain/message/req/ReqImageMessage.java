package com.jason.wechat.domain.message.req;

/**
 * 请求图片消息类
 * @author Jason
 * @data 2014-3-30 下午11:32:10
 */
public class ReqImageMessage  extends ReqMessage{

    /**  图片链接 */
    private String PicUrl;

    /**  图片消息媒体id，可以调用多媒体文件下载接口拉取数据。  */
    private String MediaId;


    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";ReqImageMessage{")
			.append("PicUrl:").append(PicUrl).append(",")
	    	.append("MediaId:").append(MediaId)
			.append("}");
	    return sb.toString();
    }
    
}
