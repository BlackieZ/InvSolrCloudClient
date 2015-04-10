package com.valueclickbrands.solr.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;


public class Node implements Serializable,Cloneable {
	public static final String NODE_FIELD_TITLE = "title";
	public static final String NODE_FIELD_SUMMARY = "summary";
	public static final String NODE_FIELD_BODY = "body";
	public static final String NODE_FIELD_SITEDATE = "sitedate";
	public static final String NODE_FIELD_CONTENTTYPE = "ContentType";
	public static final String NODE_FIELD_CHANNEL = "Channel";
	
	public static final String separator = ",";
	private int nid;
	private int vid;
	private String title;
	private String summary;
	private String invbody;
	private long sitedate;
	private int contentTypeTid;
	private String contentTypeName;
	private int channelTid;
	private String channelName;
	private String tickerValue;
	private String partenr_name;
	private int partenr_id;
	private String image_url;
	private long image_size;
	private String video_url;
	private int video_size;
	private String img_alt;
	private String img_title;
	private int author_id;
	private String author_name;
	private String url;
	private boolean is_mfo_url;
	private boolean is_include_ticker;
	private List<String> tag_group_name;
	private List<Integer> tag_group_id;
	private List<String> tickers;
	private List<String> tag_names;
	private List<Integer> tag_ids;
	private int subChannelTid;
	private String subChannelName;
	private List<Integer> auto_republish_type;//1 investopdia,2 yahoo,3 msft
	private int sponsored_content;
	private String term_type;
	private String advertising_name;
	private String sub_advertising_name;
	private String titleOrder;
	private List<Integer> priorities;
	private int featured_video_nid;
	private int show_main_image;
	private int seo_standout;
	private long original_sitedate;
	private long final_time;
	private String metatag_data;
	private boolean index;
	private List<String> ticker_search;
	private String partner_url;
	private String partner_image_url;
	private String title_old;
	private String node_type;
	private String child_body;
	private String child_summary;
	private List<String> partnerlinks_titles;
	private List<String> partnerlinks_urls;
	private String root_type_name;
	private int root_nid;
	private String root_title;
	private int root_type_id;
	private String author_url;
	private String author_legal_disclaimer;
	private int video_series_id;
	private String video_series_name;
	private String image_filemime;
	private String video_filemime;
	//for Slideshow/Tutorial
	private int plid;
	private int mlid;
	private String link_path;
	private String link_title;
	private int has_children;
	private int weight;
	private int depth;
	private int p1;
	private int p2;
	private int p3;
	private int p4;
	private int p5;
	private int p6;
	private int p7;
	private int p8;
	private int p9;
	private String tree_index;
	//for Slideshow/Tutorial end 
	private String navMap;
	private int pnid;
	private long syn_date;
	private List<Integer> relatedList;
	private String url_page;//for feed rss20 ,if page ,add '/' at end
	public Node(){
		
	}
	
	public Node(String[] data){
		nid = Integer.parseInt(data[0]);
		vid = Integer.parseInt(data[1]);
		title = data[2];
		summary = data[3];
		sitedate = Long.parseLong(data[4]);
		invbody = data[5];
		contentTypeTid = Integer.parseInt(data[6]);
		contentTypeName = data[7];
		channelTid = Integer.parseInt(data[8]);
		channelName = data[9];
		tickerValue = data[10];
		partenr_name = data[11];
		partenr_id = Integer.valueOf(data[12]);
		image_url = data[13];
		//image_size = Long.valueOf(data[14]);
		video_url = data[15];
		//video_size =  Long.valueOf(data[16]);
		img_alt = data[17];
		img_title = data[18];
		//author_id = Long.valueOf(data[19]);
		author_name = data[20];
		url = data[21];
	}
	
	public String getUrl_page() {
		return url_page;
	}

	public void setUrl_page(String url_page) {
		this.url_page = url_page;
	}

	public List<Integer> getRelatedList() {
		return relatedList;
	}

	public void setRelatedList(List<Integer> relatedList) {
		this.relatedList = relatedList;
	}

	public long getSyn_date() {
		return syn_date;
	}

	public void setSyn_date(long syn_date) {
		this.syn_date = syn_date;
	}

	public int getPnid() {
		return pnid;
	}

	public void setPnid(int pnid) {
		this.pnid = pnid;
	}

	public String getTree_index() {
		return tree_index;
	}

	public void setTree_index(String tree_index) {
		this.tree_index = tree_index;
	}

	public String getNavMap() {
		return navMap;
	}

	public void setNavMap(String navMap) {
		this.navMap = navMap;
	}

	public int getMlid() {
		return mlid;
	}

	public void setMlid(int mlid) {
		this.mlid = mlid;
	}

	public String getImage_filemime() {
		return image_filemime;
	}


	public void setImage_filemime(String image_filemime) {
		this.image_filemime = image_filemime;
	}


	public String getVideo_filemime() {
		return video_filemime;
	}


	public void setVideo_filemime(String video_filemime) {
		this.video_filemime = video_filemime;
	}



	public String getAuthor_url() {
		return author_url;
	}

	
	public int getPlid() {
		return plid;
	}

	public void setPlid(int plid) {
		this.plid = plid;
	}

	public String getLink_path() {
		return link_path;
	}



	public void setLink_path(String link_path) {
		this.link_path = link_path;
	}



	public String getLink_title() {
		return link_title;
	}



	public void setLink_title(String link_title) {
		this.link_title = link_title;
	}



	public int getHas_children() {
		return has_children;
	}



	public void setHas_children(int has_children) {
		this.has_children = has_children;
	}



	public int getWeight() {
		return weight;
	}



	public void setWeight(int weight) {
		this.weight = weight;
	}



	public int getDepth() {
		return depth;
	}



	public void setDepth(int depth) {
		this.depth = depth;
	}



	public int getP1() {
		return p1;
	}



	public void setP1(int p1) {
		this.p1 = p1;
	}



	public int getP2() {
		return p2;
	}



	public void setP2(int p2) {
		this.p2 = p2;
	}



	public int getP3() {
		return p3;
	}



	public void setP3(int p3) {
		this.p3 = p3;
	}



	public int getP4() {
		return p4;
	}



	public void setP4(int p4) {
		this.p4 = p4;
	}



	public int getP5() {
		return p5;
	}



	public void setP5(int p5) {
		this.p5 = p5;
	}



	public int getP6() {
		return p6;
	}



	public void setP6(int p6) {
		this.p6 = p6;
	}



	public int getP7() {
		return p7;
	}



	public void setP7(int p7) {
		this.p7 = p7;
	}



	public int getP8() {
		return p8;
	}



	public void setP8(int p8) {
		this.p8 = p8;
	}



	public int getP9() {
		return p9;
	}



	public void setP9(int p9) {
		this.p9 = p9;
	}



	public void setAuthor_url(String author_url) {
		this.author_url = author_url;
	}

	public String getAuthor_legal_disclaimer() {
		return author_legal_disclaimer;
	}

	public void setAuthor_legal_disclaimer(String author_legal_disclaimer) {
		this.author_legal_disclaimer = author_legal_disclaimer;
	}

	public int getVideo_series_id() {
		return video_series_id;
	}

	public void setVideo_series_id(int video_series_id) {
		this.video_series_id = video_series_id;
	}

	public String getVideo_series_name() {
		return video_series_name;
	}

	public void setVideo_series_name(String video_series_name) {
		this.video_series_name = video_series_name;
	}

	public int getRoot_type_id() {
		return root_type_id;
	}

	public void setRoot_type_id(int root_type_id) {
		this.root_type_id = root_type_id;
	}

	public String getRoot_title() {
		return root_title;
	}

	public void setRoot_title(String root_title) {
		this.root_title = root_title;
	}

	public String getRoot_type_name() {
		return root_type_name;
	}

	public void setRoot_type_name(String root_type_name) {
		this.root_type_name = root_type_name;
	}

	public int getRoot_nid() {
		return root_nid;
	}

	public void setRoot_nid(int root_nid) {
		this.root_nid = root_nid;
	}

	public String getPartner_url() {
		return partner_url;
	}

	public void setPartner_url(String partner_url) {
		this.partner_url = partner_url;
	}

	public String getPartner_image_url() {
		return partner_image_url;
	}

	public void setPartner_image_url(String partner_image_url) {
		this.partner_image_url = partner_image_url;
	}

	public String getTitle_old() {
		return title_old;
	}

	public void setTitle_old(String title_old) {
		this.title_old = title_old;
	}

	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}

	public String getChild_body() {
		return child_body;
	}

	public void setChild_body(String child_body) {
		this.child_body = child_body;
	}

	public String getChild_summary() {
		return child_summary;
	}

	public void setChild_summary(String child_summary) {
		this.child_summary = child_summary;
	}

	public List<String> getPartnerlinks_titles() {
		return partnerlinks_titles;
	}

	public void setPartnerlinks_titles(List<String> partnerlinks_titles) {
		this.partnerlinks_titles = partnerlinks_titles;
	}

	public List<String> getPartnerlinks_urls() {
		return partnerlinks_urls;
	}

	public void setPartnerlinks_urls(List<String> partnerlinks_urls) {
		this.partnerlinks_urls = partnerlinks_urls;
	}

	public List<String> getTicker_search() {
		return ticker_search;
	}

	public void setTicker_search(List<String> ticker_search) {
		this.ticker_search = ticker_search;
	}

	public boolean isIndex() {
		return index;
	}

	public void setIndex(boolean index) {
		this.index = index;
	}

	public String getMetatag_data() {
		return metatag_data;
	}

	public void setMetatag_data(String metatag_data) {
		this.metatag_data = metatag_data;
	}

	public int getSeo_standout() {
		return seo_standout;
	}

	public void setSeo_standout(int seo_standout) {
		this.seo_standout = seo_standout;
	}

	public long getOriginal_sitedate() {
		return original_sitedate;
	}

	public void setOriginal_sitedate(long original_sitedate) {
		this.original_sitedate = original_sitedate;
	}

	public long getFinal_time() {
		return final_time;
	}

	public void setFinal_time(long final_time) {
		this.final_time = final_time;
	}

	public int getShow_main_image() {
		return show_main_image;
	}

	public void setShow_main_image(int show_main_image) {
		this.show_main_image = show_main_image;
	}

	public int getFeatured_video_nid() {
		return featured_video_nid;
	}

	public void setFeatured_video_nid(int featured_video_nid) {
		this.featured_video_nid = featured_video_nid;
	}

	public List<Integer> getPriorities() {
		return priorities;
	}

	public void setPriorities(List<Integer> priorities) {
		this.priorities = priorities;
	}

	public String getAdvertising_name() {
		return advertising_name;
	}

	public void setAdvertising_name(String advertising_name) {
		this.advertising_name = advertising_name;
	}

	public String getSub_advertising_name() {
		return sub_advertising_name;
	}

	public void setSub_advertising_name(String sub_advertising_name) {
		this.sub_advertising_name = sub_advertising_name;
	}

	public String getTerm_type() {
		return term_type;
	}

	public void setTerm_type(String term_type) {
		this.term_type = term_type;
	}

	public List<String> getTag_group_name() {
		return tag_group_name;
	}

	public void setTag_group_name(List<String> tag_group_name) {
		this.tag_group_name = tag_group_name;
	}

	public List<Integer> getTag_group_id() {
		return tag_group_id;
	}

	public void setTag_group_id(List<Integer> tag_group_id) {
		this.tag_group_id = tag_group_id;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public int getVid() {
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getInvbody() {
		return invbody;
	}

	public void setInvbody(String invbody) {
		this.invbody = invbody;
	}

	public long getSitedate() {
		return sitedate;
	}

	public void setSitedate(long sitedate) {
		this.sitedate = sitedate;
	}

	public int getContentTypeTid() {
		return contentTypeTid;
	}

	public void setContentTypeTid(int contentTypeTid) {
		this.contentTypeTid = contentTypeTid;
	}

	public String getContentTypeName() {
		return contentTypeName;
	}

	public void setContentTypeName(String contentTypeName) {
		this.contentTypeName = contentTypeName;
	}

	public int getChannelTid() {
		return channelTid;
	}

	public void setChannelTid(int channelTid) {
		this.channelTid = channelTid;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getTickerValue() {
		return tickerValue;
	}

	public void setTickerValue(String tickerValue) {
		this.tickerValue = tickerValue;
	}

	public String getPartenr_name() {
		return partenr_name;
	}

	public void setPartenr_name(String partenr_name) {
		this.partenr_name = partenr_name;
	}

	public int getPartenr_id() {
		return partenr_id;
	}

	public void setPartenr_id(int partenr_id) {
		this.partenr_id = partenr_id;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public long getImage_size() {
		return image_size;
	}

	public void setImage_size(long image_size) {
		this.image_size = image_size;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public int getVideo_size() {
		return video_size;
	}

	public void setVideo_size(int video_size) {
		this.video_size = video_size;
	}

	public String getImg_alt() {
		return img_alt;
	}

	public void setImg_alt(String img_alt) {
		this.img_alt = img_alt;
	}

	public String getImg_title() {
		return img_title;
	}

	public void setImg_title(String img_title) {
		this.img_title = img_title;
	}

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isIs_mfo_url() {
		return is_mfo_url;
	}

	public void setIs_mfo_url(boolean is_mfo_url) {
		this.is_mfo_url = is_mfo_url;
	}

	public boolean isIs_include_ticker() {
		return is_include_ticker;
	}

	public void setIs_include_ticker(boolean is_include_ticker) {
		this.is_include_ticker = is_include_ticker;
	}

	public List<String> getTickers() {
		return tickers;
	}

	public void setTickers(List<String> tickers) {
		this.tickers = tickers;
	}

	public List<String> getTag_names() {
		return tag_names;
	}

	public void setTag_names(List<String> tag_names) {
		this.tag_names = tag_names;
	}

	public List<Integer> getTag_ids() {
		return tag_ids;
	}

	public void setTag_ids(List<Integer> tag_ids) {
		this.tag_ids = tag_ids;
	}

	public int getSubChannelTid() {
		return subChannelTid;
	}

	public void setSubChannelTid(int subChannelTid) {
		this.subChannelTid = subChannelTid;
	}

	public String getSubChannelName() {
		return subChannelName;
	}

	public void setSubChannelName(String subChannelName) {
		this.subChannelName = subChannelName;
	}

	public List<Integer> getAuto_republish_type() {
		return auto_republish_type;
	}

	public void setAuto_republish_type(List<Integer> auto_republish_type) {
		this.auto_republish_type = auto_republish_type;
	}

	public int getSponsored_content() {
		return sponsored_content;
	}

	public void setSponsored_content(int sponsored_content) {
		this.sponsored_content = sponsored_content;
	}

	public String getTitleOrder() {
		return titleOrder;
	}

	public void setTitleOrder(String titleOrder) {
		this.titleOrder = titleOrder;
	}

	@Override
	public String toString() {
		return "Node [nid=" + nid + ", vid=" + vid + ", title=" + title
				+ ", summary=" + summary + ", invbody=" + invbody
				+ ", sitedate=" + sitedate + ", contentTypeTid="
				+ contentTypeTid + ", contentTypeName=" + contentTypeName
				+ ", channelTid=" + channelTid 
				+ ", partenr_name="+ partenr_name 
				+ ", partenr_id="+ partenr_id 
				+ ", image_url="+ image_url 
				+ ", image_size="+ image_size 
				+ ", video_url="+ video_url 
				+ ", video_size="+ video_size 
				+ ", img_alt="+ img_alt 
				+ ", img_title="+ img_title 
				+ ", author_id="+ author_id 
				+ ", author_name="+ author_name 
				+ ", url="+ url 
				+ ", titleOrder=" + titleOrder
				+ "]";
	}
	
	public Node clone(){
		Node newNode = null;
		try {
			newNode = (Node)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return newNode;
	}
	
}
