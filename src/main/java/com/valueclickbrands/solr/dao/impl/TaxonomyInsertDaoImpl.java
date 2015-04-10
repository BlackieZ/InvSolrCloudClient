package com.valueclickbrands.solr.dao.impl;

import java.util.List;
import java.util.Map;

import com.valueclickbrands.framework.db.IbatisGenericDao;
import com.valueclickbrands.solr.dao.DumpDao;
import com.valueclickbrands.solr.dao.TaxonomyInsertDao;
import com.valueclickbrands.solr.model.Adtarget;
import com.valueclickbrands.solr.model.Advertising;
import com.valueclickbrands.solr.model.Channel;
import com.valueclickbrands.solr.model.ContentType;
import com.valueclickbrands.solr.model.DrupalTaxonomyTag;
import com.valueclickbrands.solr.model.InterestLevel;
import com.valueclickbrands.solr.model.InvTag;
import com.valueclickbrands.solr.model.Invpage;
import com.valueclickbrands.solr.model.Master;
import com.valueclickbrands.solr.model.Metatag;
import com.valueclickbrands.solr.model.Node;
import com.valueclickbrands.solr.model.Noindexparams;
import com.valueclickbrands.solr.model.Tag;
import com.valueclickbrands.solr.model.TagGroup;
import com.valueclickbrands.solr.model.Taxonomy;
import com.valueclickbrands.solr.model.TaxonomyTag;
import com.valueclickbrands.solr.model.Timelessness;
import com.valueclickbrands.solr.model.InvUrlAlias;
import com.valueclickbrands.solr.model.Website;
import com.valueclickbrands.solr.util.DataConvertor;

public class TaxonomyInsertDaoImpl extends IbatisGenericDao implements TaxonomyInsertDao {

	private String prefix = "devel";
	
	private String selectTemplateSQL = "selectTemplate";
	private String insertInvpageSQL = "insertInvpage";
	private String insertDataContentTypeSQL = "insertDataContentType";
	private String insertRevisionContentTypeSQL = "insertRevisionContentType";
	private String insertDataWebsiteSQL = "insertDataWebsite";
	private String insertRevisionWebsiteSQL = "insertRevisionWebsite";
	private String insertDataChannelSQL = "insertDataChannel";
	private String insertRevisionChannelSQL = "insertRevisionChannel";
	private String insertDataTagSQL = "insertDataTag";
	private String insertRevisionTagSQL = "insertRevisionTag";
	private String insertDataLucrativenessSQL = "insertDataLucrativeness";
	private String insertRevisionLucrativenessSQL = "insertRevisionLucrativeness";
	private String insertDataTimelessnessSQL = "insertDataTimelessness";
	private String insertRevisionTimelessnessSQL = "insertRevisionTimelessness";
	private String insertDataFeatureSQL = "insertDataFeature";
	private String insertRevisionFeatureSQL = "insertRevisionFeature";
	private String insertDataDesignSQL = "insertDataDesign";
	private String insertRevisionDesignSQL = "insertRevisionDesign";
	private String insertDataInterestLevelSQL = "insertDataInterestLevel";
	private String insertRevisionInterestLevelSQL = "insertRevisionInterestLevel";
	private String insertDataAdvertisingSQL = "insertDataAdvertising";
	private String insertRevisionAdvertisingSQL = "insertRevisionAdvertising";
	private String insertDataAdtargetSQL = "insertDataAdtarget";
	private String insertRevisionAdtargetSQL = "insertRevisionAdtarget";
	private String insertDataMasterSQL = "insertDataMaster";
	private String insertRevisionMasterSQL = "insertRevisionMaster";
	private String insertDataNoindexparamsSQL = "insertDataNoindexparams";
	private String insertRevisionNoindexparamsSQL = "insertRevisionNoindexparams";
	private String insertUrlAliasSQL = "insertUrlAlias";
	private String insertMetatagSQL = "insertMetatag";
	private String deleteMetatagSQL = "deleteMetatag";
	private String checkInvpageByTitleSQL = "checkInvpageByTitle";
	
	

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	

	@Override
	public long insertPageTaxonomy(Taxonomy pageTaxonomy) {
		long pid = 0;
		try {
			long created = System.currentTimeMillis();
			//insert invpage
			logger.debug("insert invpage");
			Invpage invpage = new Invpage(DataConvertor.reMoveSlash(pageTaxonomy.getUrl())+"#"+pageTaxonomy.getTemplateId(),created/1000);
			int count = this.tableCountNum(checkInvpageByTitleSQL, invpage);
			if(count>0){
				logger.info("invpage["+invpage.getTitle()+"] is exist.");
				return pid;
			}
			pid = (Long)this.insert(insertInvpageSQL, invpage);
			
			//insert content type
			logger.debug("insert content type");
			if(pageTaxonomy.getTypeTid()>0 && pageTaxonomy.getType()!=null){
				ContentType contentType = new ContentType(pid,pageTaxonomy.getTypeTid());
				this.insert(insertDataContentTypeSQL, contentType);
				this.insert(insertRevisionContentTypeSQL, contentType);
			}
			
			//insert website
			logger.debug("insert website");
			if(pageTaxonomy.getWebsite()!=null){
				Website website = new Website(pid,pageTaxonomy.getWebsite());
				this.insert(insertDataWebsiteSQL, website);
				this.insert(insertRevisionWebsiteSQL, website);
			}
			
			//insert channel
			logger.debug("insert channel");
			if(pageTaxonomy.getChannelTid()>0 && pageTaxonomy.getChannel()!=null){
				Channel channel = new Channel(pid,pageTaxonomy.getChannelTid());
				this.insert(insertDataChannelSQL, channel);
				this.insert(insertRevisionChannelSQL, channel);
			}
			
			//insert tags
			logger.debug("insert tags");
			if(pageTaxonomy.getTags()!=null){
				List<DrupalTaxonomyTag>  tags = pageTaxonomy.getTags();
				for(int i=0;i<tags.size();i++){
					InvTag tag = new InvTag(pid,tags.get(i).getTagID(),i);
					this.insert(insertDataTagSQL,tag);
					this.insert(insertRevisionTagSQL,tag);
				}
			}
			
			/**
			//insert Lucrativeness
			logger.debug("insert Lucrativeness");
			if(pageTaxonomy.getLucrativeness()!=null){
				Lucrativeness Lucrativeness = new Lucrativeness(pid,pageTaxonomy.getLucrativeness().toString());
				this.insert(insertDataLucrativenessSQL,Lucrativeness);
				this.insert(insertRevisionLucrativenessSQL,Lucrativeness);
			}
			**/
			
			//insert Timelessness
			logger.debug("insert Timelessness");
			if(pageTaxonomy.getTimelessnessTid()>0 && pageTaxonomy.getTimelessness()!=null){
				Timelessness timelessness = new Timelessness(pid,pageTaxonomy.getTimelessnessTid());
				this.insert(insertDataTimelessnessSQL,timelessness);
				this.insert(insertRevisionTimelessnessSQL,timelessness);
			}
			/**
			//insert Feature
			logger.debug("insert Feature");
			if(pageTaxonomy.getFeature()!=null){
				Feature feature = new Feature(pid,pageTaxonomy.getFeature());
				this.insert(insertDataFeatureSQL,feature);
				this.insert(insertRevisionFeatureSQL,feature);
			}
			
			//insert Design
			logger.debug("insert Design");
			if(pageTaxonomy.getDesign()!=null){
				Design design = new Design(pid,pageTaxonomy.getDesign());
				this.insert(insertDataDesignSQL,design);
				this.insert(insertRevisionDesignSQL,design);
			}
			**/
			
			//insert InterestLevel
			logger.debug("insert InterestLevel");
			if(pageTaxonomy.getInterestLevelTid()>0 && pageTaxonomy.getInterestLevel()!=null){
				InterestLevel interestLevel = new InterestLevel(pid,pageTaxonomy.getInterestLevelTid());
				this.insert(insertDataInterestLevelSQL,interestLevel);
				this.insert(insertRevisionInterestLevelSQL,interestLevel);
			}
			
			//insert Advertising
			logger.debug("insert Advertising");
			if(pageTaxonomy.getAdvertisingTid()>0 && pageTaxonomy.getAdvertising()!=null){
				Advertising advertising = new Advertising(pid,pageTaxonomy.getAdvertisingTid());
				this.insert(insertDataAdvertisingSQL,advertising);
				this.insert(insertRevisionAdvertisingSQL,advertising);
			}
			
			//insert Adtarget
			logger.debug("insert Adtarget");
			if(pageTaxonomy.getAdTarget()!=null){
				Adtarget adtarget = new Adtarget(pid,pageTaxonomy.getAdTarget());
				this.insert(insertDataAdtargetSQL,adtarget);
				this.insert(insertRevisionAdtargetSQL,adtarget);
			}
			
			//insert Master
			logger.debug("insert Master");
			Master master = new Master(pid,String.valueOf(pageTaxonomy.getMaster()));
			this.insert(insertDataMasterSQL,master);
			this.insert(insertRevisionMasterSQL,master);
			
			//insert Noindexparams
			logger.debug("insert Noindexparams");
			Noindexparams noindexparams = new Noindexparams(pid,String.valueOf(pageTaxonomy.getNoIndexParams()));
			this.insert(insertDataNoindexparamsSQL,noindexparams);
			this.insert(insertRevisionNoindexparamsSQL,noindexparams);
			
			//insert UrlAlias
			logger.debug("insert UrlAlias");
			InvUrlAlias urlAlias = new InvUrlAlias("invpage/"+pid,DataConvertor.reMoveSlash(pageTaxonomy.getUrl()));
			this.insert(insertUrlAliasSQL,urlAlias);
			
			//insert Metatag
			logger.debug("insert Metatag");
//			HashMap data = new HashMap();
//			HashMap robots = robots = new HashMap();
//			data.put("robots", robots);
//			HashMap value = new HashMap();
//			robots.put("value", value);
//			if(!pageTaxonomy.getIndex()){
//				value.put("noindex","noindex");
//			}
//			if(!pageTaxonomy.getFollow()){
//				value.put("nofollow","nofollow");
//			}
			Metatag metatag = new Metatag(pid,pageTaxonomy.getMetatag_data());
//			if(!pageTaxonomy.getIndex() && !pageTaxonomy.getFollow()){
			if(metatag.getData()!=null){
//				metatag.setData(new String(PHPSerializer.serialize(data, "utf-8")));
				this.remove(deleteMetatagSQL, metatag);
				this.insert(insertMetatagSQL,metatag);
			}
			logger.info(Thread.currentThread().getName()+" insert page taxonomy total time:"+(System.currentTimeMillis()-created));
			return pid;
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		return pid;
			
	}

	
}
