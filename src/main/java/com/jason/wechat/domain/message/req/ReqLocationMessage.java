package com.jason.wechat.domain.message.req;

/**
 * 请求地理位置消息类
 * @author Jason
 * @data 2014-3-30 下午11:12:22
 */
public class ReqLocationMessage extends ReqMessage{
    /**  地理位置维度 */
    private String Location_X;
    
    /** 地理位置经度 */
    private String Location_Y;
    
    /** 地图缩放大小 */
    private String Scale;

    /** 地理位置信息 */
    private String Label;

    
    public String getLocation_X() {
        return Location_X;
    }

    public void setLocation_X(String location_X) {
        Location_X = location_X;
    }

    public String getLocation_Y() {
        return Location_Y;
    }

    public void setLocation_Y(String location_Y) {
        Location_Y = location_Y;
    }

    public String getScale() {
        return Scale;
    }

    public void setScale(String scale) {
        Scale = scale;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";ReqLocationMessage{")
	    	.append("Location_X:").append(Location_X).append(",")
	    	.append("Location_Y:").append(Location_Y).append(",")
	    	.append("Scale:").append(Scale).append(",")
	    	.append("Label:").append(Label)
			.append("}");
	    return sb.toString();
    }

}
