package com.jason.wechat.domain.message.resp.model;

/**
 * 视频
 * @author Jason
 * @data 2014-3-30 下午03:53:49
 */
public class Video {

    /**  通过上传多媒体文件，得到的id */
    private String MediaId;

    /** 视频消息的标题 */
    private String Title;

    /** 视频消息的描述 */
    private String Description;

    
    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
    
    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
			.append("Video{")
			.append("MediaId:").append(MediaId).append(",")
	    	.append("Title:").append(Title).append(",")
	    	.append("Description:").append(Description)
			.append("}");
		return sb.toString();
	}
}
