package com.jason.wechat.application.weather;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jason.wechat.AbstractTestBase;



public class WeatherServiceTest extends AbstractTestBase {

	@Autowired
	private WeatherService weatherService;
	
	@Test
	public void testWeather() throws Exception {
		
		String str = weatherService.weather("拉aaa");
		System.out.println(str);
	}
}
