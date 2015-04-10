package com.valueclickbrands.solr.model;

/** 
 * @author Vanson Zou
 * @date Jan 3, 2015 
 */

public class TaxonomyTag {

	private long tag_id;
	private String tag_name;
	private long nid;
	private long vid;
	private long field_private_taxonomy_value;
	
	public long getTag_id() {
		return tag_id;
	}
	public void setTag_id(long tag_id) {
		this.tag_id = tag_id;
	}
	public String getTag_name() {
		return tag_name;
	}
	public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
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
	public long getField_private_taxonomy_value() {
		return field_private_taxonomy_value;
	}
	public void setField_private_taxonomy_value(long field_private_taxonomy_value) {
		this.field_private_taxonomy_value = field_private_taxonomy_value;
	}
	
	
	
	
	
}
