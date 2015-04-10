package com.valueclickbrands.solr.dao;

import java.util.List;
import java.util.Map;

import com.valueclickbrands.solr.model.AutoRepublish;
import com.valueclickbrands.solr.model.Node;
import com.valueclickbrands.solr.model.PartnerLinks;
import com.valueclickbrands.solr.model.Related;
import com.valueclickbrands.solr.model.SynDate;
import com.valueclickbrands.solr.model.Tag;
import com.valueclickbrands.solr.model.TagGroup;
import com.valueclickbrands.solr.model.Taxonomy;
import com.valueclickbrands.solr.model.TaxonomyTag;
import com.valueclickbrands.solr.model.UrlAlias;
import com.valueclickbrands.solr.model.WebToolSettings;


public interface DumpDao {

	public List<Node> selectNodeDetail( Map<String, Object> searchParam);
	
	public List<UrlAlias> selectALlUrl();
	
	public List<UrlAlias> selectALlUrlById(Map<String, Object> searchParam);
	
	public List<Tag> selectAllTag();
	
	public List<Tag> selectAllTagById(Map<String, Object> searchParam);
	
	public List<TagGroup> selectAllTagGroup();
	
	public List<TagGroup> selectAllTagGroupById(List<Integer> tid);

	public List<Taxonomy> selectTaxonomyForNode(Map<String, Object> searchParam);
	
	public List<TaxonomyTag> selectTaxonomyTagForNode(Map<String, Object> searchParam);
	
	public List<TaxonomyTag> selectTaxonomyTagForPage(Map<String, Object> searchParam);

	public List<Taxonomy> selectTaxonomyForPage(Map<String, Object> searchParam);
	
	public List<UrlAlias> selectALlUrlForPage();
	
	public List<UrlAlias> selectUrlBySource(Map<String, Object> searchParam);
	
	public List<Tag> selectAllTagV2();

	public List<AutoRepublish> selectAutoRepublish(Map<String, Object> searchParam) ;
	
	public List<PartnerLinks> selectPartnerLinks(Map<String, Object> searchParam) ;

	public List<WebToolSettings> selectWebToolSettings(Map<String, Object> searchParam);
	
	public List<SynDate> selectSynDate(Map<String, Object> searchParam); 
	
	public List<Related> selectRelateds(Map<String, Object> searchParam);
}

