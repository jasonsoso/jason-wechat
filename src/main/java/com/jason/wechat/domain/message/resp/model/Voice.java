package com.jason.wechat.domain.message.resp.model;

/**
 * 声音
 * @author Jason
 * @data 2014-3-30 下午11:45:50
 */
public class Voice {

    /** 通过上传多媒体文件，得到的id */
    private String MediaId;

    
    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
    
    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
			.append("Voice{")
			.append("MediaId:").append(MediaId)
			.append("}");
		return sb.toString();
	}
}
