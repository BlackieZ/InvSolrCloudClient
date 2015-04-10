package com.valueclickbrands.solr;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.valueclickbrands.solr.model.TaskEntity;
import com.valueclickbrands.solr.util.PHPSerializer;

/** 
 * @author Vanson Zou
 * @date Jan 8, 2015 
 */

public class TextMain {
	
public static void main(String[] args) throws IllegalAccessException {
	//String a = "/sssss/1234";
	//System.out.println(a.substring(1,a.length()));
//	String aString = "@#aA sffsfAAAAF   bcdefghi";
	//;
	//System.out.println(Integer.parseInt(aString, 10));
	/**
	int length = 0;
	if(aString !=null ){
		length = aString.length()>8?8:aString.length();
	}
	
	byte[] bytes = aString.substring(0, length).getBytes();
	
	
      for(int i=bytes.length-1;i>=0;i--){
          bytes[i]-=(byte)'0';
      }
      System.out.println(bytes.length);
      for(int i=0;i<bytes.length;i++){
          System.out.print(bytes[i]+",");
      }
      System.out.println();

	String r = convert(aString);
	System.out.println(r);
	t2(aString);
	
	//String aaString = aString.replaceAll("\\s*", "").toLowerCase();
	//System.out.println(aaString);
	String data = "a:1:{s:6:\"robots\";a:1:{s:5:\"value\";a:1:{s:7:\"noindex\";s:7:\"noindex\";}}}";
	String data2 = "a:2:{s:5:\"title\";a:1:{s:5:\"value\";s:22:\"Return On Equity (ROE)\";}s:6:\"robots\";a:0:{}}";
	String data3 = "a:1:{s:5:\"title\";a:1:{s:5:\"value\";s:36:\"[invpage:title] Videos | [site:name]\";}}";
	
	String data4 = "a:2:{s:11:\"description\";a:1:{s:5:\"value\";s:91:\"Let Investopedia help you find all the information and resources you need about investing. \";}s:6:\"robots\";a:1:{s:5:\"value\";a:5:{s:7:\"noindex\";s:7:\"noindex\";s:8:\"nofollow\";i:0;s:9:\"noarchive\";i:0;s:9:\"nosnippet\";i:0;s:5:\"noodp\";i:0;}}}";
	HashMap map = (HashMap)PHPSerializer.unserialize(data4.getBytes());
	HashMap robots = (HashMap)map.get("description");
	if(robots!=null){
		String value2 = (String) robots.get("value");
		System.out.println(value2);
//		
		}
	
	
	TreeMap<Integer,TreeMap<Integer, Integer>> tMap = new TreeMap<Integer, TreeMap<Integer,Integer>>();
	tMap.put(1, null);
	tMap.put(2, null);
	tMap.put(7, null);
	tMap.put(3, null);
	
	for(Integer ke:tMap.keySet()){
		System.out.println(ke);
	}
	**/
	
	Gson gson = new Gson();
	String data = "{\"data_type\":\"node\",\"action\":\"update\",\"nid\":19828,\"vid\":19828,\"date\":1419323440572, \"tree_action\":\"fully\", \"branch_nid\":19927}";
	TaskEntity taskEntity = gson.fromJson(data, TaskEntity.class);
	System.out.println(taskEntity.isIstree());
	System.out.println(taskEntity.getTree_action());
	System.out.println(taskEntity.getBranch_nid());

}
	
	
public static String convert(String str) 
{ 
str = (str == null ? "" : str); 
String tmp; 
StringBuffer sb = new StringBuffer(1000); 
char c; 
int i, j; 
sb.setLength(0); 
for (i = 0; i < str.length(); i++) 
{ 
c = str.charAt(i); 
sb.append("\\u"); 
j = (c >>>8); //取出高8位 
tmp = Integer.toHexString(j); 
if (tmp.length() == 1) 
sb.append("0"); 
sb.append(tmp); 
j = (c & 0xFF); //取出低8位 
tmp = Integer.toHexString(j); 
if (tmp.length() == 1) 
sb.append("0"); 
sb.append(tmp); 

} 
return (new String(sb)); 
} 
public static long unsigned4BytesToInt(byte[] buf, int pos) {  
    int firstByte = 0;  
    int secondByte = 0;  
    int thirdByte = 0;  
    int fourthByte = 0;  
    int index = pos;  
    firstByte = (0x000000FF & ((int) buf[index]));  
    secondByte = (0x000000FF & ((int) buf[index + 1]));  
    thirdByte = (0x000000FF & ((int) buf[index + 2]));  
    fourthByte = (0x000000FF & ((int) buf[index + 3]));  
    index = index + 4;  
    return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;  
}  

public static void t2(String s){//字符串转换为ASCII码

	  char[]chars=s.toCharArray(); //把字符中转换为字符数组 
	  System.out.println("\n\n汉字 ASCII\n----------------------");
	  for(int i=0;i<chars.length;i++){//输出结果
	         System.out.println(" "+chars[i]+" "+(int)chars[i]);
	        }
	 }

}
