package com.jason.wechat.application.local.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.jason.framework.mapper.JsonMapper;
import com.jason.framework.util.EncodeUtils;
import com.jason.framework.util.ExceptionUtils;
import com.jason.wechat.application.local.LocalService;
import com.jason.wechat.application.local.Location;
import com.jason.wechat.domain.message.resp.model.Article;
import com.jason.wechat.infrastruture.http.HttpRequester;
import com.jason.wechat.infrastruture.http.HttpRespons;


@Service
public class LocalServiceImpl implements LocalService ,InitializingBean{
	
	private static final Logger logger = LoggerFactory.getLogger(LocalServiceImpl.class);
	
	LoadingCache<String, Location> cahceBuilder ;
	
	@Override
	public void afterPropertiesSet()  {
		// 设置缓存最大个数为200，缓存过期时间为2小时
		cahceBuilder = CacheBuilder.newBuilder().maximumSize(200)
				.expireAfterAccess(2, TimeUnit.HOURS).build(new CacheLoader<String, Location>() {
					@Override
					public Location load(String key) throws Exception {
						logger.info("fetch from .....");
						return null;
					}
				});
	}
	
	/**
	 * 根据key获取缓存
	 * @param key
	 * @return
	 */
	private Location getLocation(String key) {
		Location location = null;
		try {
			location = cahceBuilder.get(key);
		}catch(InvalidCacheLoadException e){
			throw ExceptionUtils.toUnchecked(e);
		} catch (ExecutionException e) {
			throw ExceptionUtils.toUnchecked(e);
		}
		return location;
	}
	
	/**
	 * 放数据到缓存
	 * @param key
	 * @param location
	 */
	private  void putLocation(String key,Location location) {
		cahceBuilder.put(key, location);
	}
	
	
	@Override
	public List<Article> queryLocal(String userName,String lng, String lat) {
		//缓存经纬度
		Location location = new Location(lng,lat);
		putLocation(userName, location);
		
		return queryLocalAll(lng, lat, "美食");
	}
	
	@Override
	public List<Article> queryLocal(String userName,String q) {
		Location location = getLocation(userName);
		List<Article> list = new ArrayList<Article>();
		
		if(null != location){
			putLocation(userName, location); //重新放缓存
			
			list = queryLocalAll(location.getLng(), location.getLat(), q);
		}else{
			logger.info("location is null~~~");
		}
		return list;
	}
	
	private List<Article> queryLocalAll(String lng,String lat,String q) {
		String url = "http://api.map.baidu.com/telematics/v3/local?location={lng},{lat}&keyWord={keyWord}&output=json&ak={ak}";
		String ak = "SfvEysQwx7V0V3kE60hl2x2G";
		
		url = url.replace("{lng}", lng);
		url = url.replace("{lat}", lat);
		url = url.replace("{keyWord}", EncodeUtils.urlEncode(q));
		url = url.replace("{ak}", ak);
		
		List<Article> article = new ArrayList<Article>();
		List<Article> list = null;
		HttpRespons httpRespons =  HttpRequester.sendGet(url);
		int statusCode = httpRespons.getStatusCode();
		if(HttpStatus.SC_OK == statusCode){
			String content = httpRespons.getContent();
			list = parseLocal(content,lng,lat);
		}else{
			logger.error("请求返回报错 不是200！而是"+statusCode);
		}
		//http://wx.jasonsoso.com/resources/images/sousuo.png
		//如果不为null 则添加第一个
		if(list.size()>0){
			Article first = new Article();
			first.setDescription("描述");
			first.setPicUrl("http://wx.jasonsoso.com/resources/images/sousuo.png");
			first.setTitle("周边附近搜索“"+q+"”");
			first.setUrl("http://wx.jasonsoso.com/resources/images/sousuo.png");
			article.add(first);
			article.addAll(list);
		}
		return article;
	}

	/**
	 * 解析
	 * @param json 返回的字符串
	 * @return String
	 */
	private static List<Article> parseLocal(String json,String myLng,String myLat) {
		List<Article> list = new ArrayList<Article>();
		Article article = null;
		try {
            JsonNode rootNode = JsonMapper.readTree(json);// 读取Json
            
            String status = JsonMapper.asText(rootNode, "status");
            logger.info("status:"+status);
            
            if(StringUtils.equalsIgnoreCase(status, "success")){ //如果状态不是Success，则返回状态为报错
                JsonNode pointList = JsonMapper.path(rootNode, "pointList");
            	for (int i = 0; i < pointList.size(); i++) {
                    JsonNode point = pointList.get(i); 
                    String name = JsonMapper.asText(point, "name");
                    String cityName = JsonMapper.asText(point, "cityName");
                    String address = JsonMapper.asText(point, "address");
                    String distance = JsonMapper.asText(point, "distance");
                    String district = JsonMapper.asText(point, "district");
                    String type = JsonMapper.asText(point, "type");
                    
                    JsonNode location = JsonMapper.path(point, "location");
                    String lng = JsonMapper.asText(location, "lng");
                    String lat = JsonMapper.asText(location, "lat");
                    
                    article = new Article();
                    StringBuilder sb = new StringBuilder()
                    	.append("【").append(name).append("】").append("\n")
                    	.append(address)
                    	.append(" ").append("<距离").append(distance).append("米>").append("\n");
                    	
                    article.setTitle(sb.toString());
                    article.setDescription(address);
                    //参考API：http://developer.baidu.com/map/uri-introweb.htm#idmykey37
                    //http://api.map.baidu.com/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&desti
                    //nation=大雁塔&mode=driving&region=西安&output=html&src=yourCompanyName|yourAppName
                    /*StringBuilder url = new StringBuilder()
                    	.append("http://api.map.baidu.com/direction")
                    	.append("?origin=").append(EncodeUtils.urlEncode("latlng:"+myLat+","+myLng+"|name:我的位置"))
                    	.append("&destination=").append(EncodeUtils.urlEncode("胡湾里"))
                    	.append("&mode=").append(EncodeUtils.urlEncode("walking"))
                    	.append("&region=").append(EncodeUtils.urlEncode("珠海"))
                    	.append("&output=").append(EncodeUtils.urlEncode("html"))
                    	.append("&src=").append(EncodeUtils.urlEncode("yourCompanyName|yourAppName"));
                    
                    */
	                    
	                //参考API：http://api.amap.com/uri/index
                    //http://mo.amap.com/?from=31.234527,121.287689(起点名称)&to=31.234527,121.287689(终点名称)&type=0&opt=1&dev=1
                    StringBuilder gaoDe = new StringBuilder()
                	.append("http://mo.amap.com/")
                	.append("?from=").append(EncodeUtils.urlEncode(myLat+","+myLng+"(我的位置)"))
                	.append("&to=").append(EncodeUtils.urlEncode(lat+","+lng+"("+name+")"))
                	.append("&type=1")
                	.append("&opt=2")
                	.append("&dev=0");
                    
                    article.setUrl(gaoDe.toString());
                    
                    //List只装5条数据
        			if(i < 5){
        				logger.info("parseLocal.....name:"+name+" cityName"+cityName+" address:"+address+" distance:"+distance+" district:"+district+" type:"+type
                        		+" lng:"+lng+" lat:"+lat+" url:"+gaoDe.toString());
        				list.add(article);
        			}else{
        				break; //跳出循环
        			}
            	}
            }
		} catch (Exception e) {
			logger.error("parse local is error!", e);
		}
		return list;
	}

}