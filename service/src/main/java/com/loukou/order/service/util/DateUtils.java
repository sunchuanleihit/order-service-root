package com.loukou.order.service.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class DateUtils {

	public static Date getEndofDate(Date date) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    return calendar.getTime();
	}
	
	public static Date getStartofDate(Date date) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}
	
	public static String date2DateStr(Date date) {
		DateTime dt = new DateTime(date);
		return dt.toString("yyyy-MM-dd");
	}
	
	public static String date2DateStr2(Date date) {
		DateTime dt = new DateTime(date);
		return dt.toString("yyyy-MM-dd HH:mm:ss");
	}
	

	public static String date2DateStr3(Date date) {
		DateTime dt = new DateTime(date);
		return dt.toString("yyMMddHHmm");
	}
	
	
	/**
	 * 将数据库中的php time转换成yyyy－MM-dd HH:mm:ss
	 * @param args
	 */
	public static String dateTimeToStr(int datetime) {
		DateTime dt = new DateTime((long)datetime * 1000);
		return dt.toString("yyyy-MM-dd HH:mm:ss");
	}
	
	
	/**
	 * 获得小时
	 * @param time "HH:mm:ss"
	 * @return
	 */
	public static int getHour(String time) {
		if (StringUtils.isEmpty(time)) {
			return 0;
		}
		String[] strs = time.split(":");
		return Integer.valueOf(strs[0]);
	}
	
	/**
	 * 获取当前小时
	 * @return
	 */
	public static int getCurrentHour() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	/**
	 * 时间字符串转时间
	 * @param str
	 * @return
	 */
	public static Date str2Date(String str) {
		DateTime dt = DateTime.parse(str);
		return dt.toDate();
	}

	/**
	 * 获取时间戳
	 * @return
	 */
	public static int getTime() {
		
		return (int) (new Date().getTime()/1000);
	}

	
	
	public static void main(String[] args) {
//		String time = "00:03:00";
//		System.out.println(getHour(time));
//		String str = "2015-07-13";
//		System.out.println(str2Date(str));
		System.out.println(getTime());
	}

}
