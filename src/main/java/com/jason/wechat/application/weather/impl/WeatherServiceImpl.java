package com.jason.wechat.application.weather.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jason.framework.mapper.JsonMapper;
import com.jason.framework.util.EncodeUtils;
import com.jason.wechat.application.weather.WeatherService;
import com.jason.wechat.infrastruture.http.HttpRequester;
import com.jason.wechat.infrastruture.http.HttpRespons;
import com.jason.wechat.infrastruture.util.DateUtils;


@Service
public class WeatherServiceImpl implements WeatherService {
	
	private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

	@Override
	public String queryWeather(String location) {
		String url = "http://api.map.baidu.com/telematics/v3/weather?location={location}&output=json&ak={ak}";
		String ak = "SfvEysQwx7V0V3kE60hl2x2G";
		
		url = url.replace("{location}", EncodeUtils.urlEncode(location));
		url = url.replace("{ak}", ak);
		
		String s = null;
		HttpRespons httpRespons =  HttpRequester.sendGet(url);
		int statusCode = httpRespons.getStatusCode();
		if(HttpStatus.SC_OK == statusCode){
			String content = httpRespons.getContent();
			s = parseWeather(content);
		}else{
			logger.error("请求返回报错 不是200！而是"+statusCode);
		}
		return s;
	}

	/**
	 * 解析
	 * @param json 返回的字符串
	 * @return String
	 */
	private static String parseWeather(String json) {
		String str = "暂时木有结果";
		try {
            JsonNode rootNode = JsonMapper.readTree(json);// 读取Json
            
            String error = JsonMapper.asText(rootNode, "error");
            String nowDate = JsonMapper.asText(rootNode, "date");
            logger.info("error:"+error+" date:"+nowDate);
            
            if(StringUtils.equalsIgnoreCase(error, "0")){ //如果状态不是0，则返回状态为报错
                StringBuilder sb = new StringBuilder();
            	
                JsonNode results = JsonMapper.path(rootNode, "results");
                JsonNode result = results.get(0);
                if(null != result){
                	String currentCity = JsonMapper.asText(result, "currentCity");
                	logger.info("currentCity:"+currentCity);

                	sb.append("【").append(currentCity).append("】").append("\n");
                	Date  dateDate = DateUtils.formatDate(nowDate, DateUtils.DATE);
                	String dateStr = DateUtils.formatDate2Str(dateDate, DateUtils.CN_DATE);
                	String weekStr = DateUtils.getWeekOfDate(dateDate);
                	
                	sb.append(dateStr).append(" ").append(weekStr).append("\n");
                	
                	JsonNode weatherData = JsonMapper.path(result, "weather_data");
                	for (int i = 0; i < weatherData.size(); i++) {   
                        JsonNode childNode = weatherData.get(i); 
                        String date = JsonMapper.asText(childNode, "date");
                        String dayPictureUrl = JsonMapper.asText(childNode, "dayPictureUrl");
                        String nightPictureUrl = JsonMapper.asText(childNode, "nightPictureUrl");
                        String weather = JsonMapper.asText(childNode, "weather");
                        String wind = JsonMapper.asText(childNode, "wind");
                        String temperature = JsonMapper.asText(childNode, "temperature");
                        temperature = temperature.replaceAll(" ","");
                        
                        sb.append(date).append(":").append(temperature).append(",").append(weather).append(" ").append(wind).append("\n");
                        
                        logger.info("parseWeather.....date:"+date+" dayPictureUrl"+dayPictureUrl+" nightPictureUrl:"+nightPictureUrl+" weather:"+weather+" wind:"+wind+" temperature:"+temperature);
                	}
                	str = sb.toString();
	            }else{
	            	str = "木有资料";
	            }
            }
		} catch (Exception e) {
			logger.error("parse weather is error!", e);
		}
		return str;
	}
}