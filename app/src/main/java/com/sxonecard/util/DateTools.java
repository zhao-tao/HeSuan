package com.sxonecard.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.text.*;

/**
 * 
 * <p>
 * Title: 日期的操作类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Inc.
 * </p>
 * 
 * @author Ant
 * @version 1.0
 */
public class DateTools {
	/**
	 * 日期格式
	 */
	public static String yyyy_MM_dd = "yyyy-MM-dd";
	/**
	 * 日期格式
	 */
	public static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 日期格式
	 */
	public static String HH_mm_ss = "HH:mm:ss";
	/**
	 * 日期格式
	 */
	public static String yyyyMMdd = "yyyyMMdd";
	/**
	 * 日期格式
	 */
	public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	/**
	 * 日期格式 10-10-2020
	 */
	public static String MM_dd_yyyy = "MM-dd-yyyy";
	/**
	 * 日期格式 10-10-2020 18:18:10
	 */
	public static String MM_dd_yyyy_HH_mm_ss = "MM-dd-yyyy HH:mm:ss";

	/**
	 * default date value
	 */
	private static final String DEFAULT_FORMAT = "yyyy_MM_dd_HH_mm_ss";


	/**
	 * 获取当前天是当前周的第几天 星期天为1
	 *
	 * @return
	 */
	public static Integer getCurrentDayOfWeek() {
		return getCalendar().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取年
	 *
	 * @return
	 */
	public static Integer getYear() {
		return getCalendar().get(Calendar.YEAR);
	}

	/**
	 * 获取月1~12
	 *
	 * @return
	 */
	public static Integer getMonth() {
		return getCalendar().get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前日期的日
	 *
	 * @return
	 */
	public static Integer getDay() {
		return DateTools.getCalendar().get(Calendar.DATE);
	}

	/**
	 * 获取当前小时
	 *
	 * @return
	 */
	public static Integer getCurrentHour() {
		return DateTools.getCalendar().get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前分钟
	 *
	 * @return
	 */
	public static Integer getCurrentMINUTE() {
		return DateTools.getCalendar().get(Calendar.MINUTE);
	}

	/**
	 * 获取当前分钟
	 *
	 * @return
	 */
	public static Integer getCurrentSEC() {
		return DateTools.getCalendar().get(Calendar.SECOND);
	}

	public static Calendar getCalendar() {
		return Calendar.getInstance(getTimeZone(), getLocale());
	}

	/**
	 * 得到一个默认的本地语言
	 *
	 * @return
	 */
	public static Locale getLocale() {
		return Locale.getDefault();
	}

	/**
	 * 得到有一个默认的本地时区
	 *
	 * @return
	 */
	public static TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	/**
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws
	 * @Title: compareDate
	 * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	 * @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (fomatDate(s) == null || fomatDate(e) == null) {
			return false;
		}
		return fomatDate(s).getTime() >= fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 *
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date fomatDateYYYYMMddHHmmss(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getcurrent() {
		SimpleDateFormat sdfTime = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return sdfTime.format(new Date());
	}

	public static Date parseDate(String strDate) {
		return parseDate(strDate, null);
	}


	/**
	 * 日期解析
	 *
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String strDate, String pattern) {
		Date date = null;
		try {
			if (pattern == null) {
				pattern = yyyy_MM_dd_HH_mm_ss;
			}
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			date = format.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}


	/**
	 * 获取日期的年
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getYearByDate(String date) {
		Calendar c = Calendar.getInstance();
		c.setTime(parseDate(date));
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 获取日期的月
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getMonthByDate(String date) {
		Calendar c = Calendar.getInstance();
		c.setTime(parseDate(date));
		int month = c.get(Calendar.MONTH);
		if ((month + 1) < 10) {
			return "0" + (month + 1);
		} else {
			return (month + 1) + "";
		}
	}

	public static String getDayByDate(String date) {
		Calendar c = Calendar.getInstance();
		c.setTime(parseDate(date));
		int day = c.get(Calendar.DAY_OF_MONTH);
		if ((day + 1) < 10) {
			return "0" + (day + 1);
		} else {
			return (day + 1) + "";
		}
	}
	public static String getHourByDate(String date) {
		Calendar c = Calendar.getInstance();
		c.setTime(parseDate(date));
		int hour = c.get(Calendar.HOUR);
		if ((hour + 1) < 10) {
			return "0" + (hour + 1);
		} else {
			return (hour + 1) + "";
		}
	}

	public static String getMinuteByDate(String date) {
		Calendar c = Calendar.getInstance();
		c.setTime(parseDate(date));
		int minute = c.get(Calendar.MINUTE);
		if ((minute + 1) < 10) {
			return "0" + (minute + 1);
		} else {
			return (minute + 1) + "";
		}
	}

	public static String getSecondByDate(String date) {
		Calendar c = Calendar.getInstance();
		c.setTime(parseDate(date));
		int second = c.get(Calendar.SECOND);
		if ((second + 1) < 10) {
			return "0" + (second + 1);
		} else {
			return (second + 1) + "";
		}
	}
}