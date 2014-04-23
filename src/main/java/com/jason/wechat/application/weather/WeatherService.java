package com.jason.wechat.application.weather;



/**
 * 天气预报
 * @author Jason
 * @data 2014-4-22 下午01:47:41
 */
public interface WeatherService {

	/**
	 * 天气预报
	 * @param location 输入城市名或经纬度，城市名称如:北京或者131，
	 * 	经纬度格式为lng,lat坐标如: location=116.305145,39.982368;
	 * 	全国值为all,返回省会城市自治区，
	 * 	港澳台天气情况多城市天气预报中间"|"分隔,location=116.305145,39.982368| 122.305145,36.982368|….
	 * @return
	 */
	String queryWeather(String location);
}