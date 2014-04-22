package com.jason.wechat.infrastruture.util;

import java.util.Date;

import org.junit.Test;

import com.jason.wechat.AbstractTestBase;

public class DateUtilsTest extends AbstractTestBase {

	@Test
	public void testBytes() throws Exception {
		Long now = DateUtils.currentTime();
		System.out.println(now);

		Date stdFormatTime = DateUtils.format2Date(now);

		System.out.println(stdFormatTime);

		String stdFormatTime2 = DateUtils.format2Str(now);
		System.out.println(stdFormatTime2);

	}

}
