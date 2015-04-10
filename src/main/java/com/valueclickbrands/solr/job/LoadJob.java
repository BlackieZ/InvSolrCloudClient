package com.valueclickbrands.solr.job;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.valueclickbrands.solr.dao.DumpDao;
import com.valueclickbrands.solr.model.TagGroup;
import com.valueclickbrands.solr.model.WebToolSettings;
import com.valueclickbrands.solr.service.ServiceMain;
import com.valueclickbrands.solr.service.TaxonomyService;
import com.valueclickbrands.solr.util.CacheUtil;

public class LoadJob {
	private static Logger logger = Logger.getLogger(LoadJob.class);
	private static final ReentrantLock LoadJob = new ReentrantLock();

	private TaxonomyService taxonomyService;
	
	public TaxonomyService getTaxonomyService() {
		return taxonomyService;
	}

	public void setTaxonomyService(TaxonomyService taxonomyService) {
		this.taxonomyService = taxonomyService;
	}

	public void execute() {
		if (LoadJob.tryLock()) {
			try {
				logger.info("LoadJob start.");
				long startTime = System.currentTimeMillis();
				
				Map<String, HashSet<Long>> tagMap = taxonomyService.getTagGroup_tag();
				if(tagMap.size()>0){
					logger.info("tagMap size:"+tagMap.size());
					CacheUtil.tagGroupName_tagMap = tagMap;
				}
				
				Map<String, Long> tagv2_mapMap = taxonomyService.getTagV2Map();
				if(tagv2_mapMap.size()>0){
					logger.info("tagv2_mapMap size:"+tagv2_mapMap.size());
					CacheUtil.tagv2Name_tagMap = tagv2_mapMap;
				}
				
				Map<Long, List<TagGroup>> tagGroupMap = taxonomyService.getTagGroupMap();
				if(tagGroupMap.size()>0){
					logger.info("tagGroupMap size:"+tagGroupMap.size());
					CacheUtil.tagGroupMap = tagGroupMap;
				}

				Map<String,WebToolSettings> webToolSettingsMap = taxonomyService.getWebToolSettingsMap();
				if(webToolSettingsMap.size()>0){
					logger.info("webToolSettingsMap size:"+webToolSettingsMap.size());
					CacheUtil.webToolSettingsMap = webToolSettingsMap;
				}
				
				
				logger.info("LoadJob end. exe time:" +(System.currentTimeMillis()-startTime) );
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				LoadJob.unlock();
			}
		}else {
			logger.info("LoadJob is already excuting!");

		}

	}

}
