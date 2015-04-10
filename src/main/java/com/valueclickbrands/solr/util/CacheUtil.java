package com.valueclickbrands.solr.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.valueclickbrands.solr.model.AutoImage;
import com.valueclickbrands.solr.model.Node;
import com.valueclickbrands.solr.model.Tag;
import com.valueclickbrands.solr.model.TagGroup;
import com.valueclickbrands.solr.model.WebToolSettings;


public class CacheUtil {	
	protected static Logger logger = Logger.getLogger(CacheUtil.class);
	public static Map<String,List<Tag>> tagMap = new HashMap<String, List<Tag>>();
	public static Map<String,String> aliasMap = new HashMap<String, String>();
	public static Map<Long,List<TagGroup>> tagGroupMap = new HashMap<Long,List<TagGroup>>();
	public static Map<String,HashSet<Long>> tagGroupName_tagMap = new HashMap<String,HashSet<Long>>();
	public static Map<String,Long> tagv2Name_tagMap = new HashMap<String,Long>();

	public static Map<String,Node> nodeMap = new HashMap<String, Node>();
	public static Map<String,List<String>> tickerNodeMap = new HashMap<String, List<String>>();
	
	public static Map<String,WebToolSettings> webToolSettingsMap = new HashMap<String, WebToolSettings>();
	
	public static Map<String,List<AutoImage>> autoImageMap = new HashMap<String, List<AutoImage>>();
	
	
	
	
}
