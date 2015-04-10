package com.valueclickbrands.solr.util;

public class Snippet {
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
}

/** 
* @author Vanson Zou
* @date Dec 19, 2014 
*/ 

