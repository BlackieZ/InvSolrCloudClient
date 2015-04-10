package com.valueclickbrands.solr.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;


/** 
 * @author Vanson Zou
 * @date Dec 31, 2014 
 */

public class Taxonomy {
	
	private long nid;
	private long vid;
	private String id;
	
	private long pageID;
	private String website;
	private String path;
	private String hashKey;
	private String channel;
	private String subChannel;
	private String advertising;
	private String subAdvertising;
	private String adTarget;
	private String type;
	private BigDecimal lucrativeness;
	private String timelessness;
	private String feature;
	private String design;
	
	private String interestLevel;
	private boolean index;
	private int noIndexParams;
	private boolean follow;
	private int master;
	private String createdBy;
	private String author_full_name;
	private String createdOn;
	private String updatedBy;
	private String updatedOn;
	private String metatag_data;
	private List<DrupalTaxonomyTag> tags;
	private List<String> tag_names;
	private List<Long> tag_ids;
	private List<String> tag_group_name;
	private List<Integer> tag_group_id;
	private List<Long> tag_prioritys;
	private String outNoIndexParams;
	private String p_type;
	
	private long pid;
	private String data_type;
	private String url;
	
	private String dfp;
	private long typeTid;
	private long templateId;
	private long channelTid;
	private long timelessnessTid;
	private long interestLevelTid;
	private long advertisingTid;
	private String keywords;
	private String node_type;
	private String webToolSettings;
	private String metatags_title;
	private String metatags_description;
	
	public String getMetatags_title() {
		return metatags_title;
	}

	public void setMetatags_title(String metatags_title) {
		this.metatags_title = metatags_title;
	}

	public String getMetatags_description() {
		return metatags_description;
	}

	public void setMetatags_description(String metatags_description) {
		this.metatags_description = metatags_description;
	}

	public String getWebToolSettings() {
		return webToolSettings;
	}

	public void setWebToolSettings(String webToolSettings) {
		this.webToolSettings = webToolSettings;
	}

	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}

	public List<Integer> getTag_group_id() {
		return tag_group_id;
	}

	public void setTag_group_id(List<Integer> tag_group_id) {
		this.tag_group_id = tag_group_id;
	}

	public List<String> getTag_group_name() {
		return tag_group_name;
	}

	public void setTag_group_name(List<String> tag_group_name) {
		this.tag_group_name = tag_group_name;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDfp() {
		return dfp;
	}

	public void setDfp(String dfp) {
		this.dfp = dfp;
	}

	public long getTypeTid() {
		return typeTid;
	}

	public void setTypeTid(long typeTid) {
		this.typeTid = typeTid;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public long getChannelTid() {
		return channelTid;
	}

	public void setChannelTid(long channelTid) {
		this.channelTid = channelTid;
	}

	public long getTimelessnessTid() {
		return timelessnessTid;
	}

	public void setTimelessnessTid(long timelessnessTid) {
		this.timelessnessTid = timelessnessTid;
	}

	public long getInterestLevelTid() {
		return interestLevelTid;
	}

	public void setInterestLevelTid(long interestLevelTid) {
		this.interestLevelTid = interestLevelTid;
	}

	public long getAdvertisingTid() {
		return advertisingTid;
	}

	public void setAdvertisingTid(long advertisingTid) {
		this.advertisingTid = advertisingTid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor_full_name() {
		return author_full_name;
	}

	public void setAuthor_full_name(String author_full_name) {
		this.author_full_name = author_full_name;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public List<Long> getTag_prioritys() {
		return tag_prioritys;
	}

	public void setTag_prioritys(List<Long> tag_prioritys) {
		this.tag_prioritys = tag_prioritys;
	}

	public List<String> getTag_names() {
		return tag_names;
	}

	public void setTag_names(List<String> tag_names) {
		this.tag_names = tag_names;
	}

	
	public List<Long> getTag_ids() {
		return tag_ids;
	}

	public void setTag_ids(List<Long> tag_ids) {
		this.tag_ids = tag_ids;
	}

	public String getP_type() {
		return p_type;
	}

	public void setP_type(String p_type) {
		this.p_type = p_type;
	}

	public String getOutNoIndexParams() {
		return outNoIndexParams;
	}

	public void setOutNoIndexParams(String outNoIndexParams) {
		this.outNoIndexParams = outNoIndexParams;
	}

	public long getPageID() {
		return pageID;
	}

	public void setPageID(long pageID) {
		this.pageID = pageID;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHashKey() {
		return hashKey;
	}

	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSubChannel() {
		return subChannel;
	}

	public void setSubChannel(String subChannel) {
		this.subChannel = subChannel;
	}

	public String getAdvertising() {
		return advertising;
	}

	public void setAdvertising(String advertising) {
		this.advertising = advertising;
	}

	public String getSubAdvertising() {
		return subAdvertising;
	}

	public void setSubAdvertising(String subAdvertising) {
		this.subAdvertising = subAdvertising;
	}

	public String getAdTarget() {
		return adTarget;
	}

	public void setAdTarget(String adTarget) {
		this.adTarget = adTarget;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getLucrativeness() {
		return lucrativeness;
	}

	public void setLucrativeness(BigDecimal lucrativeness) {
		this.lucrativeness = lucrativeness;
	}

	public String getTimelessness() {
		return timelessness;
	}

	public void setTimelessness(String timelessness) {
		this.timelessness = timelessness;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getInterestLevel() {
		return interestLevel;
	}

	public void setInterestLevel(String interestLevel) {
		this.interestLevel = interestLevel;
	}

	public boolean isIndex() {
		return index;
	}

	public void setIndex(boolean index) {
		this.index = index;
	}

	public int getNoIndexParams() {
		return noIndexParams;
	}

	public void setNoIndexParams(int noIndexParams) {
		this.noIndexParams = noIndexParams;
	}

	public boolean isFollow() {
		return follow;
	}

	public void setFollow(boolean follow) {
		this.follow = follow;
	}

	public int getMaster() {
		return master;
	}

	public void setMaster(int master) {
		this.master = master;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public List<DrupalTaxonomyTag> getTags() {
		return tags;
	}

	public void setTags(List<DrupalTaxonomyTag> tags) {
		this.tags = tags;
	}

	public String getMetatag_data() {
		return metatag_data;
	}

	public void setMetatag_data(String metatag_data) {
		this.metatag_data = metatag_data;
	}

	public long getNid() {
		return nid;
	}

	public void setNid(long nid) {
		this.nid = nid;
	}

	public long getVid() {
		return vid;
	}

	public void setVid(long vid) {
		this.vid = vid;
	}
	
	
	
	
	 
}
