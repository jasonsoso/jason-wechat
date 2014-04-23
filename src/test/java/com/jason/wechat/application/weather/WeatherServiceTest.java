package com.jason.wechat.application.weather;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jason.wechat.AbstractTestBase;



public class WeatherServiceTest extends AbstractTestBase {

	@Autowired
	private WeatherService weatherService;
	
	@Test
	public void testWeather() throws Exception {
		
		String str = weatherService.queryWeather("珠海");
		System.out.println(str);
		
		String local= "116.305145,39.982368";
		String str2 = weatherService.queryWeather(local);
		System.out.println(str2);
	}
}
