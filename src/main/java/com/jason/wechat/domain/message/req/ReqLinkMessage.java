package com.jason.wechat.domain.message.req;

/**
 * 请求链接消息类
 * @author Jason
 * @data 2014-3-30 下午11:15:30
 */
public class ReqLinkMessage extends ReqMessage{

    /** 消息标题 */
    private String Title;
    
    /** 消息描述 */
    private String Description;
    
    /** 消息链接 */
    private String Url;
    

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

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";ReqLinkMessage{")
	    	.append("Title:").append(Title).append(",")
	    	.append("Description:").append(Description).append(",")
	    	.append("Url:").append(Url)
			.append("}");
	    return sb.toString();
    }
}
