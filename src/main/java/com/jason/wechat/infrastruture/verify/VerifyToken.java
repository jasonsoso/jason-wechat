package com.jason.wechat.infrastruture.verify;


import java.util.Arrays;

import com.jason.framework.util.EncryptUtils;
import com.jason.wechat.application.message.constant.Constant;

/**
 * 验证消息真实性 
 * @author Jason
 * @data 2014-3-30 下午08:10:02
 */
public class VerifyToken {



    /**
     * 验证消息真实性 
     * 效验方式与首次提交验证申请一致。 
     * @param timestamp 时间戳 
     * @param nonce 随机数 
     * @param echostr 随机字符串 
     * @param signature  微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。 
     * @return 是否真实
     */
    public static boolean checkSignature(
    		String timestamp,String nonce,String echostr,String signature){
    	// 1. 将token、timestamp、nonce三个参数进行字典序排序
        String[] tokenArray = new String[]{Constant.TOKEN, timestamp, nonce};
        Arrays.sort(tokenArray);
        
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder content = new StringBuilder();
        for(String s : tokenArray){
            content.append(s);
        }
        String shaStr = EncryptUtils.sha(content.toString());
        
        // 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if(shaStr.equals(signature)){
            return true;
        }else{
        	return  false;
        }
    }
}
