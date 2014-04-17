package com.jason.wechat.application.article;

import java.util.List;

import com.jason.wechat.domain.message.resp.model.Article;


/**
 * RSS 获取杰森轻博 的 文章
 * @author Jason
 * @data 2014-4-17 下午05:03:44
 */
public interface ArticleService {

	
	/**
	 * 获取十条 杰森轻博  文章
	 * @return Article
	 */
	List<Article> queryArticle();
}