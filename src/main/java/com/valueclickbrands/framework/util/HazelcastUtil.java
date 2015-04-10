package com.valueclickbrands.framework.util;

import java.util.List;
import java.util.Map;

import com.hazelcast.core.HazelcastInstance;

public class HazelcastUtil  {
	public static final String CACHE_KEY_NODE_LIST = "nodelist";
	public static final String CACHE_KEY_NODE_MAP = "nodemap";
	public static final String CACHE_KEY_URL_MAP = "urlmap";
	public static final String CACHE_KEY_HIGHLIGHTS_MAP = "Highlightsmap";
	public static final String CACHE_KEY_TAG_MAP = "tagmap";
	public static final String CACHE_KEY_CHANNEL_LIST = "channellist";
	
	public static final int CACHE_TYPE_LIST = 0;
	public static final int CACHE_TYPE_MAP = 1;
	public static final int CACHE_TYPE_OBJECT = 2;
	
	private HazelcastInstance cache;
	
	public Map getMapForCache(String key){
		return cache.getMap(key);
	}
	
	public List getListForCache(String key) {
		return cache.getList(key);
	}
	
	public void setCache(HazelcastInstance cache) {
		this.cache = cache;
	}
}
