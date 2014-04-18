package com.jason.wechat.application.translate;



/**
 * 在线翻译
 * @author Jason
 * @data 2014-4-18 下午03:00:06
 */
public interface TranslateService {

	/**
	 * 对关键字进行相应的翻译
	 * @param q 关键字
	 * @return String
	 */
	String translate(String q);
}