package com.valueclickbrands.solr.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.valueclickbrands.solr.dao.DumpDao;
import com.valueclickbrands.solr.model.AutoImage;
import com.valueclickbrands.solr.model.AutoRepublish;
import com.valueclickbrands.solr.model.Node;
import com.valueclickbrands.solr.model.PartnerLinks;
import com.valueclickbrands.solr.model.Related;
import com.valueclickbrands.solr.model.SynDate;
import com.valueclickbrands.solr.model.Tag;
import com.valueclickbrands.solr.model.TagGroup;
import com.valueclickbrands.solr.model.TreeNode;
import com.valueclickbrands.solr.model.UrlAlias;
import com.valueclickbrands.solr.util.CacheUtil;
import com.valueclickbrands.solr.util.Configure;
import com.valueclickbrands.solr.util.DataConvertor;
import com.valueclickbrands.solr.util.PHPSerializer;
import com.valueclickbrands.solr.util.StringUtil;


/** 
 * @author Vanson Zou
 * @date Dec 18, 2014 
 */

public class DumpService{
	private static Logger logger = Logger.getLogger(DumpService.class);
	public static String[] ALAIS_SUFFIXS = new String[] { ".asp", ".aspx", ".php" };
	public static String URL_SEP = "/";
	private static String HTTP_PRE = "http://";
	private static String HASHKEY_SEP = "$";
	public static final String DEFAULT_DOMAIN = "http://www.investopedia.com";
	public static final String DEFAULT_DOMAIN2 = "www.investopedia.com";
	private static String PUBLIC_HEAD_STR = "public://";

	
	private DumpDao DumpDao;
	
	
	public void setDumpDao(DumpDao dumpDao) {
		DumpDao = dumpDao;
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
	
	private HashMap<Integer, Set<Integer>> autoReplish2Map(List<AutoRepublish> autoRepublishs){
		HashMap<Integer, Set<Integer>> AutoRepublishMap = new HashMap<Integer, Set<Integer>>();
		if(autoRepublishs !=null && autoRepublishs.size()>0){
			logger.info("dump autoRepublishs size :"+autoRepublishs.size());
			for(AutoRepublish a:autoRepublishs){
				if(AutoRepublishMap.containsKey(a.getNid())){
					AutoRepublishMap.get(a.getNid()).add(a.getType());
				}else {
					Set<Integer> set = new HashSet<Integer>();
					set.add(a.getType());
					AutoRepublishMap.put(a.getNid(), set);

				}
			}
		}
		return AutoRepublishMap;
	}
	
	private HashMap<String, List<Integer>> related2Map(List<Related> relateds){
		HashMap<String, List<Integer>> relatedMap = new HashMap<String, List<Integer>>();
		if(relateds !=null && relateds.size()>0){
			logger.info("dump relateds size :"+relateds.size());
			for(Related a:relateds){
				String key = a.getNid()+"_"+a.getVid();
				if(relatedMap.containsKey(key)){
					relatedMap.get(key).add(a.getRelated_nid());
				}else {
					List<Integer> list = new ArrayList<Integer>();
					list.add(a.getRelated_nid());
					relatedMap.put(key, list);

				}
			}
		}
		return relatedMap;
	}
	
	public Map<String, Map<String, Node>> dump(String indexType) {
		
		logger.info("dump data begin...");
		long starttime = System.currentTimeMillis();
		Map<String, Map<String, Node>> resultMap = new HashMap<String, Map<String,Node>>();
		try{
			
			List<Tag> tagList = DumpDao.selectAllTag();
			HashMap<String,List<Tag>> tagMap = new HashMap<String, List<Tag>>();
			if(tagList != null && tagList.size()>0){
				for(Tag t:tagList){
					if(StringUtils.isEmpty(t.getName()))continue;
					String key = t.getEntity_id()+"_"+t.getRevision_id();
					if(tagMap.containsKey(key)){
						tagMap.get(key).add(t);
					}else{
						List<Tag> list = new ArrayList<Tag>();
						list.add(t);
						tagMap.put(key, list);
					}
				}
				logger.info("dump tagList size :"+tagList.size());
			}
			
			//CacheUtil.tagMap = tagMap;
			
			///FileUtil.writeDUMPFile(dumpFilePath+File.separator+InvUtil.DUMP_FILE_TAG, data);
			
			
			List<UrlAlias> aliasList = DumpDao.selectALlUrl();
			HashMap<String,String> aliasMap = new HashMap<String,String>();
			
			if(aliasList!=null && aliasList.size()>0){
				for(UrlAlias a:aliasList){
					aliasMap.put(a.getSource(), a.getUrl());
				}
				logger.info("dump aliasList size :"+aliasList.size());
			}
			
			//CacheUtil.aliasMap = aliasMap;
			
			//HashMap<String,List<String>> tickerNodeMapping = new HashMap<String, List<String>>();
		
			
			List<PartnerLinks> partnerLinksList = DumpDao.selectPartnerLinks(null);
			HashMap<String,List<String>> partnerlinks_titleMap = new HashMap<String,List<String>>();
			HashMap<String,List<String>> partnerlinks_urlMap = new HashMap<String,List<String>>();
			
			if(partnerLinksList!=null && partnerLinksList.size()>0){
				String key ;
				for(PartnerLinks p : partnerLinksList){
					key = p.getNid()+"_"+p.getVid();
					if(partnerlinks_titleMap.containsKey(key)){
						partnerlinks_titleMap.get(key).add(p.getPartnerlinks_title());
						partnerlinks_urlMap.get(key).add(p.getPartnerlinks_url());
					}else {
						List<String> partnerlinks_titles = new ArrayList<String>();
						partnerlinks_titles.add(p.getPartnerlinks_title());
						partnerlinks_titleMap.put(key, partnerlinks_titles);
						
						List<String> partnerlinks_urls = new ArrayList<String>();
						partnerlinks_urls.add(p.getPartnerlinks_url());
						partnerlinks_urlMap.put(key, partnerlinks_urls);
					}
				}
				logger.info("dump partnerLinksList size :"+partnerLinksList.size());
			}
			
			List<AutoRepublish> autoRepublishs = DumpDao.selectAutoRepublish(null);
			HashMap<Integer, Set<Integer>> AutoRepublishMap =  autoReplish2Map(autoRepublishs);
			
			List<SynDate> syncDate = DumpDao.selectSynDate(null);
			HashMap<String, Long> syncDateMap = new HashMap<String, Long>();
			if(syncDate !=null && syncDate.size()>0){
				logger.info("dump syncDate size :"+syncDate.size());
				for(SynDate a:syncDate){
					syncDateMap.put(a.getNid()+"_"+a.getVid(), a.getSynDate());
				}
			}

			List<Related> relateds = DumpDao.selectRelateds(null);
			HashMap<String, List<Integer>> relatedMap =  related2Map(relateds);
			
			List<Node> nodeList = DumpDao.selectNodeDetail(null);
			// title order
			
			if(nodeList!=null && nodeList.size()>0){
				logger.info("dump nodeList size :"+nodeList.size());
				if(indexType.equalsIgnoreCase(Configure.index_type_all)) {
					Map<String, Node>  nodeMap_feed = dataETLForFeed(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,null,syncDateMap,relatedMap);//aliasMap repalce to aliasMapAu
					Map<String, Node> nodeMap_api = dataETL(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,null,syncDateMap,relatedMap);//aliasMap repalce to aliasMapAu
					resultMap.put(Configure.index_type_feed, nodeMap_feed);
					resultMap.put(Configure.index_type_api, nodeMap_api);

				}else {
					Map<String, Node> nodeMap_e = new HashMap<String, Node>();
					if(indexType.equalsIgnoreCase(Configure.index_type_feed)){
						nodeMap_e = dataETLForFeed(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,null,syncDateMap,relatedMap);//aliasMap repalce to aliasMapAu
					}else if (indexType.equalsIgnoreCase(Configure.index_type_api)) {
						nodeMap_e = dataETL(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,null,syncDateMap,relatedMap);//aliasMap repalce to aliasMapAu
					}else if (indexType.equalsIgnoreCase(Configure.index_type_search)) {
						nodeMap_e = dataETL(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,null,syncDateMap,relatedMap);//aliasMap repalce to aliasMapAu
					}
					resultMap.put(indexType, nodeMap_e);
				}
				
				
				
			}
			
			logger.info("dump data success,exe time :"+(System.currentTimeMillis()-starttime));
			
		}catch(Exception e){
			e.printStackTrace();
			return resultMap;
		}
		return resultMap;
	}
	
	public Map<String, Map<String, Node>> dumpIncremental(int nid,int vid,String treeAction,String indexType) {
		
		logger.info("dump data begin...");
		long starttime = System.currentTimeMillis();
		Map<String, Map<String, Node>> resultMap = new HashMap<String, Map<String,Node>>();

		Map<String, Node> nodeMap = new HashMap<String,Node>();
		Map<String, Object> searchParam = new HashMap<String, Object>();

		if(!StringUtils.isEmpty(treeAction) && treeAction.equalsIgnoreCase(TaskService.TreeAction.FULLY.getNCode())){
			searchParam.put("root_id", nid);
		}else {
			searchParam.put("nid", nid);
			searchParam.put("vid", vid);
			searchParam.put("source", "node/"+nid);
		}
		
		List<Tag> tagList = DumpDao.selectAllTagById(searchParam);
		List<Integer> tIdList = new ArrayList<Integer>();
		HashMap<String,List<Tag>> tagMap = new HashMap<String, List<Tag>>();
		if(tagList != null && tagList.size()>0){
			for(Tag t:tagList){
				if(StringUtils.isEmpty(t.getName()))continue;
				tIdList.add(t.getTid());
				String key = t.getEntity_id()+"_"+t.getRevision_id();
				if(tagMap.containsKey(key)){
					tagMap.get(key).add(t);
				}else{
					List<Tag> list = new ArrayList<Tag>();
					list.add(t);
					tagMap.put(key, list);
				}
			}
			logger.info("dump tagList size :"+tagList.size());
			logger.info("dump tagMap size :"+tagMap.size());
		}
		
		
		/**
		//CacheUtil.tagMap = tagMap;
		List<TagGroup> tagGroup = new ArrayList<TagGroup>();
		if (tIdList.size() > 0) {
			tagGroup = DumpDao.selectAllTagGroupById(tIdList);
		}
		
		HashMap<Long,List<TagGroup>> tagGroupMap = new HashMap<Long,List<TagGroup>>();
		
		if(tagGroup!=null && tagGroup.size()>0){
			for(TagGroup g:tagGroup){
				long tkey =g.getTid();
				if(tagGroupMap.containsKey(tkey)){
					tagGroupMap.get(tkey).add(g);
				}else{
					List<TagGroup> list = new ArrayList<TagGroup>();
					list.add(g);
					tagGroupMap.put(tkey, list);
				}
			}
			logger.info("dump tagGroup size :"+tagGroup.size());
			logger.info("dump tagGroupMap size :"+tagGroupMap.size());
		}
		//CacheUtil.tagGroupMap = tagGroupMap;
		**/
		
		List<UrlAlias> aliasList = DumpDao.selectALlUrlById(searchParam);
		HashMap<String,String> aliasMap = new HashMap<String,String>();
		
		if(aliasList!=null && aliasList.size()>0){
			for(UrlAlias a:aliasList){
				aliasMap.put(a.getSource(), a.getUrl());
			}
			logger.info("dump aliasList size :"+aliasList.size());
		}
		
	//	CacheUtil.aliasMap = aliasMap;
	
		List<AutoRepublish> autoRepublishs = DumpDao.selectAutoRepublish(searchParam);
		HashMap<Integer, Set<Integer>> AutoRepublishMap =  autoReplish2Map(autoRepublishs);

		
		List<PartnerLinks> partnerLinksList = DumpDao.selectPartnerLinks(searchParam);
		HashMap<String,List<String>> partnerlinks_titleMap = new HashMap<String,List<String>>();
		HashMap<String,List<String>> partnerlinks_urlMap = new HashMap<String,List<String>>();
		
		if(partnerLinksList!=null && partnerLinksList.size()>0){
			String key ;
			for(PartnerLinks p : partnerLinksList){
				key = p.getNid()+"_"+p.getVid();
				if(partnerlinks_titleMap.containsKey(key)){
					partnerlinks_titleMap.get(key).add(p.getPartnerlinks_title());
					partnerlinks_urlMap.get(key).add(p.getPartnerlinks_url());
				}else {
					List<String> partnerlinks_titles = new ArrayList<String>();
					partnerlinks_titles.add(p.getPartnerlinks_title());
					partnerlinks_titleMap.put(key, partnerlinks_titles);
					
					List<String> partnerlinks_urls = new ArrayList<String>();
					partnerlinks_urls.add(p.getPartnerlinks_url());
					partnerlinks_urlMap.put(key, partnerlinks_urls);
				}
			}
			logger.info("dump partnerLinksList size :"+partnerLinksList.size());
		}
		
		List<SynDate> syncDate = DumpDao.selectSynDate(searchParam);
		HashMap<String, Long> syncDateMap = new HashMap<String, Long>();
		if(syncDate !=null && syncDate.size()>0){
			logger.info("dump syncDate size :"+syncDate.size());
			for(SynDate a:syncDate){
				syncDateMap.put(a.getNid()+"_"+a.getVid(), a.getSynDate());
			}
		}
		
		List<Related> relateds = DumpDao.selectRelateds(searchParam);
		HashMap<String, List<Integer>> relatedsMap = related2Map(relateds);
		
		List<Node> nodeList = DumpDao.selectNodeDetail(searchParam);
		if(nodeList!=null && nodeList.size()>0){
			logger.info("dump nodeList size :"+nodeList.size());
			HashMap<String,String> aliasMapAu = new HashMap<String,String>();
			for(Node n : nodeList){
				int authorId = n.getAuthor_id();
				if(authorId > 0){
					Map<String, Object> searchParamAu = new HashMap<String, Object>();
					searchParamAu.put("source", "user/"+authorId);
					List<UrlAlias> aliasListAu = DumpDao.selectALlUrlById(searchParamAu);
					if(aliasListAu!=null && aliasListAu.size()>0){
						for(UrlAlias a:aliasListAu){
							aliasMapAu.put(a.getSource(), a.getUrl());
						}
						logger.info("dump Author url size :"+aliasListAu.size());
					}
				}
				
		   }

			if(indexType.equalsIgnoreCase(Configure.index_type_all)) {
				Map<String, Node>  nodeMap_feed = dataETLForFeed(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,aliasMapAu,syncDateMap,relatedsMap);
				Map<String, Node> nodeMap_api = dataETL(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,aliasMapAu,syncDateMap,relatedsMap);
				resultMap.put(Configure.index_type_feed, nodeMap_feed);
				resultMap.put(Configure.index_type_api, nodeMap_api);

			}else {
				Map<String, Node> nodeMap_e = new HashMap<String, Node>();
				if(indexType.equalsIgnoreCase(Configure.index_type_feed)){
					nodeMap_e = dataETLForFeed(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,aliasMapAu,syncDateMap,relatedsMap);
				}else if (indexType.equalsIgnoreCase(Configure.index_type_api)) {
					nodeMap_e = dataETL(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,aliasMapAu,syncDateMap,relatedsMap);
				}else if (indexType.equalsIgnoreCase(Configure.index_type_search)) {
					nodeMap_e = dataETL(nodeList, tagMap, aliasMap, CacheUtil.tagGroupMap,AutoRepublishMap,partnerlinks_titleMap,partnerlinks_urlMap,aliasMapAu,syncDateMap,relatedsMap);
				}
				resultMap.put(indexType, nodeMap_e);
			}
			
		}
		logger.info("dump data success,exe time :"+(System.currentTimeMillis()-starttime));
		return resultMap;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Node> dataETL(List<Node> nodeList ,Map<String,List<Tag>> tagMap,Map<String,String> aliasMap,Map<Long,List<TagGroup>> tagGroupMap,HashMap<Integer, Set<Integer>> AutoRepublishMap
			,HashMap<String,List<String>> partnerlinks_titleMap,HashMap<String,List<String>>  partnerlinks_urlMap,HashMap<String,String> aliasMapAu,HashMap<String, Long> syncDateMap,HashMap<String, List<Integer>> relatedMap){
		Map<String, Node> nodeMap = new HashMap<String, Node>();
		String key =null;
    	List<String> tagNames = null;
    	List<Integer> tagIds = null;
    	List<Integer> priority = null;
		if(nodeList!=null && nodeList.size()>0){
			String title = null;
			String c = null;
			String term_type = null;
			int channel_id=0;
			String channel_name=null;
			int sub_channel_id=0;
			String sub_channel_name=null;
			
			String advertising_name=null;
			String sub_advertising_name=null;
			String data=null;
			HashSet<String> tagGroupNames ;
	    	HashSet<Integer> tagGroupIds;
	    	String ticker;
	    	String ticker_s;
	    	String body;
	    	String summary;
	    	String node_type;
	    	String type_name;
	    	int type_id;
	    	String author_url = null;
	    	String nav_mapString = null;
			Map<Integer,TreeMap<Integer, List<Node>>> treeTopmap = new HashMap<Integer, TreeMap<Integer,List<Node>>>();
			Map<Integer, List<Node>> treePidmap = new HashMap<Integer, List<Node>>();

			int i = 0;
			int p;
			boolean isInvcontent_child = false;
			String url_page;
			for(Node node:nodeList){
				Node n = node.clone();
				if(n==null)continue;
				key= n.getNid()+"_"+n.getVid();
				nodeMap.put(key, n);
			
				node_type = n.getNode_type();
				
				if(node_type.equals("invcontent_child")){
					isInvcontent_child = true;
					body = StringUtil.CleanInvalidXmlChars(n.getChild_body(), "");
					summary = StringUtil.CleanInvalidXmlChars(n.getChild_summary(), "");
					title = StringUtil.CleanInvalidXmlChars(n.getTitle_old(), "").trim();
					type_name = n.getRoot_type_name();
					type_id = n.getRoot_type_id();
				}else {
					isInvcontent_child = false;

					body = StringUtil.CleanInvalidXmlChars(n.getInvbody(), "");
					summary = StringUtil.CleanInvalidXmlChars(n.getSummary(), "");
					title = StringUtil.CleanInvalidXmlChars(n.getTitle(), "").trim();
					type_name = n.getContentTypeName();
					type_id = n.getContentTypeTid();
				}
				if(body != null){
					body = body.replaceAll("href=\"/", "href=\"http://www.investopedia.com/");
				}
				if(aliasMap.containsKey("user/"+n.getAuthor_id())){
					author_url = aliasMap.get("user/"+n.getAuthor_id());
					n.setAuthor_url(author_url);
				}
				if(aliasMapAu !=null ){
					if(aliasMapAu.containsKey("user/"+n.getAuthor_id())){
						author_url = aliasMapAu.get("user/"+n.getAuthor_id());
						n.setAuthor_url(author_url);
					}
				}
				
				n.setInvbody(body);
				n.setSummary(summary);
				n.setTitle(title);
				n.setUrl(aliasMap.get("node/"+n.getNid()));
				url_page = aliasMap.get("node/"+n.getNid());
				if(StringUtils.isNotEmpty(url_page)){
						
					if(!url_page.startsWith("/")){
						url_page ="/" + url_page;
					}
					boolean isPage = false;
					if(!url_page.endsWith(DataConvertor.URL_SEP)){
						for(String suffix:DataConvertor.ALAIS_SUFFIXS){
							if(url_page.trim().toLowerCase().endsWith(suffix.trim().toLowerCase())){
								isPage = true;
								break;
							}
						}
					}
					
					if(!isPage){
						url_page = url_page + DataConvertor.URL_SEP;
					}
				
				}
				n.setUrl_page(url_page);
				n.setContentTypeName(type_name);
				n.setContentTypeTid(type_id);

				channel_id=n.getChannelTid();
				channel_name=n.getChannelName();
				sub_channel_id=n.getSubChannelTid();
				sub_channel_name=n.getSubChannelName()==null?"":n.getSubChannelName();
				
				advertising_name=n.getAdvertising_name();
				sub_advertising_name=n.getSub_advertising_name()==null?"":n.getSub_advertising_name();
				
				n.setChannelTid(channel_id==0?sub_channel_id:channel_id);
				n.setChannelName(channel_name==null?sub_channel_name:channel_name);
				
				n.setSubChannelTid(channel_id==0?0:sub_channel_id);
				n.setSubChannelName(channel_name==null?"":sub_channel_name);
				
				n.setAdvertising_name(advertising_name==null?sub_advertising_name:advertising_name);
				n.setSub_advertising_name(advertising_name==null?"":sub_advertising_name);
				if(StringUtils.isEmpty(title)){
					title ="0";
				}
				c = title.substring(0, 1);
				if(c.toLowerCase().compareTo("a") < 0){
					term_type = "1";
				}else {
					term_type = c.toUpperCase();
				}
				n.setTerm_type(term_type);
				n.setImage_url(dealUrlStr(n.getImage_url()));
				n.setVideo_url(dealUrlStr(n.getVideo_url()));
				if(AutoRepublishMap.containsKey(n.getNid())){
					n.setAuto_republish_type((List)Arrays.asList(AutoRepublishMap.get(n.getNid()).toArray()));
				}
				if(n.getUrl() != null && n.getUrl().trim().toLowerCase().startsWith("university/monthly-forex-report")){
					n.setIs_mfo_url(true);
				}
				n.setIndex(true);
				data = n.getMetatag_data();
				if(data !=null){
					try {
						HashMap map = (HashMap)PHPSerializer.unserialize(data.getBytes());
//						logger.info(map);
						if(map.containsKey("robots")){
							HashMap robots = (HashMap)map.get("robots");
							if(robots!=null){
								HashMap value = (HashMap)robots.get("value");
//								logger.info(value);
								if(value!=null){
									n.setIndex(!"noindex".equals(value.get("noindex")));
								}
							}
						}
					} catch (Exception e) {
					}
				}
				
				n.setAuthor_name(n.getAuthor_name()==null?"":n.getAuthor_name().trim());
				List<Tag> tags = tagMap.get(key);

            	if(tags !=null){

            		tagGroupNames = new HashSet<String>();
	    	        tagGroupIds = new HashSet<Integer>();
	            	tagNames = new ArrayList<String>();
	            	tagIds = new ArrayList<Integer>();
	            	priority = new ArrayList<Integer>();
            		 for(Tag tag : tags){
 		            	tagNames.add(tag.getName());
 		            	tagIds.add(tag.getTid());
 		            	priority.add(tag.getField_private_taxonomy_value());
 		            	long tkey = Long.valueOf(tag.getTid());
 		            	if(tagGroupMap.containsKey(tkey)){
 		            		List<TagGroup> tagGroups = tagGroupMap.get(tkey);
 		            		for(TagGroup tg:tagGroups){
 		            			tagGroupNames.add(tg.getGroupName());
 		            			tagGroupIds.add(tg.getGroupId());
 		            		}
 		            	}
 		            }
            		n.setTag_names(tagNames);
            		n.setTag_ids(tagIds);
            		n.setPriorities(priority);
            		n.setTag_group_name((List)Arrays.asList(tagGroupNames.toArray()));
            		n.setTag_group_id((List)Arrays.asList(tagGroupIds.toArray()));
            	}
				ticker = n.getTickerValue();
				List<String> tickers = new ArrayList<String>();
				HashSet<String> ticker_search = new HashSet<String>();
				if(!StringUtil.isNull(ticker)){
					StringTokenizer tokenizer = new StringTokenizer(ticker.toUpperCase(), ",");
					if(tokenizer.countTokens()>0){
						while(tokenizer.hasMoreTokens()){
							ticker_s = tokenizer.nextToken(); 
							tickers.add(ticker_s);
							ticker_search.add(ticker_s);
							if(ticker_s.indexOf('/') > -1){
								String[] sp = ticker_s.split("/");
								for(String s : sp){
									ticker_search.add(s);
								}
							}
						}
					}
					n.setTickers(tickers);
					n.setIs_include_ticker(true);
					n.setTicker_search((List)Arrays.asList(ticker_search.toArray()));
				}
				if(partnerlinks_titleMap.containsKey(key)){
					n.setPartnerlinks_titles(partnerlinks_titleMap.get(key));
				}
				if(partnerlinks_urlMap.containsKey(key)){
					n.setPartnerlinks_urls(partnerlinks_urlMap.get(key));
				}
				if(n.getPartner_url() !=null && !n.getPartner_url().startsWith("http")){
					n.setPartner_url("http://"+n.getPartner_url());
				}
				if(relatedMap !=null && relatedMap.containsKey(key)){
					n.setRelatedList(relatedMap.get(key));
				}
				n.setPartner_image_url(dealUrlStr(n.getPartner_image_url()));
				n.setTitleOrder(n.getTitle()==null?"":n.getTitle().replaceAll("\\s*", "").toLowerCase());
				if(syncDateMap!=null && syncDateMap.containsKey(key)){
					n.setSyn_date(syncDateMap.get(key));
				}else {
					n.setSyn_date(0);
				}
				
				//for Slideshow/Tutorial begin
				int depth = n.getDepth();

				if(depth>0){
					StringBuffer nav_map = new StringBuffer();
					nav_map.append(n.getP1());
					nav_map.append("_");
					nav_map.append(n.getP2());
					nav_map.append("_");
					nav_map.append(n.getP3());
					nav_map.append("_");
					nav_map.append(n.getP4());
					nav_map.append("_");
					nav_map.append(n.getP5());
					nav_map.append("_");
					nav_map.append(n.getP6());
					nav_map.append("_");
					nav_map.append(n.getP7());
					nav_map.append("_");
					nav_map.append(n.getP8());
					nav_map.append("_");
					nav_map.append(n.getP9());
					nav_mapString = nav_map.toString();
					int site = nav_mapString.indexOf("_0");
					n.setNavMap(nav_mapString.substring(0,site>=0?site:nav_mapString.length()));
					
					int p1 = n.getP1();
					int plid = n.getPlid();
					if(p1>0 && depth==1){
						if(treeTopmap.containsKey(p1)){
							TreeMap<Integer, List<Node>> depthMap = treeTopmap.get(p1);
							if(depthMap.containsKey(depth)){
								depthMap.get(depth).add(n);
							}else {
								List<Node> nList = new ArrayList<Node>();
								nList.add(n);
								depthMap.put(depth, nList);
							}
						}else {
							TreeMap<Integer, List<Node>> depthMap = new TreeMap<Integer, List<Node>>();
							List<Node> nList = new ArrayList<Node>();
							nList.add(n);
							depthMap.put(depth, nList);
							treeTopmap.put(p1, depthMap);
						}
					}
					
					if(plid > 0){
						if(treePidmap.containsKey(plid)){
							treePidmap.get(plid).add(n);
						}else {
							List<Node> nList = new ArrayList<Node>();
							nList.add(n);
							treePidmap.put(plid, nList);
						}
					}
				}
				//for Slideshow/Tutorial end
				i++;
			}
			
			for(Entry<Integer, TreeMap<Integer, List<Node>>> entry : treeTopmap.entrySet()){
				TreeMap<Integer, List<Node>> depthMap = entry.getValue();
				List<Node> nodes = depthMap.get(1);
					if(nodes!=null && nodes.size()>0){
						for(Node n : nodes){
							if(n.getHas_children()==1){
								TreeNode treeNode = new TreeNode();
								getChildreNode(treeNode, n, treePidmap);
								sortTreeNode(treeNode);
							}
						}						
					}
			}
			
		}
		return nodeMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Node> dataETLForFeed(List<Node> nodeList ,Map<String,List<Tag>> tagMap,Map<String,String> aliasMap,Map<Long,List<TagGroup>> tagGroupMap,HashMap<Integer, Set<Integer>> AutoRepublishMap
			,HashMap<String,List<String>> partnerlinks_titleMap,HashMap<String,List<String>>  partnerlinks_urlMap,HashMap<String,String> aliasMapAu,HashMap<String, Long> syncDateMap,HashMap<String, List<Integer>> relatedMap){
		Map<String, Node> nodeMap = new HashMap<String, Node>();
		String key =null;
    	List<String> tagNames = null;
    	List<Integer> tagIds = null;
    	List<Integer> priority = null;
		if(nodeList!=null && nodeList.size()>0){
			String title = null;
			String c = null;
			String term_type = null;
			int channel_id=0;
			String channel_name=null;
			int sub_channel_id=0;
			String sub_channel_name=null;
			
			String advertising_name=null;
			String sub_advertising_name=null;
			String data=null;
			HashSet<String> tagGroupNames ;
	    	HashSet<Integer> tagGroupIds;
	    	String ticker;
	    	String ticker_s;
	    	String body;
	    	String summary;
	    	String node_type;
	    	String type_name;
	    	int type_id;
	    	String author_url = null;
	    	String nav_mapString = null;
			Map<Integer,TreeMap<Integer, List<Node>>> treeTopmap = new HashMap<Integer, TreeMap<Integer,List<Node>>>();
			Map<Integer, List<Node>> treePidmap = new HashMap<Integer, List<Node>>();

			int i = 0;
			int p;
			boolean isInvcontent_child = false;
			String url_page;
			for(Node node:nodeList){
				Node n = node.clone();
				if(n==null) continue;
				key= n.getNid()+"_"+n.getVid();
				nodeMap.put(key, n);
			
				node_type = n.getNode_type();
				
				if(node_type.equals("invcontent_child")){
					isInvcontent_child = true;
					body = StringUtil.CleanInvalidXmlChars(n.getChild_body(), "");
					summary = StringUtil.CleanInvalidXmlChars(n.getChild_summary(), "");
					title = StringUtil.CleanInvalidXmlChars(n.getTitle_old(), "").trim();
					type_name = n.getRoot_type_name();
					type_id = n.getRoot_type_id();
				}else {
					isInvcontent_child = false;

					body = StringUtil.CleanInvalidXmlChars(n.getInvbody(), "");
					summary = StringUtil.CleanInvalidXmlChars(n.getSummary(), "");
					title = StringUtil.CleanInvalidXmlChars(n.getTitle(), "").trim();
					type_name = n.getContentTypeName();
					type_id = n.getContentTypeTid();
				}
				if(body != null){
					body = body.replaceAll("href=\"/", "href=\"http://www.investopedia.com/");
				}
				if(aliasMap.containsKey("user/"+n.getAuthor_id())){
					author_url = aliasMap.get("user/"+n.getAuthor_id());
					n.setAuthor_url(author_url);
				}
				if(aliasMapAu !=null ){
					if(aliasMapAu.containsKey("user/"+n.getAuthor_id())){
						author_url = aliasMapAu.get("user/"+n.getAuthor_id());
						n.setAuthor_url(author_url);
					}
				}
				
				n.setInvbody(body);
				n.setSummary(summary);
				n.setTitle(title);
				n.setUrl(aliasMap.get("node/"+n.getNid()));
				url_page = aliasMap.get("node/"+n.getNid());
				if(StringUtils.isNotEmpty(url_page)){
						
					if(!url_page.startsWith("/")){
						url_page ="/" + url_page;
					}
					boolean isPage = false;
					if(!url_page.endsWith(DataConvertor.URL_SEP)){
						for(String suffix:DataConvertor.ALAIS_SUFFIXS){
							if(url_page.trim().toLowerCase().endsWith(suffix.trim().toLowerCase())){
								isPage = true;
								break;
							}
						}
					}
					
					if(!isPage){
						url_page = url_page + DataConvertor.URL_SEP;
					}
				
				}
				n.setUrl_page(url_page);
				n.setContentTypeName(type_name);
				n.setContentTypeTid(type_id);

				channel_id=n.getChannelTid();
				channel_name=n.getChannelName();
				sub_channel_id=n.getSubChannelTid();
				sub_channel_name=n.getSubChannelName()==null?"":n.getSubChannelName();
				
				advertising_name=n.getAdvertising_name();
				sub_advertising_name=n.getSub_advertising_name()==null?"":n.getSub_advertising_name();
				
				n.setChannelTid(channel_id==0?sub_channel_id:channel_id);
				n.setChannelName(channel_name==null?sub_channel_name:channel_name);
				
				n.setSubChannelTid(channel_id==0?0:sub_channel_id);
				n.setSubChannelName(channel_name==null?"":sub_channel_name);
				
				n.setAdvertising_name(advertising_name==null?sub_advertising_name:advertising_name);
				n.setSub_advertising_name(advertising_name==null?"":sub_advertising_name);
				if(StringUtils.isEmpty(title)){
					title ="0";
				}
				c = title.substring(0, 1);
				if(c.toLowerCase().compareTo("a") < 0){
					term_type = "1";
				}else {
					term_type = c.toUpperCase();
				}
				n.setTerm_type(term_type);
				n.setImage_url(dealUrlStr(n.getImage_url()));
				n.setVideo_url(dealUrlStr(n.getVideo_url()));
				if(AutoRepublishMap.containsKey(n.getNid())){
					n.setAuto_republish_type((List)Arrays.asList(AutoRepublishMap.get(n.getNid()).toArray()));
				}
				if(n.getUrl() != null && n.getUrl().trim().toLowerCase().startsWith("university/monthly-forex-report")){
					n.setIs_mfo_url(true);
				}
				n.setIndex(true);
				data = n.getMetatag_data();
				if(data !=null){
					try {
						HashMap map = (HashMap)PHPSerializer.unserialize(data.getBytes());
//						logger.info(map);
						if(map.containsKey("robots")){
							HashMap robots = (HashMap)map.get("robots");
							if(robots!=null){
								HashMap value = (HashMap)robots.get("value");
//								logger.info(value);
								if(value!=null){
									n.setIndex(!"noindex".equals(value.get("noindex")));
								}
							}
						}
					} catch (Exception e) {
					}
				}
				
				n.setAuthor_name(n.getAuthor_name()==null?"":n.getAuthor_name().trim());
				List<Tag> tags = tagMap.get(key);

            	if(tags !=null){

            		tagGroupNames = new HashSet<String>();
	    	        tagGroupIds = new HashSet<Integer>();
	            	tagNames = new ArrayList<String>();
	            	tagIds = new ArrayList<Integer>();
	            	priority = new ArrayList<Integer>();
            		 for(Tag tag : tags){
 		            	tagNames.add(tag.getName());
 		            	tagIds.add(tag.getTid());
 		            	priority.add(tag.getField_private_taxonomy_value());
 		            	long tkey = Long.valueOf(tag.getTid());
 		            	if(tagGroupMap.containsKey(tkey)){
 		            		List<TagGroup> tagGroups = tagGroupMap.get(tkey);
 		            		for(TagGroup tg:tagGroups){
 		            			tagGroupNames.add(tg.getGroupName());
 		            			tagGroupIds.add(tg.getGroupId());
 		            		}
 		            	}
 		            }
            		n.setTag_names(tagNames);
            		n.setTag_ids(tagIds);
            		n.setPriorities(priority);
            		n.setTag_group_name((List)Arrays.asList(tagGroupNames.toArray()));
            		n.setTag_group_id((List)Arrays.asList(tagGroupIds.toArray()));
            	}
				ticker = n.getTickerValue();
				List<String> tickers = new ArrayList<String>();
				HashSet<String> ticker_search = new HashSet<String>();
				if(!StringUtil.isNull(ticker)){
					StringTokenizer tokenizer = new StringTokenizer(ticker.toUpperCase(), ",");
					if(tokenizer.countTokens()>0){
						while(tokenizer.hasMoreTokens()){
							ticker_s = tokenizer.nextToken(); 
							tickers.add(ticker_s);
							ticker_search.add(ticker_s);
							if(ticker_s.indexOf('/') > -1){
								String[] sp = ticker_s.split("/");
								for(String s : sp){
									ticker_search.add(s);
								}
							}
						}
					}
					n.setTickers(tickers);
					n.setIs_include_ticker(true);
					n.setTicker_search((List)Arrays.asList(ticker_search.toArray()));
				}
				if(partnerlinks_titleMap.containsKey(key)){
					n.setPartnerlinks_titles(partnerlinks_titleMap.get(key));
				}
				if(partnerlinks_urlMap.containsKey(key)){
					n.setPartnerlinks_urls(partnerlinks_urlMap.get(key));
				}
				if(n.getPartner_url() !=null && !n.getPartner_url().startsWith("http")){
					n.setPartner_url("http://"+n.getPartner_url());
				}
				if(relatedMap !=null && relatedMap.containsKey(key)){
					n.setRelatedList(relatedMap.get(key));
				}
				n.setPartner_image_url(dealUrlStr(n.getPartner_image_url()));
				n.setTitleOrder(n.getTitle()==null?"":n.getTitle().replaceAll("\\s*", "").toLowerCase());
				if(syncDateMap!=null && syncDateMap.containsKey(key)){
					n.setSyn_date(syncDateMap.get(key));
				}else {
					n.setSyn_date(0);
				}
				
				//for Slideshow/Tutorial begin
				int depth = n.getDepth();

				if(depth>0){
					StringBuffer nav_map = new StringBuffer();
					nav_map.append(n.getP1());
					nav_map.append("_");
					nav_map.append(n.getP2());
					nav_map.append("_");
					nav_map.append(n.getP3());
					nav_map.append("_");
					nav_map.append(n.getP4());
					nav_map.append("_");
					nav_map.append(n.getP5());
					nav_map.append("_");
					nav_map.append(n.getP6());
					nav_map.append("_");
					nav_map.append(n.getP7());
					nav_map.append("_");
					nav_map.append(n.getP8());
					nav_map.append("_");
					nav_map.append(n.getP9());
					nav_mapString = nav_map.toString();
					int site = nav_mapString.indexOf("_0");
					n.setNavMap(nav_mapString.substring(0,site>=0?site:nav_mapString.length()));
					
					int p1 = n.getP1();
					int plid = n.getPlid();
					if(p1>0 && depth==1){
						if(treeTopmap.containsKey(p1)){
							TreeMap<Integer, List<Node>> depthMap = treeTopmap.get(p1);
							if(depthMap.containsKey(depth)){
								depthMap.get(depth).add(n);
							}else {
								List<Node> nList = new ArrayList<Node>();
								nList.add(n);
								depthMap.put(depth, nList);
							}
						}else {
							TreeMap<Integer, List<Node>> depthMap = new TreeMap<Integer, List<Node>>();
							List<Node> nList = new ArrayList<Node>();
							nList.add(n);
							depthMap.put(depth, nList);
							treeTopmap.put(p1, depthMap);
						}
					}
					
					if(plid > 0){
						if(treePidmap.containsKey(plid)){
							treePidmap.get(plid).add(n);
						}else {
							List<Node> nList = new ArrayList<Node>();
							nList.add(n);
							treePidmap.put(plid, nList);
						}
					}
					
					
				}

				if(StringUtils.isEmpty(n.getImage_url())){
					setImageUrlAndFileName(n);
				}
				//for Slideshow/Tutorial end
				i++;
			}
			
			for(Entry<Integer, TreeMap<Integer, List<Node>>> entry : treeTopmap.entrySet()){
				TreeMap<Integer, List<Node>> depthMap = entry.getValue();
				List<Node> nodes = depthMap.get(1);
					if(nodes!=null && nodes.size()>0){
						for(Node n : nodes){
							if(n.getHas_children()==1){
								TreeNode treeNode = new TreeNode();
								getChildreNode(treeNode, n, treePidmap);
								sortTreeNode(treeNode);
							}
						}						
					}
			}
			
		}
		return nodeMap;
	}
	
	private String getImagePath(List<String> tags) {
		String path = null;
		if (tags == null || tags.size() == 0) {
			return "investing";
		}
		for (String tag : tags) {
			if (",ETFs,ETNs,International Markets,Stock Analysis,".toLowerCase().indexOf(","+tag.toLowerCase()+",") != -1) {
				return "activeTrading";
			}
		}
		for (String tag : tags) {
			if (",Currencies,Forex,Forex Brokers and Accounts,Forex Derivatives,Forex Fundamentals,Forex Technical Analysis,Forex Theory,Forex Trading Strategies,Forex-Advanced,Forex-Beginner,Forex-Intermediate,Formulas,Interest Rates,"
					.toLowerCase().indexOf(","+tag.toLowerCase()+",") != -1) {
				return "forex";
			}
		}
		if (path == null) {
			path = "investing";
		}
		return path;
	}
	
	public void setImageUrlAndFileName(Node n) {
		Map<String, List<AutoImage>> autoImageMap = CacheUtil.autoImageMap;
		if (autoImageMap.isEmpty()) {
			autoImageMap.put("forex", getImageList("forex"));
			autoImageMap.put("activeTrading", getImageList("activeTrading"));
			autoImageMap.put("investing", getImageList("investing"));
		}
		if (StringUtils.isEmpty(n.getImage_url())) {
			String path = getImagePath(n.getTag_names());
			List<AutoImage> imageList = (List<AutoImage>) autoImageMap.get(path);
			if(imageList!=null && imageList.size()>0){
				Random r = new Random();
				int index = r.nextInt(imageList.size());
				n.setImage_url((String.format(Configure.imageUrl, new String[] { path, imageList.get(index).getName() })));
				n.setImage_filemime("image/jpeg");
				n.setImage_size(imageList.get(index).getSize());
			}
		}
	}

	private List<AutoImage> getImageList(String path) {
		File filePath = new File(Configure.imagesPath + File.separator + path);
		File[] fileList = filePath.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().toLowerCase().endsWith(".jpg");
			}
		});
		List<AutoImage> list = new ArrayList<AutoImage>();
		if(fileList!=null && fileList.length >0){
			for (File f : fileList) {
				AutoImage image = new AutoImage();
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(f);
					image.setSize(fis.available());
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if(fis!=null){
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				image.setName(f.getName());
				list.add(image);
			}
		}
		return list;
	}
	
	public static void sortTreeNode(TreeNode treeNode){
		
		if(treeNode != null){
			
			List<TreeNode> clist = treeNode.getChildrenList();
			Node node = treeNode.getNode();
			if(node != null){
				String tree_site =node.getTree_index();
				if(node.getDepth()==1){
					tree_site = "00";
					node.setTree_index(tree_site);
				}
				if(clist != null && clist.size()>0){
					Collections.sort(clist, new Comparator<TreeNode>() {
						@Override
						public int compare(TreeNode o1, TreeNode o2) {
							if(o1 != null && o2 != null ){
								if( o2.getNode().getWeight() > o1.getNode().getWeight()){
									return -1;
								}
							}
							return 0;
						}
					});
					String tree_index = "";
					int count = 0;
					for(int i=0;i<clist.size();i++){
						TreeNode t = clist.get(i);
						count = i+1; 
						if(count<10){
							tree_index = "0"+count;
						}else {
							tree_index = ""+count;
						}
						if(tree_site != null){
							tree_index = tree_site + "" + tree_index;
						}
						t.getNode().setTree_index(tree_index);
						if(t.getChildrenList()!=null && t.getChildrenList().size()>0){
							sortTreeNode(t);
						}
						
					}
				
				}
			}
			
			
			
			
			
		}
	}
	/**
	 * 
	 * @param reNode
	 * @param n
	 * @param treePidmap
	 */
	public void getChildreNode(TreeNode reNode,Node n,Map<Integer, List<Node>> treePidmap){
		Integer plid = n.getPlid();
		Integer lid = n.getMlid();
		if(treePidmap.containsKey(lid)){
			List<Node> nodes = treePidmap.get(lid);
			if(nodes != null){
				for(Node node : nodes){
					Integer hashChildren = node.getHas_children();
					TreeNode treeNodeChild = new TreeNode();
					treeNodeChild.setpLid(lid);
					treeNodeChild.setHas_children(node.getHas_children());
					treeNodeChild.setId(node.getMlid());
					treeNodeChild.setRootId(node.getRoot_nid());
					treeNodeChild.setNode(node);
					if(hashChildren>0){
						getChildreNode(treeNodeChild, node, treePidmap);
					}
					reNode.getChildrenList().add(treeNodeChild);
				}
				reNode.setNode(n);
			}
			
		}
	}
	
	
	public static void main(String[] args) {
		/**
		ClassPathXmlApplicationContext content = new ClassPathXmlApplicationContext("classpath:applicationContext-task.xml");
		DumpService dumpService = new DumpService();
		DumpDao dumpDao = (DumpDao)content.getBean("DumpDao");
		Map<String, Object> searchParam = new HashMap<String, Object>();
		searchParam.put("nid", 1000);
		searchParam.put("vid", 1000);
		searchParam.put("source", "node/"+1000);
		//dumpDao.selectNodeDetail(searchParam);
		dumpService.setDumpDao(dumpDao);
		dumpService.dumpIncremental(1000, 1000);
		
	//	System.err.println(false && false);
	
		String data = "a:2:{s:5:\"title\";a:1:{s:5:\"value\";s:17:\"Prioritizing Debt\";}s:6:\"robots\";a:0:{}}";

		String data2 ="a:2:{s:5:\"title\";a:1:{s:5:\"value\";s:31:\"An Introduction To Mutual Funds\";}s:6:\"robots\";a:0:{}}";
		
		String data3 ="a:1:{s:6:\"robots\";a:1:{s:5:\"value\";a:1:{s:7:\"noindex\";s:7:\"noindex\";}}}";
		HashMap map;
		try {
			map = (HashMap)PHPSerializer.unserialize(data2.getBytes());
			if(map.containsKey("robots")){
				HashMap robots = (HashMap)map.get("robots");
				if(robots!=null){
					HashMap value = (HashMap)robots.get("value");
//					logger.info(value);
					if(value!=null){
					}
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		logger.info(map);
		
		
		TreeNode treeNode = new TreeNode();
		Node node = new Node();
		treeNode.setNode(node);
		List<TreeNode> children = new ArrayList<TreeNode>();
		TreeNode child = new TreeNode();
		child.setNode(new Node());
		child.setWeight(2);
		TreeNode child2 = new TreeNode();
		child2.setWeight(1);
		child2.setNode(new Node());

		TreeNode child21 = new TreeNode();
		child21.setWeight(1);
		child21.setNode(new Node());

		TreeNode child22 = new TreeNode();
		child22.setWeight(1);
		child22.setNode(new Node());
		child2.getChildrenList().add(child22);
		child2.getChildrenList().add(child21);
		
		children.add(child);
		children.add(child2);
		treeNode.getChildrenList().addAll(children);
		
		DumpService dumpService = new DumpService();
		dumpService.sortTreeNode(treeNode);
		System.out.println(treeNode.getChildrenList().get(0).getNode().getTree_index());
		
		**/
		/**
		
		Node node = new Node();
		node.setAuthor_id(100);
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(node);
		node.setAuthor_id(200);
		System.out.println(nodes.get(0).getAuthor_id());
		
		String nav_mapString = "236759_0";
		System.out.println(nav_mapString.substring(0,nav_mapString.indexOf("_0")));
		**/
		/**
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(1);
		Collections.sort(list, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if(o1 != null && o2 != null ){
					if( o1 > o2){
						return -1;
					}
				}
				return 0;
			}
		});
		for(Integer i: list){
			System.out.println(i);
		}
		
		
		Node node = new Node();
		node.setAuthor_name("name1");
		Node node2 = node.clone();
		node2.setAuthor_name("name2");
		System.out.println(node.getAuthor_name());
		**/
	//	int n= "Currencies,Forex,Forex Brokers and Accounts,Forex Derivatives,Forex Fundamentals,Forex Technical Analysis,Forex Theory,Forex Trading Strategies,Forex-Advanced,Forex-Beginner,Forex-Intermediate,Formulas,Interest Rates".toLowerCase().indexOf("Technical Indicators");
		//System.out.println(n);
		List<String> list = new ArrayList<String>();
		list.add("Momentum Trading");
		list.add("Technical Analysis");
		list.add("Technical Indicators");
		DumpService dumpService = new DumpService();
	    String s = dumpService.getImagePath(list);
	    System.out.println(s);
		
	}


}
