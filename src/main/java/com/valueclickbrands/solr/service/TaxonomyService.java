package com.valueclickbrands.solr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.And;
import com.valueclickbrands.solr.dao.DumpDao;
import com.valueclickbrands.solr.dao.TaxonomyInsertDao;
import com.valueclickbrands.solr.model.DrupalTaxonomyTag;
import com.valueclickbrands.solr.model.Tag;
import com.valueclickbrands.solr.model.TagGroup;
import com.valueclickbrands.solr.model.Taxonomy;
import com.valueclickbrands.solr.model.TaxonomyTag;
import com.valueclickbrands.solr.model.UrlAlias;
import com.valueclickbrands.solr.model.WebToolSettings;
import com.valueclickbrands.solr.util.CacheUtil;
import com.valueclickbrands.solr.util.ContentTypesMap;
import com.valueclickbrands.solr.util.DataConvertor;
import com.valueclickbrands.solr.util.PHPSerializer;

/** 
 * @author Vanson Zou
 * @date Dec 30, 2014 
 */

public class TaxonomyService {
	private static Logger logger = Logger.getLogger(TaxonomyService.class);
	private DumpDao DumpDao;
	private TaxonomyInsertDao taxonomyInsertDao;
	private ZKService zkService;
	private Gson gson = new Gson();


	public void setTaxonomyInsertDao(TaxonomyInsertDao taxonomyInsertDao) {
		this.taxonomyInsertDao = taxonomyInsertDao;
	}

	public ZKService getZkService() {
		return zkService;
	}

	public void setZkService(ZKService zkService) {
		this.zkService = zkService;
	}

	public DumpDao getDumpDao() {
		return DumpDao;
	}

	public void setDumpDao(DumpDao dumpDao) {
		DumpDao = dumpDao;
	}
	
	
	public List<Taxonomy> getTaxonomyListForNode(){
		
		List<UrlAlias> aliasList = DumpDao.selectALlUrl();
		HashMap<String,String> aliasMap = new HashMap<String,String>();
		
		if(aliasList!=null && aliasList.size()>0){
			for(UrlAlias a:aliasList){
				aliasMap.put(a.getSource(), a.getUrl());
			}
			logger.info("dump aliasList size :"+aliasList.size());
		}
		
		List<TaxonomyTag> tagList = DumpDao.selectTaxonomyTagForNode(null);
		Map<String,List<DrupalTaxonomyTag>> taxTagMap = new HashMap<String, List<DrupalTaxonomyTag>>();
		Map<String, List<String>> tagNames_map = new HashMap<String, List<String>>();
		Map<String, List<Long>> tagIds_map = new HashMap<String, List<Long>>();
		Map<String, List<Long>> tagPrioritys_map = new HashMap<String, List<Long>>();
		
		tagList2Map(tagList, tagNames_map, tagIds_map, tagPrioritys_map,true);
		
		List<Taxonomy> taxList = DumpDao.selectTaxonomyForNode(null);	
		Map<Long,List<TagGroup>> tagGroupMap = CacheUtil.tagGroupMap;

		
		dataETL(taxList, aliasMap, tagNames_map, tagIds_map, tagPrioritys_map,tagGroupMap,CacheUtil.webToolSettingsMap, true);
		
		return taxList;
	}
	
public List<Taxonomy> getTaxonomyListForNode(int nid,int vid,String treeAction){
	
		Map<String, Object> searchParam = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(treeAction) && treeAction.equalsIgnoreCase(TaskService.TreeAction.FULLY.getNCode())){
			searchParam.put("root_id", nid);
		}else {
			searchParam.put("nid", nid);
			searchParam.put("vid", vid);
			searchParam.put("source", "node/"+nid);
		}
		
		List<UrlAlias> aliasList = DumpDao.selectALlUrlById(searchParam);
		HashMap<String,String> aliasMap = new HashMap<String,String>();
		
		if(aliasList!=null && aliasList.size()>0){
			for(UrlAlias a:aliasList){
				aliasMap.put(a.getSource(), a.getUrl());
			}
			logger.info("dump aliasList size :"+aliasList.size());
		}
		
		List<TaxonomyTag> tagList = DumpDao.selectTaxonomyTagForNode(searchParam);
		Map<String, List<String>> tagNames_map = new HashMap<String, List<String>>();
		Map<String, List<Long>> tagIds_map = new HashMap<String, List<Long>>();
		Map<String, List<Long>> tagPrioritys_map = new HashMap<String, List<Long>>();
		logger.info("Taxonomy node dump tagList size :"+tagList==null?0:tagList.size());
		tagList2Map(tagList, tagNames_map, tagIds_map, tagPrioritys_map,true);
		
		List<Taxonomy> taxList = DumpDao.selectTaxonomyForNode(searchParam);	
		Map<Long,List<TagGroup>> tagGroupMap = CacheUtil.tagGroupMap;

		
		dataETL(taxList, aliasMap, tagNames_map, tagIds_map, tagPrioritys_map,tagGroupMap,CacheUtil.webToolSettingsMap, true);
		
		return taxList;
	}
	
	public List<Taxonomy> getTaxonomyListForPage(){

		List<UrlAlias> aliasList = DumpDao.selectALlUrlForPage();
		HashMap<String,String> aliasMap = new HashMap<String,String>();
		
		if(aliasList!=null && aliasList.size()>0){
			for(UrlAlias a:aliasList){
				aliasMap.put(a.getSource(), a.getUrl());
			}
			logger.info("dump aliasList size :"+aliasList.size());
		}
		List<TaxonomyTag> tagList = DumpDao.selectTaxonomyTagForPage(null);
		Map<String,List<DrupalTaxonomyTag>> taxTagMap = new HashMap<String, List<DrupalTaxonomyTag>>();
		Map<String, List<String>> tagNames_map = new HashMap<String, List<String>>();
		Map<String, List<Long>> tagIds_map = new HashMap<String, List<Long>>();
		Map<String, List<Long>> tagPrioritys_map = new HashMap<String, List<Long>>();
		
		tagList2Map(tagList, tagNames_map, tagIds_map, tagPrioritys_map,false);
		
		List<Taxonomy> taxList = DumpDao.selectTaxonomyForPage(null);
		Map<Long,List<TagGroup>> tagGroupMap = CacheUtil.tagGroupMap;
		dataETL(taxList, aliasMap, tagNames_map, tagIds_map, tagPrioritys_map,tagGroupMap,CacheUtil.webToolSettingsMap, false);
		
		return taxList;
	}
	
	public void WebToolSettings2Map(List<WebToolSettings> list,Map<String,WebToolSettings>  webToolSettings_map){
		
		if(list !=null && list.size()>0){
			String url;
			for(WebToolSettings webToolSettings :list){
				url = webToolSettings.getUrl_alias();
				if(url!=null && !url.startsWith("/")){
					url = "/"+url;
				}
				webToolSettings_map.put(url,webToolSettings);
			}
		}
		
	}
	
	public void tagList2Map(List<TaxonomyTag> tagList,Map<String, List<String>> tagNames_map,Map<String, List<Long>> tagIds_map,Map<String, List<Long>> tagPrioritys_map,boolean isNode){
		
		for(TaxonomyTag taxonomyTag : tagList){
			
			String key = taxonomyTag.getNid()+"";
			if(isNode){
				key = taxonomyTag.getNid()+"_"+taxonomyTag.getVid();
			}
			if(tagNames_map.containsKey(key)){
				tagNames_map.get(key).add(taxonomyTag.getTag_name());
				tagIds_map.get(key).add(taxonomyTag.getTag_id());
				tagPrioritys_map.get(key).add(taxonomyTag.getField_private_taxonomy_value());
			}else {
				List<String> tag_names = new ArrayList<String>();
				List<Long> tag_ids = new ArrayList<Long>();
				List<Long> tag_prioritys = new ArrayList<Long>();
				tag_names.add(taxonomyTag.getTag_name());
				tag_ids.add(taxonomyTag.getTag_id());
				tag_prioritys.add(taxonomyTag.getField_private_taxonomy_value());
				tagNames_map.put(key, tag_names);
				tagIds_map.put(key, tag_ids);
				tagPrioritys_map.put(key, tag_prioritys);
			}
			
		}
	}
	public void dataETL(List<Taxonomy> taxList,HashMap<String,String> aliasMap,Map<String, List<String>> tagNames_map,Map<String, List<Long>> tagIds_map,Map<String, List<Long>> tagPrioritys_map,Map<Long,List<TagGroup>> tagGroupMap
			,Map<String,WebToolSettings>  webToolSettings_map,boolean isNode){
		
		String channel_name;
		String sub_channel_name;
		String advertising_name;
		String sub_advertising_name;
		Calendar outCreateOn; 
		String data;
		String[] infos ;
		String uri ;
		String host;
		String newSubDomain ;
		String parameters ;
		String url;
		String key;
		HashSet<String> tagGroupNames ;
    	HashSet<Integer> tagGroupIds;
		String id;
		String data_type = "invpage";
		if(isNode){
			data_type = "node";
		}
		
		String sourceKey;
		if(taxList !=null && taxList.size()>0){
			logger.info("dump taxList for page size :"+taxList.size());

			for(Taxonomy t:taxList){
				sourceKey = data_type+"/"+t.getPid();
				key = t.getPid()+"";
				id = data_type+"_"+t.getPageID();
				
				if(isNode){
					sourceKey = data_type+"/"+t.getNid();
					key = t.getNid()+"_"+t.getVid();
					id = data_type +"_"+t.getNid();
				}
				
				url = aliasMap.get(sourceKey)==null?t.getUrl():aliasMap.get(sourceKey);
				
				channel_name=t.getChannel();
				sub_channel_name=t.getSubChannel()==null?"":t.getSubChannel();
				advertising_name=t.getAdvertising();
				sub_advertising_name=t.getSubAdvertising()==null?"":t.getSubAdvertising();
				t.setId(id);
				
				t.setData_type(data_type);
				t.setChannel(channel_name==null?sub_channel_name:channel_name);
				t.setSubChannel(channel_name==null?"":sub_channel_name);
				t.setAdvertising(advertising_name==null?sub_advertising_name:advertising_name);
				t.setSubAdvertising(advertising_name==null?"":sub_advertising_name);
				t.setIndex(true);
				t.setFollow(true);
				data = t.getMetatag_data();
				
				
				
				if(data !=null){
						
						try {
							HashMap map = (HashMap)PHPSerializer.unserialize(data.getBytes());
							
							try {
//								logger.info(map);
									if(map.containsKey("robots")){
										HashMap robots = (HashMap)map.get("robots");
										if(robots!=null){
											HashMap value = (HashMap)robots.get("value");
											if(value!=null){
												t.setIndex(!"noindex".equals(value.get("noindex")));
												t.setFollow(!"nofollow".equals(value.get("nofollow")));
											}
										}
									}
								} catch (Exception e) {
								}
							
							try {
								if (map.containsKey("title")) {
									HashMap robots = (HashMap)map.get("title");
									if(robots!=null){
										String value = (String)robots.get("value");
										if(value!=null){
											t.setMetatags_title(value);
										}
									}
								}
							} catch (Exception e) {
							}
							
							try {
								if (map.containsKey("description")) {
									HashMap robots = (HashMap)map.get("description");
									if(robots!=null){
										String value = (String)robots.get("value");
										if(value!=null){
											t.setMetatags_description(value);
										}
									}
								}
							} catch (Exception e) {
							}
						} catch (IllegalAccessException e1) {
						}
					
					
				}
				t.setPageID(DataConvertor.intString2Integer(String.valueOf(t.getNid())));
				List<Long> tagIds = tagIds_map.get(key);

				if(tagIds !=null){
	            	tagGroupNames = new HashSet<String>();
	            	tagGroupIds = new HashSet<Integer>();
            		 for(Long tid : tagIds){
 		            	if(tagGroupMap.containsKey(tid)){
 		            		for(TagGroup tg:tagGroupMap.get(tid)){
 		            			tagGroupNames.add(tg.getGroupName());
 		            			tagGroupIds.add(tg.getGroupId());
 		            		}
 		            	}
 		            }

             		t.setTag_group_name((List)Arrays.asList(tagGroupNames.toArray()));
            		t.setTag_group_id((List)Arrays.asList(tagGroupIds.toArray()));
            	}
				
				
				
				if(!isNode){
					t.setPageID(t.getPid());
				}
				if(tagNames_map.containsKey(key)){
					t.setTag_names(tagNames_map.get(key));
					t.setTag_ids(tagIds_map.get(key));
					t.setTag_prioritys(tagPrioritys_map.get(key));
				}
				t.setType(t.getP_type()==null?t.getType():t.getP_type());
				t.setCreatedBy(t.getCreatedBy()==null?"":t.getCreatedBy().trim());
			
				if(url !=null ){
					
					if(!url.startsWith("/")){
						url ="/" + url;
					}
					infos = DataConvertor.buildURL(url.toLowerCase());
					uri = infos[0];
					host = infos[1];
					newSubDomain = infos[2];
					parameters = infos[3];
					
					t.setUrl(infos[4]);
					t.setWebsite(newSubDomain);
					t.setPath(DataConvertor.buildPath(host, uri));
					t.setHashKey(DataConvertor.buildHashKey(uri));
					
				}else if(!isNode && t.getPid()==17){
					url = "/";
					t.setHashKey("$");
					t.setUrl(url);
					t.setPath("http://www.investopedia.com/invpage/17/");
					t.setWebsite("www");
				}
				
				if(webToolSettings_map.containsKey(url)){
					t.setWebToolSettings(webToolSettings_map.get(url).getSettings());
				}
				
				
		}
		
		}
	}
	
	public Taxonomy getTaxonomyForTemplateByPid(long template_pid,String url){
		String source = "invpage/"+template_pid;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("source", source);
		params.put("pid", template_pid);
		params.put("type", "invpage_template");
		
		
		HashMap<String,String> aliasMap = new HashMap<String,String>();
		/**
		List<UrlAlias> aliasList = DumpDao.selectUrlBySource(params);
		if(aliasList!=null && aliasList.size()>0){
			for(UrlAlias a:aliasList){
				aliasMap.put(a.getSource(), a.getUrl());
			}
			logger.info("dump aliasList size :"+aliasList.size());
		}
		**/
		
		Map<String,List<DrupalTaxonomyTag>> taxTagMap = new HashMap<String, List<DrupalTaxonomyTag>>();
		Map<String, List<String>> tagNames_map = new HashMap<String, List<String>>();
		Map<String, List<Long>> tagIds_map = new HashMap<String, List<Long>>();
		Map<String, List<Long>> tagPrioritys_map = new HashMap<String, List<Long>>();
		
		
		List<TaxonomyTag> tagList = DumpDao.selectTaxonomyTagForPage(params);
		if(tagList!=null && tagList.size()>0){
			logger.info("dump tagList size :"+tagList.size());
			tagList2Map(tagList, tagNames_map, tagIds_map, tagPrioritys_map, false);
		}
		
		List<Taxonomy> taxList = DumpDao.selectTaxonomyForPage(params);
		
		Map<Long,List<TagGroup>> tagGroupMap = CacheUtil.tagGroupMap;
		if(taxList !=null && taxList.size()>0){
			logger.info("dump taxList size :"+taxList.size());
			taxList.get(0).setUrl(url);
			dataETL(taxList, aliasMap, tagNames_map, tagIds_map, tagPrioritys_map, tagGroupMap,CacheUtil.webToolSettingsMap,false);
			return taxList.get(0);
		}

		return null;
	}
	
	
	private HashMap<String,String> autoMatch(Taxonomy taxonomy){
		HashMap<String,String> map = new HashMap<String,String>();
		if(taxonomy!=null){
			String advertising = "Investing";
			String subAdvertising = null;
			String path = taxonomy.getUrl();
			List<Long> tags = taxonomy.getTag_ids();
			
			if(taxonomy.getWebsite().equals("www") 
					&& path.equals(DataConvertor.URL_SEP)){
				advertising = "Investing";
				subAdvertising = "Homepage";
			}
			if(path.startsWith("/simulator/")){
				advertising = "Investing";
				subAdvertising = "Simulator";
			}
			if(taxonomy.getChannel().equals("Professionals")){
				advertising = "Professionals";
				subAdvertising = null;
			}
			if(isInclude(tags,getTagDroupChildrenName("Professional Education"))){
				advertising = "Professionals";
				subAdvertising = "FA";
			}
			if(path.startsWith("/quizzer/")){
				advertising = "Professionals";
				subAdvertising = "Quizzer";
			}
			if(taxonomy.getChannel().equals("Personal Finance")){
				advertising = "Personal Finance";
				subAdvertising = null;
			}
			if(isInclude(tags,getTagDroupChildrenName("Personal Finance"))){
				advertising = "Personal Finance";
				subAdvertising = null;
			}
			if(taxonomy.getSubChannel().equals("Taxes")){
				advertising = "Personal Finance";
				subAdvertising = "Taxes";
			}
			if(isInclude(tags,getTagDroupChildrenName("Taxes"))){
				advertising = "Personal Finance";
				subAdvertising = "Taxes";
			}
			if(isInclude(tags,getTagDroupChildrenName("Bonds"))){
				advertising = "Investing";
				subAdvertising = "Bonds";
			}
			if(taxonomy.getSubChannel().equals("Insurance")){
				advertising = "Personal Finance";
				subAdvertising = "Insurance";
			}
			if(isInclude(tags,getTagDroupChildrenName("Insurance"))){
				advertising = "Personal Finance";
				subAdvertising = "Insurance";
			}
			if(taxonomy.getSubChannel().equals("Retirement")){
				advertising = "Personal Finance";
				subAdvertising = "Retirement";
			}
			if(isInclude(tags,getTagDroupChildrenName("Retirement"))){
				advertising = "Personal Finance";
				subAdvertising = "Retirement";
			}
			
			if(taxonomy.getChannel().equals("Active Trading")){
				advertising = "Active Trading";
				subAdvertising = null;
			}
			if(isInclude(tags,getTagDroupChildrenName("Active Trading"))){
				advertising = "Active Trading";
				subAdvertising = null;
			}
			if(taxonomy.getChannel().equals("Markets")){
				advertising = "Active Trading";
				subAdvertising = null;
			}
			if(taxonomy.getChannel().equals("Markets") && taxonomy.getSubChannel().equals("Quotes")){
				advertising = "Active Trading";
				subAdvertising = "Quotes";
			}
			
			if(taxonomy.getSubChannel().equals("Options & Futures")){
				advertising = "Options & Futures";
				subAdvertising = null;
			}
			if(isInclude(tags,getTagDroupChildrenName("Options And Futures"))){
				advertising = "Options And Futures";
				subAdvertising = null;
			}
			if(taxonomy.getChannel().equals("Forex")){
				advertising = "Forex";
				subAdvertising = null;
			}
			if(isInclude(tags,getTagDroupChildrenName("Forex"))){
				advertising = "Forex";
				subAdvertising = null;
			}
			if(path.startsWith("/fxtrader/")){
				advertising = "Forex";
				subAdvertising = "FXTrader";
			}
			
			if(taxonomy.getSubChannel().equals("Mutual Funds & ETFs")){
				advertising = "ETFs & Mutual Funds";
				subAdvertising = null;
			}
			
			HashSet<Long> set = new HashSet<Long>();
			set.add(CacheUtil.tagv2Name_tagMap.get("ETFs"));
			set.add(CacheUtil.tagv2Name_tagMap.get("Index Funds"));
			set.add(CacheUtil.tagv2Name_tagMap.get("Leveraged ETF"));
			if(isInclude(tags,set)){
				advertising = "ETFs & Mutual Funds";
				subAdvertising = "ETF";
			}
			if(taxonomy.getSubChannel().equals("ETF Center")){
				advertising = "ETFs & Mutual Funds";
				subAdvertising = "ETF";
			}
			
			HashSet<Long> set2 = new HashSet<Long>();
			set2.add(CacheUtil.tagv2Name_tagMap.get("Mutual Funds"));
			if(isInclude(tags,set2)){
				advertising = "ETFs & Mutual Funds";
				subAdvertising = "Mutual Funds";
			}
			
			taxonomy.setAdvertising(advertising);
			taxonomy.setSubAdvertising(subAdvertising);
			long advertisingTid = ContentTypesMap.getAdvertisingTid(subAdvertising==null?advertising:subAdvertising);
			taxonomy.setAdvertisingTid(advertisingTid);
			
			String ad_target = ContentTypesMap.getAdTargetByAdvertising(advertising);
			if(subAdvertising!=null){
				ad_target = ContentTypesMap.getAdTargetBySubAdvertising(subAdvertising);
			}
			if(ad_target==null){
				ad_target = "investopedia.com";
			}
			logger.debug("advertising:"+advertising+",ad_target"+ad_target);
			taxonomy.setAdTarget(ad_target);
			taxonomy.setDfp(ContentTypesMap.getDFPByAdTarget(ad_target));
		}
		return map;
	}
	
	public Map<String, HashSet<Long>> getTagGroup_tag(){
		List<TagGroup> tagGroup = new ArrayList<TagGroup>();
		tagGroup = DumpDao.selectAllTagGroup();
		HashMap<String,HashSet<Long>> tagGroupMap = new HashMap<String,HashSet<Long>>();
		
		if(tagGroup!=null && tagGroup.size()>0){
			for(TagGroup g:tagGroup){
				if(tagGroupMap.containsKey(g.getGroupName())){
					tagGroupMap.get(g.getGroupName()).add(Long.valueOf(g.getTid()));
				}else{
					HashSet<Long> list = new HashSet<Long>();
					list.add(Long.valueOf(g.getTid()));
					tagGroupMap.put(g.getGroupName(), list);
				}
			}
			logger.info("dump TagGroup_tag size :"+tagGroup.size());
		}
		return tagGroupMap;
	}
	
	public Map<String, Long> getTagV2Map(){
		List<Tag> tags = DumpDao.selectAllTagV2();
		HashMap<String,Long> tagGroupMap = new HashMap<String,Long>();
		
		if(tags!=null && tags.size()>0){
			for(Tag g:tags){
				tagGroupMap.put(g.getName(), Long.valueOf(g.getTid()));
			}
			logger.info("dump TagGroup_tag size :"+tags.size());
		}
		return tagGroupMap;
		
	}
	
	public Map<Long,List<TagGroup>> getTagGroupMap(){
		List<TagGroup> tagGroup = DumpDao.selectAllTagGroup();
		HashMap<Long,List<TagGroup>> tagGroupMap = new HashMap<Long,List<TagGroup>>();
		
		if(tagGroup!=null && tagGroup.size()>0){
			for(TagGroup g:tagGroup){
				long tkey = Long.valueOf(g.getTid());
				if(tagGroupMap.containsKey(tkey)){
					tagGroupMap.get(tkey).add(g);
				}else{
					List<TagGroup> list = new ArrayList<TagGroup>();
					list.add(g);
					tagGroupMap.put(tkey, list);
				}
			}
			logger.info("dump tagGroup size :"+tagGroup.size());
		}
		return tagGroupMap;
		
	}
	
	public Map<String,WebToolSettings> getWebToolSettingsMap(){
		
		Map<String,WebToolSettings>  webToolSettings_map = new HashMap<String, WebToolSettings>();
		List<WebToolSettings> list = DumpDao.selectWebToolSettings(null);
		WebToolSettings2Map(list, webToolSettings_map);
		return webToolSettings_map;
		
	}
	
	private HashSet<Long> getTagDroupChildrenName(String parentName){
		HashSet<Long> tagIdMapping = new HashSet<Long>();
		if(CacheUtil.tagGroupName_tagMap.containsKey(parentName)){
			tagIdMapping = CacheUtil.tagGroupName_tagMap.get(parentName);
		}
		return tagIdMapping;
	}
	
	private boolean isInclude(List<Long> tagIds,HashSet<Long> tags){
		if(tagIds==null){
			return false;
		}
		for(Long tag:tagIds){
			if(tags.contains(tag)){
				return true;
			}
		}
		return false;
	}
	

	public Taxonomy addTaxonomy(long template_pid,String url){
		Taxonomy t = getTaxonomyForTemplateByPid(template_pid,url);
		if(t != null){
			t.setPageID(0);
			t.setTemplateId(template_pid);
			try {
				autoMatch(t);
				BeforInsert(t);
				long page_id = taxonomyInsertDao.insertPageTaxonomy(t);
				if(page_id ==0){
					return null;
				}else {
					t.setPageID(page_id);
					t.setId("invpage_"+page_id);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return null;
			}
		}
		return t;
	}

	public void BeforInsert(Taxonomy t){
		
	}
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext content = new ClassPathXmlApplicationContext("classpath:applicationContext-task.xml");
		TaxonomyService taxonomyService = new TaxonomyService();
		DumpDao dumpDao = (DumpDao)content.getBean("DumpDao");
		TaxonomyInsertDao taxonomyInsertDao = (TaxonomyInsertDao)content.getBean("TaxonomyInsertDao");
		taxonomyService.setDumpDao(dumpDao);
		taxonomyService.setTaxonomyInsertDao(taxonomyInsertDao);
		//System.out.println(taxonomy.getTag_names());
		Map<String, Object> searchParam = new HashMap<String, Object>();
		searchParam.put("pid", 19);
		List<Taxonomy> list = dumpDao.selectTaxonomyForPage(searchParam);
		System.out.println(list.get(0).getKeywords());
		//taxonomyService.getTaxonomyListForNode();
	}
	
	
	
}
