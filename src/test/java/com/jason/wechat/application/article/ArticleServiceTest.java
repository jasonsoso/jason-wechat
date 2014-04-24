package com.jason.wechat.application.article;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jason.wechat.AbstractTestBase;
import com.jason.wechat.domain.message.resp.model.Article;



public class ArticleServiceTest extends AbstractTestBase {

	@Autowired
	private ArticleService articleService;
	@Test
	public void testQueryArticle() throws Exception {
		
		List<Article> articles = articleService.queryArticle();
		int size =  articles.size();
		System.out.println("文章的总数:"+size);
		Assert.assertTrue("文章的总数不应该大于5条！", size<= 5);
		
		for (Article article:articles) {
			System.out.println(article);
		}
	}
}
