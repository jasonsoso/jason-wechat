package com.jason.wechat.application.transit.impl;

import org.apache.http.HttpStatus;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jason.framework.mapper.JsonMapper;
import com.jason.framework.util.EncodeUtils;
import com.jason.wechat.application.transit.TransitService;
import com.jason.wechat.infrastruture.http.HttpRequester;
import com.jason.wechat.infrastruture.http.HttpRespons;


@Service
public class TransitServiceImpl implements TransitService {
	
	private static final Logger logger = LoggerFactory.getLogger(TransitServiceImpl.class);


	@Override
	public String queryTransit(String city, String busId) {
		String url = "http://openapi.aibang.com/bus/lines?app_key={appKey}&city={city}&q={q}&alt=json";
		String apiKey = "0ddc1fa1af448eab5377ceb2beefd6cc";
			
		url = url.replace("{appKey}", apiKey);
		url = url.replace("{city}", EncodeUtils.urlEncode(city));
		url = url.replace("{q}", busId);
		
		String s = null;
		HttpRespons httpRespons =  HttpRequester.sendGet(url);
		int statusCode = httpRespons.getStatusCode();
		if(HttpStatus.SC_OK == statusCode){
			String content = httpRespons.getContent();
			s = parseTranslateForLine(content);
		}else{
			logger.error("请求返回报错 不是200！而是"+statusCode);
		}
		return s;
	}

	@Override
	public String queryTransit(String city, String from, String to) {
		String url = "http://openapi.aibang.com/bus/transfer?app_key={appKey}&city={city}&start_addr={startAddr}&end_addr={endAddr}&alt=json";
		String apiKey = "0ddc1fa1af448eab5377ceb2beefd6cc";
		String startAddr = EncodeUtils.urlEncode(from);
		String endAddr = EncodeUtils.urlEncode(to);
			
		url = url.replace("{appKey}", apiKey);
		url = url.replace("{city}", EncodeUtils.urlEncode(city));
		url = url.replace("{startAddr}", startAddr);
		url = url.replace("{endAddr}", endAddr);
		
		String s = null;
		HttpRespons httpRespons =  HttpRequester.sendGet(url);
		int statusCode = httpRespons.getStatusCode();
		if(HttpStatus.SC_OK == statusCode){
			String content = httpRespons.getContent();
			s = parseTranslate(content);
		}else{
			logger.error("请求返回报错 不是200！而是"+statusCode);
		}
		return s;
	}
	
	private static String parseTranslateForLine(String json) {
		String str = "暂时木有结果";
		int count = 2;	//只获取前二条方案
		try {
            JsonNode rootNode = JsonMapper.readTree(json);// 读取Json
            
            JsonNode lines = JsonMapper.path(rootNode, "lines");
            JsonNode line = JsonMapper.path(lines, "line");
            StringBuilder scheme = new StringBuilder();

            int isize = line.size();
            for (int i = 0; i < isize; i++) {
            	JsonNode results = line.get(i);
            	
            	String name = JsonMapper.asText(results, "name");
            	String stats = JsonMapper.asText(results, "stats");
            	
            	if(i < count){
            		scheme.append("【").append(name).append("】").append("\n");
            		scheme.append("沿途站点：").append(stats.replaceAll(";", "→").replaceAll("；", "→"));
            		if((count-1) != i){
            			scheme.append("\n\n");
                	}
            	}else{
    				break; //跳出循环
    			}
			}
            str = scheme.toString();
		} catch (Exception e) {
			logger.error("parse translate is error!", e);
		}
		return str;
	}

	/**
	 * 解析
	 * @param json 返回的字符串
	 * @return String
	 */
	private static String parseTranslate(String json) {
		String str = "暂时木有结果";
		int count = 3;	//只获取前三条方案
		try {
            JsonNode rootNode = JsonMapper.readTree(json);// 读取Json
            //String resultNum = JsonMapper.asText(rootNode, "result_num");
            
            JsonNode buses = JsonMapper.path(rootNode, "buses");
            JsonNode bus = JsonMapper.path(buses, "bus");
            StringBuilder scheme = new StringBuilder();
            //方案
            int isize = bus.size();
            for (int i = 0; i < isize; i++) {
            	JsonNode results = bus.get(i);
            	//String dist = JsonMapper.asText(results, "dist");	//距离
            	
            	JsonNode segments = JsonMapper.path(results, "segments");
            	JsonNode segment = JsonMapper.path(segments, "segment");
            	
            	StringBuilder segSB = new StringBuilder();
            	//方案里面的步骤
            	int jsize = segment.size();
            	for (int j = 0; j < jsize;  j++) {
                	JsonNode s = segment.get(j);
                	String startStat = JsonMapper.asText(s, "start_stat");	//上车站点
                	String endStat = JsonMapper.asText(s, "end_stat");	//下车站点
                	String lineName = JsonMapper.asText(s, "line_name");	//路线名称
                	String stats = JsonMapper.asText(s, "stats");	//经途站点
                	
                	segSB.append("在“"+startStat+"”站乘").append("【").append(lineName).append("】").append("到“"+endStat+"”站下车,");
                	segSB.append("途径站点：").append(stats.replaceAll(";", "→").replaceAll("；", "→"));
                	if((jsize-1) != j){
                		segSB.append("\n");
                	}
            	}
            	if(i < count){
            		scheme.append("方案").append(i+1).append(":");
            		scheme.append(segSB);
            		if((count-1) != i){
            			scheme.append("\n\n");
                	}
            		
            	}else{
    				break; //跳出循环
    			}
			}
            str = scheme.toString();
		} catch (Exception e) {
			logger.error("parse translate is error!", e);
		}
		return str;
	}

}