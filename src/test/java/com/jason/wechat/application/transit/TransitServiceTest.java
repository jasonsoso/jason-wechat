package com.jason.wechat.application.transit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jason.wechat.AbstractTestBase;

public class TransitServiceTest extends AbstractTestBase {

	@Autowired
	private TransitService transitService;

	@Test
	public void testQueryTransit() throws Exception {

		String str1 = transitService.queryTransit("珠海", "凤凰北", "湖心路口");
		System.out.println(str1);
		
		String str2 = transitService.queryTransit("珠海", "2路");
		System.out.println(str2);
		
		String str = "珠海，2路";  
		String[] strBuf = str.split("，|,");  
		for (int i = 0; i < strBuf.length; i++) {  
			String string = strBuf[i];  
			System.out.println(string);  
		}  
		
	}
}
