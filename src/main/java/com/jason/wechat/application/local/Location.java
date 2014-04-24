package com.jason.wechat.application.local;

/**
 * 位置
 * @author Jason
 * @data 2014-4-24 下午12:01:58
 */
public class Location {
	
	private String lng;	//经度
	private String lat;	//纬度
	
	
	
	public Location(){
	}
	public Location(String lng,String lat){
		this.lng = lng;
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
		.append("Location{")
		.append("lng:").append(lng).append(",")
		.append("lat:").append(lat)
		.append("}");
		return sb.toString();
	}
}
