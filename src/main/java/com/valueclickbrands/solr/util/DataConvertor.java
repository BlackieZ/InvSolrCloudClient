package com.valueclickbrands.solr.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;


public class DataConvertor {
	public static String[] ALAIS_SUFFIXS = new String[] { ".asp", ".aspx", ".php" };
	public static String URL_SEP = "/";
	private static String HTTP_PRE = "http://";
	private static String HASHKEY_SEP = "$";
	public static final String DEFAULT_DOMAIN = "http://www.investopedia.com";
	public static final String DEFAULT_DOMAIN2 = "www.investopedia.com";
	private static String PUBLIC_HEAD_STR = "public://";

	public static void main(String[] args) {
	  String str =null;
		// System.out.println(buildURL(str)[0] + "::::::::" + buildURL(str)[1]
		// + "::::" + buildURL(str)[2]);
		//
		//	System.out.println(dealUrlStr("publidc://stock-public://analysis/2013/layoffs-and-the-stock-market-c-hpq0123.aspx"));
		/**
		String[] strings = buildURL("http://dev3.investopedia.com/markets/etfs/gld/");
		System.out.println(strings[0]);
		System.out.println(strings[1]);
		System.out.println(strings[2]);
		System.out.println(strings[3]);
		System.out.println(strings[4]);**/
		
		//System.out.println(longString2Calendar("1361639474000"));
		/**
		String url = "/articles/investing/032113/";
		System.out.println(buildURL(url)[4]);
		System.out.println(buildPath(url));;
		System.out.println(DataConvertor.buildPath("", url));
		System.out.println(buildHashKey(url));
		**/
		System.out.println(reMoveSlash(str));;
	}

	public static String dealUrlStr(String url) {
		if (StringUtils.isEmpty(url)) {
			return "";
		}
		url = url.toLowerCase().trim();
		if (url.startsWith(PUBLIC_HEAD_STR)) {
			url = HTTP_PRE + url.substring(PUBLIC_HEAD_STR.length());
		}
		if (url.startsWith(HTTP_PRE)) {
			return url;
		}
		return "";
	}

	public static boolean intString2Boolean(String value) {
		if ("1".equals(value)) {
			return true;
		}
		return false;
	}

	public static boolean booleanString2Boolean(String value) {
		if ("false".equals(value))
			return false;
		return true;
	}

	public static long longString2Long(String value) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	public static int intString2Integer(String value) {
		if (StringUtils.isEmpty(value)) {
			return 0;
		}
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	public static String longString2Calendar(String value) {
		try {
			
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            formatter.setTimeZone(TimeZone.getTimeZone("MST"));
			long lDate = longString2Long(value);
			Date date = new Date(lDate);
			return formatter.format(lDate);
			/**
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.setTimeZone(TimeZone.getTimeZone("MST"));
			return c;
			**/
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	public static String longString2Publish(String value) {
		try {
			
            SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy HH:mm:ss Z");
            formatter.setTimeZone(TimeZone.getTimeZone("MST"));
			long lDate = longString2Long(value);
			Date date = new Date(lDate);
			return formatter.format(lDate);
			/**
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.setTimeZone(TimeZone.getTimeZone("MST"));
			return c;
			**/
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * TODO 0 -> uri 1 -> sub domain
	 * 
	 * @throws MalformedURLException
	 */
	public static String[] buildURL(String url) {

		if (url != null) {
			url = url.trim();
			URL ur;
			String subdomain = null;
			try {
				if (url.startsWith(URL_SEP)) {
					url = DEFAULT_DOMAIN + url;
				}
				if (!url.startsWith(HTTP_PRE)) {
					url = HTTP_PRE + url;
					subdomain = "www";
				}
				ur = new URL(url);
				String host = ur.getHost();
				String path = ur.getPath();
				String uri = ur.getPath();
				String parameters = "";
				if (url.indexOf(path) != -1) {
					parameters = url.substring(url.indexOf(path) + path.length());
				}
				/*
				 * Deal with URI
				 */
				if (uri == null || "".equals(uri)) {
					uri = ConstantValue.DEFAULT_PAGE;
				}
				String[] infos = uri.split(URL_SEP);
				String str = null;
				for (String info : infos) {
					if (!"".equals(info)) {
						if (str == null)
							str = info;
						else
							str = str + URL_SEP + info;
					}
				}
				uri = str;

				/*
				 * Deal with subdomain
				 */
				if (subdomain == null) {
					int index = host.indexOf(".");
					if (index != -1) {
						subdomain = host.substring(0, index);
					}
				}

				if (uri == null || "".equals(uri)) {
					uri = ConstantValue.DEFAULT_PAGE;
				}
				
				boolean isPage = false;
				for(String suffix:ALAIS_SUFFIXS){
					if(uri.endsWith(suffix)){
						isPage = true;
						break;
					}
				}
				path = uri;
				if(!isPage){
					path = URL_SEP + uri + URL_SEP;
				}else{
					path = URL_SEP + uri;
				}
				return new String[] { uri, host, subdomain, parameters, path };
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

	/**
	 * TODO 0 -> uri 1 -> sub domain
	 * 
	 * @throws MalformedURLException
	 */
	public static String[] urlBuildURL(String url) {

		if (url != null) {
			url = url.trim();
			URL ur;
			String subdomain = null;
			try {
				if (url.startsWith(URL_SEP)) {
					url = DEFAULT_DOMAIN + url;
				}
				if (!url.startsWith(HTTP_PRE)) {
					url = HTTP_PRE + url;
					subdomain = "www";
				}
				ur = new URL(url);
				String host = ur.getHost();
				String uri = ur.getPath();

				/*
				 * Deal with URI
				 */
				if (uri == null) {
					uri = "";
				}
				String[] infos = uri.split(URL_SEP);
				String str = null;
				for (String info : infos) {
					if (!"".equals(info)) {
						if (str == null)
							str = info;
						else
							str = str + URL_SEP + info;
					}
				}
				uri = str;

				/*
				 * Deal with subdomain
				 */
				if (subdomain == null) {
					int index = host.indexOf(".");
					if (index != -1) {
						subdomain = host.substring(0, index);
					}
				}

				if (uri == null ) {
					uri = "";
				}

				String parameters = "";
				if (url.indexOf(uri) != -1) {
					parameters = url.substring(url.indexOf(uri) + uri.length());
				}

				return new String[] { uri, host, subdomain, parameters };
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static List<String> parserURL(String url) {
		String[] s1 = url.split(URL_SEP);
		int start = 0;
		String startStr = "";
		if (url.startsWith(URL_SEP)) {
			start = 1;
			startStr = URL_SEP;
		}
		// HashMap<Integer,HashMap<String,Integer>> map = new
		// HashMap<Integer,HashMap<String,Integer>>();
		// for(int i=1;i<s1.length;i++){
		// String t = s1[i];
		// HashMap<String,Integer> tmp = new HashMap<String,Integer>();
		// tmp.put(t, (1<<(s1.length-i)));
		// tmp.put("%", (1<<(s1.length-i-1)));
		// map.put(i, tmp);
		// }
		// HashMap<String,Integer> map2 = new HashMap<String,Integer>();
		// int max = 0;
		// for(int i=1;i<s1.length;i++){
		// max += map.get(i).get(s1[i]);
		// }
		// System.out.println(max);
		// map2.put(url, max);

		List<String> list = new ArrayList<String>();
		list.add(url);
		if (url.endsWith("/")) {
			list.add(url + "%");
		}
		int index = list.size();
		for (int i = start; i < s1.length; i++) {
			String[] tmp = new String[s1.length];
			// int num = 0;
			String ss = null;
			for (int j = start; j < tmp.length; j++) {
				if (j == i) {
					tmp[j] = "%";
				} else {
					tmp[j] = s1[j];
				}
				// num+=map.get(j).get(tmp[j]);
				if (ss == null) {
					ss = startStr + tmp[j];
				} else {
					ss = ss + URL_SEP + tmp[j];
				}
			}
			// map2.put(ss, num);
			list.add(index, ss);
		}
		return list;
	}

	// public static String[] parserURL(String url) {
	// return parseuri(url).toArray(new String[0]);
	// }
	public static ArrayList<String> parseuri(String uri) {
		ArrayList<String> list = new ArrayList<String>();
		String p = uri;
		while (true) {
			if ("*/".equals(p)) {
				list.add(p);
				return list;
			}
			list.add(p);
			p = replace(p);
		}
	}

	/**
	 * 把\后面替换成%
	 * 
	 * @return
	 */
	public static String replace(String str) {
		if (str == null)
			return null;
		if ("%".equalsIgnoreCase(str))
			return "*/";
		if (str.indexOf("/") == -1)
			return "%";
		if (str.equalsIgnoreCase("*/"))
			return null;
		int pos = str.lastIndexOf("/");
		String tmp1 = str.substring(0, pos);
		String tmp2 = str.substring(pos + 1, str.length());
		if (tmp2.equalsIgnoreCase("%")) {
			return tmp1;
		} else {
			return tmp1 + "/%";
		}
	}

	public static String buildHashKey(String alias) {

		if (alias == null)
			alias = "";

		alias = alias.trim();

		String newAlias = alias;
		 for (String suffix : ALAIS_SUFFIXS) {
			 if(alias.trim().toLowerCase().endsWith(suffix)){
				 newAlias = alias.substring(0, alias.length()-suffix.length());
			 }
//		 newAlias = alias.replace(suffix, "");
		 }
//		if (alias.lastIndexOf(".") > 0) {
//			newAlias = alias.substring(0, alias.lastIndexOf("."));
//		}

		boolean isFile = newAlias.length() != alias.length();
		if (ConstantValue.DEFAULT_PAGE.equalsIgnoreCase(newAlias.trim()) || "".equalsIgnoreCase(newAlias.trim())) {
			return HASHKEY_SEP;
		}

		String[] infos = newAlias.split(URL_SEP);
		String str = null;
		for (String info : infos) {
			if (!"".equals(info)) {
				if (str == null)
					str = info;
				else
					str = str + HASHKEY_SEP + info;
			}
		}

		return HASHKEY_SEP + str + (isFile ? "" : HASHKEY_SEP);
	}
	
	public static String urlBuildHashKey(String url) {

		if (url == null)
			url = "";

		url = url.trim();

		String newAlias = url;
		 for (String suffix : ALAIS_SUFFIXS) {
			 if(url.trim().toLowerCase().endsWith(suffix)){
				 newAlias = url.substring(0, url.length()-suffix.length());
			 }
//		 newAlias = alias.replace(suffix, "");
		 }
//		if (alias.lastIndexOf(".") > 0) {
//			newAlias = alias.substring(0, alias.lastIndexOf("."));
//		}

		boolean isFile = newAlias.length() != url.length();

		if ("".equalsIgnoreCase(newAlias.trim())) {
			return HASHKEY_SEP;
		}

		String[] infos = newAlias.split(URL_SEP);
		String str = null;
		for (String info : infos) {
			if (!"".equals(info)) { 
				if (str == null)
					str = info;
				else
					str = str + HASHKEY_SEP + info;
			}
		}

		return HASHKEY_SEP + str + (isFile ? "" : HASHKEY_SEP);
	}

	public static String parserNID(String value) {
		return value.substring(5);
	}

	public static String parserPID(String value) {
		return value.substring(8);
	}

	public static String buildPath(String domain, String uri) {
		// String result = "";
		// if(uri.lastIndexOf(".")>0){
		// result = domain + "/" + uri;
		// }else{
		// result = "/" + uri;
		// }
		String result = null;
		if (uri.lastIndexOf(".") != -1 && uri.lastIndexOf(".") > uri.lastIndexOf(URL_SEP)) {
			result = HTTP_PRE + domain + URL_SEP + uri;
		} else {
			// System.out.println(uri+"uri.endsWith(\"/\")--->"+uri.endsWith("/"));
			if (uri.endsWith(URL_SEP)) {
				result = HTTP_PRE + domain + URL_SEP + uri;
			} else {
				result = HTTP_PRE + domain + URL_SEP + uri + URL_SEP;
			}
			// result = HTTP_PRE+domain + URL_SEP + uri;
		}
		return result;
	}
	
	public static String reMoveSlash(String url) {
		if(url!=null){
			if(url.startsWith("/")){
				url = url.substring(1,url.length());
			}
			if(url.endsWith("/")){
				url = url.substring(0,url.length()-1);
			}
		}else {
			return "";
		}
		
		return url;
	}
	
	
	public static String buildPath(String uri) {
		// String result = "";
		// if(uri.lastIndexOf(".")>0){
		// result = domain + "/" + uri;
		// }else{
		// result = "/" + uri;
		// }
		String result = null;
		if (uri.lastIndexOf(".") != -1 && uri.lastIndexOf(".") > uri.lastIndexOf(URL_SEP)) {
			result =  uri;
		} else {
			// System.out.println(uri+"uri.endsWith(\"/\")--->"+uri.endsWith("/"));
			if (uri.endsWith(URL_SEP)) {
				result =   uri;
			} else {
				result =   uri + URL_SEP;
			}
			// result = HTTP_PRE+domain + URL_SEP + uri;
		}
		return result;
	}

	public static String stringToUnicode(String str) {
		if (str == null || str.trim().length() == 0) {
			return str;
		}
		StringBuffer unicodeStr = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			String hexStr = Integer.toHexString(str.charAt(i));
			unicodeStr.append("\\u");
			if (hexStr.length() == 2) {
				unicodeStr.append("00").append(hexStr);
			} else if (hexStr.length() == 1) {
				unicodeStr.append("000").append(hexStr);
			} else if (hexStr.length() == 3) {
				unicodeStr.append("0").append(hexStr);
			} else {
				unicodeStr.append(hexStr);
			}
		}
		return unicodeStr.toString();
	}
}
