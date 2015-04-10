package com.valueclickbrands.solr.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DateUtil {
	public static final String DATE_FORMAT_STANDARD_DATE = "yyyy-MM-dd";
	public static final String DATE_FORMAT_STANDARD_TIME = "HH:mm:ss";
	public static final String DATE_FORMAT_STANDARD_LONGTIME = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat unixtostrFormat = new SimpleDateFormat(DATE_FORMAT_STANDARD_LONGTIME);

	private static Logger logger = Logger.getLogger(DateUtil.class);

	public static Date isDate(String date, String format) {
		if (date == null || format == null)
			return null;
		SimpleDateFormat f = new SimpleDateFormat(format);
		try {
			return f.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date parseISO8601(String date) throws ParseException {
		String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
		SimpleDateFormat f = new SimpleDateFormat(format);
		StringBuilder tmp = new StringBuilder(date);
		if (tmp.substring(date.length() - 3, date.length() - 2).equals(":")) {
			tmp.delete(date.length() - 3, date.length() - 2);
		}
		return f.parse(tmp.toString());
	}

	public static String formatISO8601(Date date) {
		String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
		SimpleDateFormat f = new SimpleDateFormat(format);
		StringBuilder tmp = new StringBuilder(f.format(date));
		tmp.insert(tmp.length() - 2, ":");
		return tmp.toString();
	}

	public static String formatDateString(String input) {
		if(StringUtils.isEmpty(input)) return null;
		try {
			DateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
			Date date = fmt.parse(input);
			String format = "yyyy-MM-dd";
			SimpleDateFormat f = new SimpleDateFormat(format);
			StringBuilder tmp = new StringBuilder(f.format(date));
			return tmp.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String format(Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}

	public static String formatISO8601(Date date, String timeZone) {
		// String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

		String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat f = new SimpleDateFormat(format);
		f.setTimeZone(TimeZone.getTimeZone(timeZone));
		StringBuilder tmp = new StringBuilder(f.format(date));
		// tmp.insert(tmp.length() - 2, ":");
		return tmp.toString();
	}

	public static String formatISO8601(Date date, String format, String timeZone, boolean hasColon) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		f.setTimeZone(TimeZone.getTimeZone(timeZone));
		StringBuilder tmp = new StringBuilder(f.format(date));
		if (hasColon) {
			tmp.insert(tmp.length() - 2, ":");
		}
		return tmp.toString();
	}

	public static String unixTimeStampToString(long unixTimeStamp) {
		return unixtostrFormat.format(new java.util.Date(unixTimeStamp * 1000));
	}

	/** Transform Calendar to ISO 8601 string. */
	public static String fromCalendar(final Calendar calendar) {
		Date date = calendar.getTime();
		String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
		return formatted.substring(0, 22) + ":" + formatted.substring(22);
	}

	/** Get current date and time formatted as ISO 8601 string. */
	public static String now() {
		return fromCalendar(GregorianCalendar.getInstance());
	}

	/** Transform ISO 8601 string to Calendar. */
	public static Calendar toCalendar(final String iso8601string) throws ParseException {
		Calendar calendar = GregorianCalendar.getInstance();
		String s = iso8601string.replace("Z", "+00:00");
		try {
			s = s.substring(0, 22) + s.substring(23);
		} catch (IndexOutOfBoundsException e) {
			throw new ParseException("Invalid length", 0);
		}
		Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
		calendar.setTime(date);
		return calendar;
	}

	// 2010-07-12T07:59:59.000+00:00
	// 2013-01-12T07:59:59Z
	// 12/20/2012
	public static long toUnixTimeStampForRss(final String iso8601string) throws ParseException {
		/*
		 * int timezone_index =iso8601string.indexOf(".000");
		 * 
		 * int hour = 0; int minutes = 0;
		 * 
		 * 
		 * if(timezone_index > 0){ hour= Integer.valueOf(iso8601string.substring(timezone_index + 5,timezone_index + 5 + 2)); minutes= Integer.valueOf(iso8601string.substring(timezone_index + 8,timezone_index + 8 + 2)); }
		 * 
		 * Calendar calendar = GregorianCalendar.getInstance(); String s = iso8601string.replaceAll("\\.000.*", ""); Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(s); calendar.setTime(date); return calendar.getTimeInMillis() - hour*60*60*1000 - minutes*60*1000;
		 */
		Date date = new SimpleDateFormat("MM/dd/yyyy").parse(iso8601string);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	// 2012-12-03T06:53:24.000+00:00
	public static Date iso8601ToDate(final String iso8601string) throws ParseException {
		String s = iso8601string.replace("+00:00", "");
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(s);
	}

	// 获取指定参数前的日期
	public static String preNumberDate(int lastDate) {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, lastDate); // 得到前n天日期
		String preNumDate = new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime());
		return preNumDate;
	}

	public static String preDate() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, -1); // 得到前一天
		String yestedayDate = new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime());
		return yestedayDate;
	}

	public static String nextDate() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, 1); // 得到前一天
		String yestedayDate = new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime());
		return yestedayDate;
	}

	public static String preYearDate() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, -1); // 得到前一天
		calendar.add(Calendar.YEAR, -1); // 得到前一天
		String yestedayDate = new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime());
		return yestedayDate;
	}

	public static String tomorrow() {
		return tomorrow(DATE_FORMAT_STANDARD_DATE);
	}

	public static String tomorrow(String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}

	public static String today(String format) {
		Date date = new Date();
		return new SimpleDateFormat(format).format(date);
	}

	public static String today() {
		return today(DATE_FORMAT_STANDARD_DATE);
	}

	private static ThreadLocal<SimpleDateFormat> dateformatMMDDYYYY = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy");
		}

	};

	public static java.sql.Date formatMMDDYYToDate(String datestr) {
		if(StringUtils.isEmpty(datestr)) return null;
		try {
			return new java.sql.Date(dateformatMMDDYYYY.get().parse(datestr).getTime());
		} catch (Exception ex) {
			logger.error("formatMMDDYYToDate error,datestr=" + datestr);
		}
		return null;
	}

	public static String formatMMDDYYToString(java.sql.Date date) {
		try {
			return dateformatMMDDYYYY.get().format(date);
		} catch (Exception ex) {
			logger.error("formatMMDDYYToString error,date=" + date);
		}
		return "";
	}

	public static String currentTimeStampToString(long unixTimeStamp) {
		return unixtostrFormat.format(new java.util.Date(unixTimeStamp));
	}
	
	public static void main(String[] args) throws ParseException {
		// String jtdate = "12/20/2012";
		// //String jtdate = "2012-12-20";
		// System.out.println((jtdate));
		// System.out.println(toUnixTimeStampForRss(jtdate));

		// System.out.println("YYYYY:"+preNumberDate(-7));
		// System.out.println("KKKKK:"+formatISO8601( new Date(),"UTC"));

		// System.out.println("KKKKK:"+formatDateString("05/12/2013"));
		//System.out.println(formatMMDDYYToDate("3/1/2013"));
		//System.out.println(formatMMDDYYToString(formatMMDDYYToDate("3/1/2013")));
		
		//System.out.println(currentTimeStampToString(System.currentTimeMillis()));
		System.out.println(today("M/d/yyyy"));
	}

}
