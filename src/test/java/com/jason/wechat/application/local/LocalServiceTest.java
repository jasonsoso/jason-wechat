package com.jason.wechat.application.local;


import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jason.wechat.AbstractTestBase;
import com.jason.wechat.domain.message.resp.model.Article;



public class LocalServiceTest extends AbstractTestBase {

	@Autowired
	private LocalService localService;
	
	@Test
	public void testQueryLocal() throws Exception {
		
		//List<Article> list = localService.queryLocal("tanjianna","113.572273","22.286671");
		//for (Article article : list) {
		//	System.out.println("title:"+article.getTitle()+" url:"+article.getUrl());
		//}
		//Thread.sleep(60000);
		
		List<Article> list2 = localService.queryLocal("tanjianna", "ATM");
		for (Article article : list2) {
			System.out.println("-------title:"+article.getTitle()+" url:"+article.getUrl());
		}
	}
}
