
package com.jason.wechat.application.local;


import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jason.wechat.AbstractTestBase;

/**
 * 本地缓存演示，使用GuavaCache.
 * 
 */
public class GuavaCacheTest extends AbstractTestBase {


	@Test
	public void demo() throws Exception {
		// 设置缓存最大个数为100，缓存过期时间为5秒
		LoadingCache<String, String> cahceBuilder = CacheBuilder.newBuilder().maximumSize(100)
				.expireAfterAccess(5, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
					@Override
					public String load(String key) throws Exception {
						logger.info("fetch from .....");
						return "";
					}

				});

		// 初始化数据
		cahceBuilder.put("t", "tan");
		// 第一次加载会查数据库
		String str = cahceBuilder.get("t");
		System.out.println(str);

		// 第二次加载时直接从缓存里取
		String str2 = cahceBuilder.get("t");
		System.out.println(str2);
		
		// 第三次加载时，因为缓存已经过期所以会查数据库
		Thread.sleep(6000);
		
		String str3 = cahceBuilder.get("t");
		System.out.println(str3);
		Assert.assertEquals("沒有緩存應該為空", str3, "");
	}
}
