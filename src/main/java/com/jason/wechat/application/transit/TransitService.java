package com.jason.wechat.application.transit;



/**
 * 公交查询
 * @author Jason
 * @data 2014-4-24 下午04:06:38
 */
public interface TransitService {

	String queryTransit(String city,String busId);
	
	String queryTransit(String city,String from,String to);
}