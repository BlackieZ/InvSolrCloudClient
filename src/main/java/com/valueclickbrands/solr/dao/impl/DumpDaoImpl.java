package com.valueclickbrands.solr.dao.impl;

import java.util.List;
import java.util.Map;

import com.valueclickbrands.framework.db.IbatisGenericDao;
import com.valueclickbrands.solr.dao.DumpDao;
import com.valueclickbrands.solr.model.Adtarget;
import com.valueclickbrands.solr.model.Advertising;
import com.valueclickbrands.solr.model.AutoRepublish;
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
import com.valueclickbrands.solr.model.PartnerLinks;
import com.valueclickbrands.solr.model.Related;
import com.valueclickbrands.solr.model.SynDate;
import com.valueclickbrands.solr.model.Tag;
import com.valueclickbrands.solr.model.TagGroup;
import com.valueclickbrands.solr.model.Taxonomy;
import com.valueclickbrands.solr.model.TaxonomyTag;
import com.valueclickbrands.solr.model.Timelessness;
import com.valueclickbrands.solr.model.UrlAlias;
import com.valueclickbrands.solr.model.WebToolSettings;
import com.valueclickbrands.solr.model.Website;

public class DumpDaoImpl extends IbatisGenericDao implements DumpDao {
	private String selectNodeDetailSQL = "selectNodeDetail";
	private String selectNodeDetailByIdSQL = "selectNodeDetailById";
	private String prefix = "devel";
	private String selectALlUrlSQL = "selectALlUrl";
	private String selectUrlByIdSQL = "selectUrlById";
	private String selectAllTagSQL = "selectAllTag";
	private String selectTagByIdSQL = "selectTagById";
	private String selectAllTagGroupSQL = "selectAllTagGroup";
	private String selectTagGroupByIdSQL = "selectTagGroupById";

	private String selectTaxonomyForNodeSQL = "selectTaxonomyForNode";
	private String selectTaxonomyForPageSQL = "selectTaxonomyForPage";
	private String selectTaxonomyTagForNodeSQL = "selectTaxonomyTagForNode";
	private String selectTaxonomyTagForPageSQL = "selectTaxonomyTagForPage";
	private String selectALlUrlForPageSQL = "selectALlUrlForPage";
	private String selectUrlBySourceSQL = "selectUrlBySource";	
	private String selectV2AllTagSQL = "selectAllTagv2";
	private String selectAutoRepublishSQL = "selectAutoRepublish";
	private String selectPartnerLinksSQL = "selectPartnerLinks";
	private String selectWeb_tool_settingsSQL = "selectWeb_tool_settings";
	private String selectSynDateSQL = "selectSynDate";
	private String selectRelatedSQL = "selectRelated";

	
	
	

	public void setSelectALlUrlSQL(String selectALlUrlSQL) {
		this.selectALlUrlSQL = selectALlUrlSQL;
	}

	public void setSelectAllTagSQL(String selectAllTagSQL) {
		this.selectAllTagSQL = selectAllTagSQL;
	}

	public void setSelectNodeDetailSQL(String selectNodeDetailSQL) {
		this.selectNodeDetailSQL = selectNodeDetailSQL;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public List<Node> selectNodeDetail(Map<String, Object> searchParam) {
		return this.findForList(selectNodeDetailSQL, searchParam);
	}
	
	
	@Override
	public List<UrlAlias> selectALlUrl(){
		return this.findForList(selectALlUrlSQL, prefix);
	
	}
	
	@Override
	public List<UrlAlias> selectALlUrlById(Map<String, Object> searchParam) {
		return this.findForList(selectUrlByIdSQL, searchParam);
	}
	
	@Override
	public List<Tag> selectAllTag() {
		return this.findForList(selectAllTagSQL, prefix);
	}

	@Override
	public List<Tag> selectAllTagById(Map<String, Object> searchParam) {
		return this.findForList(selectTagByIdSQL, searchParam);
	}
	
	@Override
	public List<TagGroup> selectAllTagGroup() {
		return this.findForList(selectAllTagGroupSQL, prefix);
	}

	@Override
	public List<TagGroup> selectAllTagGroupById(List<Integer> tid) {
		return this.findForList(selectTagGroupByIdSQL, tid);
	}

	@Override
	public List<Taxonomy> selectTaxonomyForNode(Map<String, Object> searchParam) {
		return this.findForList(selectTaxonomyForNodeSQL,  searchParam);
	}

	@Override
	public List<TaxonomyTag> selectTaxonomyTagForNode(Map<String, Object> searchParam) {
		return this.findForList(selectTaxonomyTagForNodeSQL, searchParam);
	}
	
	@Override
	public List<TaxonomyTag> selectTaxonomyTagForPage(Map<String, Object> searchParam) {
		return this.findForList(selectTaxonomyTagForPageSQL, searchParam);
	}

	@Override
	public List<Taxonomy> selectTaxonomyForPage(Map<String, Object> searchParam) {
		return this.findForList(selectTaxonomyForPageSQL, searchParam);
	}
	
	@Override
	public List<UrlAlias> selectALlUrlForPage() {
		return this.findForList(selectALlUrlForPageSQL, prefix);

	}

	@Override
	public List<UrlAlias> selectUrlBySource(Map<String, Object> searchParam) {
		return this.findForList(selectUrlBySourceSQL, searchParam);

	}

	@Override
	public List<Tag> selectAllTagV2() {
		return this.findForList(selectV2AllTagSQL, prefix);

	}
	
	@Override
	public List<AutoRepublish> selectAutoRepublish(Map<String, Object> searchParam) {
		return this.findForList(selectAutoRepublishSQL, searchParam);

	}

	@Override
	public List<PartnerLinks> selectPartnerLinks(
			Map<String, Object> searchParam) {
		return this.findForList(selectPartnerLinksSQL, searchParam);
	}

	@Override
	public List<WebToolSettings> selectWebToolSettings(
			Map<String, Object> searchParam) {
		return this.findForList(selectWeb_tool_settingsSQL, searchParam);
	}

	@Override
	public List<SynDate> selectSynDate(Map<String, Object> searchParam) {
		return this.findForList(selectSynDateSQL, searchParam);
	}

	@Override
	public List<Related> selectRelateds(Map<String, Object> searchParam) {
		return this.findForList(selectRelatedSQL, searchParam);
	}
	
	
	
}
