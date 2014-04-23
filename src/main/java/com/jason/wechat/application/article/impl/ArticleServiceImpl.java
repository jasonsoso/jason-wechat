package com.jason.wechat.application.article.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jason.wechat.application.article.ArticleService;
import com.jason.wechat.domain.message.resp.model.Article;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


@Service
public class ArticleServiceImpl implements ArticleService {
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

	@Override
	public List<Article> queryArticle() {
		String url = "http://tanjianna.diandian.com/rss";
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			feed = input.build(new XmlReader(new URL(url)));
		} catch (Exception e) {
			logger.error("远程连接错误 或者 解析xml错误！", e);
		}
		List<Article> list = new ArrayList<Article>(10);
		Article article = null;
		@SuppressWarnings("unchecked")
		List<SyndEntry> entries = feed.getEntries();
		int size = entries.size();
		for (int i = 0; i < size; i++) {
			SyndEntry entry = entries.get(i);
			article = new Article();
			article.setDescription(entry.getDescription().getValue());
			article.setPicUrl("http://wx.jasonsoso.com/resources/images/login.png");
			article.setTitle(entry.getTitle());
			article.setUrl(entry.getLink());
			
			//List只装5条数据
			if(i < 5){
				list.add(article);
			}else{
				break; //跳出循环
			}
		}
		return list;
	}
}