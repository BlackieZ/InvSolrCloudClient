package com.valueclickbrands.framework.util;

import java.security.NoSuchAlgorithmException;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class CacheUtil  {
	public static final String CACHE_KEY_NODE_LIST = "nodelist";
	public static final String CACHE_KEY_NODE_MAP = "nodemap";
	public static final String CACHE_KEY_URL_MAP = "urlmap";
	public static final String CACHE_KEY_HIGHLIGHTS_MAP = "Highlightsmap";
	public static final String CACHE_KEY_TAG_MAP = "tagmap";
	public static final String CACHE_KEY_CHANNEL_LIST = "channellist";
	public static final String CACHE_KEY_CHANNELMAPPING_LIST = "channellChannelMappingMap";
	public static final String CACHE_KEY_TAGMAPPING_LIST = "TagMappingList";
	public static final String CACHE_KEY_TICKERMAPPING_LIST = "TickerMappingList";
	
	private GeneralCacheAdministrator cache;
	
	public Object getObjectForCache(String key) {
		try {
			return cache.getFromCache(md5Key(key));
		} catch (NeedsRefreshException e) {
			cache.cancelUpdate(md5Key(key));
			return null;
		}
	}
	
	public void setCache(GeneralCacheAdministrator cache) {
		this.cache = cache;
	}

	public void putObjectForCache(String key, Object obj) {
		if(obj!=null){
			cache.putInCache(md5Key(key), obj);
		}
	}
	
	public void remove(String key){
		cache.removeEntry(md5Key(key));
	}
	
	public void removeAll(){
		cache.flushAll();
	}
	
	private String md5Key(String key){
		try {
			return SecurityUtils.strMD5(key);
		} catch (NoSuchAlgorithmException e) {
			return key;
		}
	}
}
