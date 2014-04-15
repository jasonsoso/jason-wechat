package com.jason.wechat.infrastruture.util;

import org.junit.Test;

import com.jason.wechat.AbstractTestBase;



public class MessageBytesTest extends AbstractTestBase {

	@Test
	public void testBytes() throws Exception {
		String str = "谭建";
		System.out.println("length:"+str.length());
		System.out.println("default length:"+str.getBytes().length);
		System.out.println("UTF-8 length:"+str.getBytes("UTF-8").length);
		System.out.println("GBK length:"+str.getBytes("GBK").length);
		System.out.println("GB2312 length:"+str.getBytes("GB2312").length);
		System.out.println("ISO8859-1 length:"+str.getBytes("ISO8859-1").length);
	}
	
}
