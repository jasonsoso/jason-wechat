package com.jason.wechat.infrastruture.http;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.jason.wechat.AbstractTestBase;



public class HttpRequesterTest extends AbstractTestBase {

	@Test
	public void testSendGet() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
        map.put("q","喜爱夜蒲");
        
		HttpRespons httpRespons =  HttpRequester.sendGet("http://api.douban.com/v2/movie/search", map);
        
		System.out.println("-------testSendGet----"+httpRespons.getContent());
        
        Assert.assertEquals(httpRespons.getStatusCode(), 200);
	}
	
}
