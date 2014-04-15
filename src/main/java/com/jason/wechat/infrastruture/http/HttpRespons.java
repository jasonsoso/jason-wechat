package com.jason.wechat.infrastruture.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import com.jason.framework.mapper.JsonMapper;

/**
 * HTTP请求返回响应
 * 
 * @author Jason
 * @data 2014-3-30 上午09:59:02
 */
public class HttpRespons {

	String url; 				//请求url
	String method; 				//请求方法： GET/POST
	Map<String, String> params;	//请求参数
	
	Header[] headers;		//返回头信息
	String contentType;		//返回内容类型
	String mimeType;		//返回mime类型
	int statusCode;			//返回的状态码
	long contentLength;		//返回内容长度
	long elapsedTime;		//请求花费的时间
	String contentCharset;	//返回内容编码

	String content; // 返回内容信息
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 根据apache HttpResponse 设置自信对象
	 * @param httpresponse
	 */
	public void parseHttpresponse(HttpResponse apacheHttpResponse) {

    	Assert.notNull(apacheHttpResponse, "apacheHttpResponse must not null");
    	
        HttpEntity entity =  apacheHttpResponse.getEntity();
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),HttpConstants.DEFAULT_CHARSET));
            
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line!=null){
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();//关闭流

            ContentType contentType = ContentType.getOrDefault(entity);
            
            this.setHeaders(apacheHttpResponse.getAllHeaders());
            this.setContentType(entity.getContentType().getValue());
            this.setMimeType(contentType.getMimeType());
            this.setStatusCode(apacheHttpResponse.getStatusLine().getStatusCode());
            this.setContentLength(entity.getContentLength());
            this.setContentCharset((contentType.getCharset()==null)?null:contentType.getCharset().toString());
            this.setContent(temp.toString());
            
            EntityUtils.consume(entity);//确保实体内容完全消耗和内容流,如果存在则关闭。
        } catch (IOException e){
           throw new RuntimeException(e);
        }
	}
	@Override
	public String toString() {
		return JsonMapper.toJsonString(this);
	}
}