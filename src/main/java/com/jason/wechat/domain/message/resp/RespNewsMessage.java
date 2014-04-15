package com.jason.wechat.domain.message.resp;

import java.util.List;

import com.jason.wechat.domain.message.resp.model.Article;

/**
 * 回复图文消息类
 * @author Jason
 * @data 2014-3-30 下午11:48:48
 */
public class RespNewsMessage extends RespMessage{

    /**  图文消息个数，限制为10条以内 */
    private int ArticleCount;
    
    /** 多条图文消息信息，默认第一个item为大图 */
    private List<Article> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<Article> getArticles() {
        return Articles;
    }

    public void setArticles(List<Article> articles) {
        Articles = articles;
    }
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString())
			.append(";RespNewsMessage{")
	    	.append("ArticleCount:").append(ArticleCount).append(",")
	    	.append("Articles:").append(Articles)
			.append("}");
	    return sb.toString();
    }
}
