package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerUtils {

	// 获取当前时间的字符串
	public static String getCurrentTime() {
		String time = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date().getTime());
		return time;
	}

}
