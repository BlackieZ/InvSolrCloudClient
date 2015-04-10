package com.valueclickbrands.solr.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {
	public static String unicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

	public static String stringToUnicode(String str) {
		if(StringUtil.isNull(str)){
			return str;
		}
		StringBuffer unicodeStr = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			String hexStr = Integer.toHexString(str.charAt(i));
			unicodeStr.append("\\u");
			if (hexStr.length() == 2) {
				unicodeStr.append("00").append(hexStr);
			}else if (hexStr.length() == 1){
				unicodeStr.append("000").append(hexStr);
			}else if (hexStr.length() == 3){
				unicodeStr.append("0").append(hexStr);
			} else {
				unicodeStr.append(hexStr);
			}
		}
		return unicodeStr.toString();
	}

	public static boolean isNull(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String[] str2Array(String str, String delim) {
		String[] array = null;
		if (!isNull(str)) {
			StringTokenizer tk = new StringTokenizer(str, delim);
			array = new String[tk.countTokens()];
			int i = 0;
			while (tk.hasMoreElements()) {
				array[i] = tk.nextToken().trim();
				i++;
			}
		}
		return array;
	}

	public static List<String> str2List(String str, String delim) {
		String[] array = str2Array(str, delim);
		List<String> list = new ArrayList<String>();
		for (String s : array) {
			list.add(s);
		}
		return list;
	}

	public static String StringFilter(String str) {
		return StringFilter(str,",");
	}
	
	public static String StringFilter(String str, String delim) {
		try {
			String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			return m.replaceAll(delim).trim();
		} catch (PatternSyntaxException e) {
			return str;
		}
	}
	
	public static boolean isNumeric(String str){ 
		if(isNull(str)){
			return false;
		}
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	
	public static String CleanInvalidXmlChars(String text, String replacement) {
		if (text == null) {
			return "";
		}
	
		// String re =
		// "[^\\x09\\x0A\\x0D\\x20-\\xD7FF\\xE000-\\xFFFD\\x10000-x10FFFF]";
		// return Regex.Replace(text, re, "");
	
		// okString re =
		// "[^\\x09\\x0A\\x0D\\x20-\\xD7FF\\xE000-\\xFFFD\\x10000-x10FFFF]";
	
		// String re = "[^\\x09\\x0A\\x0D\\x20-\\xD7FF]";
	
		// preg_replace("/[\x{0e}-\x{1f}]/u",'',$body);
		String re = "[\\x0e-\\x1f]";
		text = text.replaceAll(re, replacement);
	
		re = "[\\x01-\\x02]";
		text = text.replaceAll(re, replacement);
	
		re = "[\\x07-\\x08]";
		text = text.replaceAll(re, replacement);
	
		return text;// .replaceAll("\n", replacement).replaceAll("\r",
					// replacement);
	}

	public static void main(String[] args) {
		String s = "sss,ss#ss";
//		System.out.println(unicodeToString("\u4e39\u6ce2\u61a7\u61ac\uff5eAspiration\uff5e"));
		//System.out.println(stringToUnicode("\r\n"));
		//System.out.println(unicodeToString(stringToUnicode("\r\n")));
		CleanInvalidXmlChars(s, "");
		System.out.println(s);
	}
}
