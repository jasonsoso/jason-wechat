package com.jason.wechat.application.local;

import java.util.List;

import com.jason.wechat.domain.message.resp.model.Article;



/** 
 * 附近搜索
 * @author Jason
 * @data 2014-4-22 下午04:16:27
 */
public interface LocalService {

	/**
	 * 根据经纬度找附近的美食
	 * @param lng 经度
	 * @param lat 纬度
	 * @return 
	 */
	List<Article> queryLocal(String userName,String lng,String lat);
	
	/**
	 * 根据关键词 附近搜索 
	 * @param q
	 * @return
	 */
	List<Article> queryLocal(String userName,String q) ;
	
}