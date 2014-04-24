package com.jason.wechat.infrastruture.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 时间帮助类
 * @author Jason
 * @data 2014-4-24 上午10:33:24
 */
public class DateUtils {
	
	 /**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public final static String TIME = "";
	 
	 /**
	 * yyyy-MM-dd
	 */
	public final static String DATE = "yyyy-MM-dd";
	 /**
	 * yyyy年MM月dd日
	 */
	public final static String CN_DATE = "yyyy年MM月dd日";
	 
	/**
	 * 微信消息接口中的CreateTime表示距离1970年的秒数
	 * 而不是毫秒数，所以乘以1000，转为毫秒数
	 * 
	 * @param createTime
	 * @return
	 */
	public static String format2Str(Long createTime) {
		Date date = format2Date(createTime);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	/**
	 * 微信消息接口中的CreateTime表示距离1970年的秒数
	 * 而不是毫秒数，所以乘以1000，转为毫秒数
	 * 
	 * @param createTime
	 * @return Date
	 */
	public static Date format2Date(Long createTime) {
		long msgCreateTime = createTime * 1000L;
		return new Date(msgCreateTime);
	}
	

	/**
	 * 根据字符串格式化相应的字符串
	 * @param datestr 时间字符串
	 * @param sourceformat 原格式
	 * @param targetformat 目标格式
	 * @return String
	 * @throws Exception
	 */
	public static String formatDate2Str(String datestr, String sourceformat,
			String targetformat) throws Exception {
		Date date = DateUtils.formatDate(datestr, sourceformat);
		return DateFormatUtils.format(date, targetformat);
	}
	
	/**
	 * 根据Date 格式化为字符串
	 * @param date
	 * @param targetformat
	 * @return
	 */
	public static String formatDate2Str(Date date,String targetformat){
		return DateFormatUtils.format(date, targetformat);
	}
	/**
	 * 根据字符串格式为Date类型
	 * @param datestr
	 * @param sourceformat
	 * @return
	 * @throws Exception
	 */
	public static Date formatDate(String datestr, String sourceformat) throws Exception {
		Date date = org.apache.commons.lang.time.DateUtils.parseDate(datestr,
				new String[] { sourceformat });
		return date;
	}
	
	/**
	 * 获取星期几
	 * @param dt 
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
        	w = 0;
        }
        return weekDays[w];
    } 
	
	/**
	 * 获得当前秒数
	 * @return Long
	 */
	public static Long currentTime() {
		Date now = new Date();
		return now.getTime()/1000;
	}
	
}
