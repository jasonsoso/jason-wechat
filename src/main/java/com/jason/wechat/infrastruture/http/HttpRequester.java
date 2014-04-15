package com.jason.wechat.infrastruture.http;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP 请求客户端
 * @author Jason
 * @data 2014-3-30 上午09:47:18
 */
public class HttpRequester {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequester.class);
    
    /**
     * httpClient 4.2 客户端
     */
    private static HttpClient httpClient = new DefaultHttpClient();
    
    static{
    	httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);//连接时间
    	httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);//数据传输时间
    	httpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
    }

    /**
     * GET请求
     * @param url 路径
     * @param params 参数
     * @return HttpRespons
     * @throws Exception
     */
    public static  HttpRespons sendGet(String url, Map<String, String> params){
       return sendGet(url, params, HttpConstants.DEFAULT_CHARSET);
    }
    
    /**
     * GET请求
     * @param url 路径
     * @param params 参数
     * @param charset 编码
     * @return
     */
    public static  HttpRespons sendGet(String url, Map<String, String> params,String charset){
        return send(url, "GET", params,charset);
    }
    /**
     * POST请求
     * @param url 路径
     * @param params 参数
     * @return HttpRespons
     * @throws Exception
     */
    public static  HttpRespons sendPost(String url, Map<String, String> params) {
        return sendPost(url, params, HttpConstants.DEFAULT_CHARSET);
    }
    /**
     * POST请求
     * @param url 路径
     * @param params 参数
     * @param charset 编码
     * @return
     */
    public static  HttpRespons sendPost(String url, Map<String, String> params,String charset) {
        return send(url, "POST", params,charset);
    }
	public static HttpRespons send(String urlString, String method,
			Map<String, String> parameters, String charset) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (parameters != null) {
			for (String key : parameters.keySet()) {
				params.add(new BasicNameValuePair(key, parameters.get(key)));
			}
		}
		HttpResponse httpresponse = null;//apache http Response
		HttpRespons httpRespons = null;	//jason http Respons
		long elapsed = 0;

		if ("GET".equals(method)) {
			HttpGet httpGet = new HttpGet(urlString);
			try {
				
				if(params.size()>0){ //有参数才拼装URL
					String str = EntityUtils.toString(new UrlEncodedFormEntity(params, charset));
					String url = httpGet.getURI().toString();
					if(StringUtils.indexOf(url, "?")>0){//如果原来url有?,则不用加"?"，否则需要添加"?"进行拼装url
						httpGet.setURI(new URI( url + str));
					}else{
						httpGet.setURI(new URI( url + "?" + str));
					}
				}

				httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
				httpGet.setHeader("Connection", "keep-alive");
				httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");
				httpGet.setHeader("Accept-Charset", charset);
				httpGet.setHeader("ContentType", charset);
				
				
				// 发送请求
				long startTime = System.currentTimeMillis();
				httpresponse = httpClient.execute(httpGet);
				elapsed = System.currentTimeMillis() - startTime;

				logger.info("-----------发送get请求-------------------");
				logger.info("花费{}毫秒,发送get请求：{}", elapsed, httpGet.getURI().toString());
				
				httpRespons = new HttpRespons();
				httpRespons.setUrl(httpGet.getURI().toString());
				httpRespons.setMethod(method);
				httpRespons.setParams(parameters);
				httpRespons.setElapsedTime(elapsed);
				httpRespons.parseHttpresponse(httpresponse);//組裝httpRespons信息 提供返回
				
				logger.info("请求结果{}",httpRespons.toString());
	            logger.info("--------------------------------------------------------------------");
	            
			} catch (Exception e) {
				logger.error(String.format("GET请求%s报错！", httpGet.getURI().toString()),e);
			} finally {
				httpGet.releaseConnection();
			}
		} else {
			// Post请求
			HttpPost httpPost = new HttpPost(urlString);
			try {
				httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
				httpPost.setHeader("Connection", "keep-alive");
				httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36");
				httpPost.setHeader("Accept-Charset", charset);
				httpPost.setHeader("ContentType", charset);
				// 设置参数
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				// 发送请求
				long startTime = System.currentTimeMillis();
				httpresponse = httpClient.execute(httpPost);
				elapsed = System.currentTimeMillis() - startTime;

				logger.info("------------发送post请求-------------------");
				logger.info("花费{}毫秒,发送post请求{}", elapsed, httpPost.getURI().toString());
				
				httpRespons = new HttpRespons();
				httpRespons.setUrl(httpPost.getURI().toString());
				httpRespons.setMethod(method);
				httpRespons.setParams(parameters);
				httpRespons.setElapsedTime(elapsed);
				httpRespons.parseHttpresponse(httpresponse);//組裝httpRespons信息 提供返回
				
				logger.info("请求结果{}",httpRespons.toString());
	            logger.info("--------------------------------------------------------------------");
	            
			} catch (Exception e) {
				logger.error(String.format("Post请求%s报错！", httpPost.getURI().toString()), e);
			} finally {
				httpPost.releaseConnection();
			}
		}
		return httpRespons;
	}

}
