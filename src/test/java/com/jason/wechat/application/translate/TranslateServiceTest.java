package com.jason.wechat.application.translate;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jason.wechat.AbstractTestBase;



public class TranslateServiceTest extends AbstractTestBase {

	@Autowired
	private TranslateService translateService;
	
	@Test
	public void testTranslate() throws Exception {
		
		String str = translateService.translate("我爱你");
		System.out.println(str);
		Assert.assertEquals(str, "I love you");
		
		String str2 = translateService.translate("I love you");
		System.out.println(str2);
		Assert.assertEquals(str2, "我爱你");
	}
}
