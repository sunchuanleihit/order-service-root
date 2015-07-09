package com.loukou.order.service.util;

import java.util.Calendar;
import java.util.Date;

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
	
	/**
	 * 获得小时
	 * @param time "HH:mm:ss"
	 * @return
	 */
	public static int getHour(String time) {
		String[] strs = time.split(":");
		return Integer.valueOf(strs[0]);
	}
	
	public static int getCurrentHour() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	
	public static void main(String[] args) {
		String time = "00:03:00";
		System.out.println(getHour(time));
	}


}
