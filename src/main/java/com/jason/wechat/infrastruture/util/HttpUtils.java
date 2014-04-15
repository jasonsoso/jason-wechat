package com.jason.wechat.infrastruture.util;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP 辅助类
 * @author Jason
 * @data 2014-3-31 下午08:22:01
 */
public class HttpUtils {
	
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    
    /**
     * 读取流,输入字符串
     * @param in 流
     * @param charsetName 编码
     * @return
     */
    public static final String inputStream2String(InputStream in,String charsetName){
    	if(in == null){
			return "";
		}
		StringBuffer out = new StringBuffer();
		try {
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n, "UTF-8"));
			}
		} catch (Exception e) {
			logger.error("读取流错误！",e);
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				logger.error("关闭流错误！",e);
			}
		}
		return out.toString();
    }
    /**
     * 读取流,输入字符串
     * @param in 流
     * @return
     * @throws IOException
     */
    public static final String inputStream2String(InputStream in){
		return inputStream2String(in, "UTF-8");
	}
}
